-- === Enums ===============================================================
DO $$ BEGIN
CREATE TYPE payment_method AS ENUM ('CASH','CARD','VODAFONE_CASH','FAWRY');
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

DO $$ BEGIN
CREATE TYPE order_status AS ENUM ('PENDING','IN_PROGRESS','DELIVERED','RETURNED','CANCELED');
EXCEPTION WHEN duplicate_object THEN NULL; END $$;

-- === Brands (multi-brand ready) =========================================
CREATE TABLE IF NOT EXISTS brands (
                                      id          BIGSERIAL PRIMARY KEY,
                                      name        VARCHAR(120) NOT NULL UNIQUE
    );

-- === Products ============================================================
/*
  - colors و sizes كـ text[] عشان المنتج يقدر يبقى له أكتر من لون/قياس
  - total_sell للتتبع السريع
*/
CREATE TABLE IF NOT EXISTS products (
                                        id               BIGSERIAL PRIMARY KEY,
                                        brand_id         BIGINT NOT NULL REFERENCES brands(id) ON DELETE CASCADE,
    name             VARCHAR(200) NOT NULL,
    category         VARCHAR(120) NOT NULL,
    sku              VARCHAR(120) NOT NULL UNIQUE,
    colors           TEXT[]       NOT NULL DEFAULT '{}',
    sizes            TEXT[]       NOT NULL DEFAULT '{}',
    cost_price       NUMERIC(12,2) NOT NULL DEFAULT 0,
    selling_price    NUMERIC(12,2) NOT NULL DEFAULT 0,
    minimum_stock    INT           NOT NULL DEFAULT 0,
    total_quantity   INT           NOT NULL DEFAULT 0,
    total_sell       INT           NOT NULL DEFAULT 0,
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at       TIMESTAMPTZ   NOT NULL DEFAULT now()
    );

-- Trigger to auto-update updated_at (optional)
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_products_updated_at ON products;
CREATE TRIGGER trg_products_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- === Expenses ============================================================
CREATE TABLE IF NOT EXISTS expenses (
                                        id                 BIGSERIAL PRIMARY KEY,
                                        brand_id           BIGINT NOT NULL REFERENCES brands(id) ON DELETE CASCADE,
    expense_category   VARCHAR(120) NOT NULL,
    payment_method     payment_method NOT NULL,
    expense_collection VARCHAR(120) NOT NULL,
    description        TEXT,
    expense_date       DATE NOT NULL,
    amount             NUMERIC(12,2) NOT NULL CHECK (amount >= 0)
    );

-- === Shipping ============================================================
CREATE TABLE IF NOT EXISTS shipping_companies (
                                                  id          BIGSERIAL PRIMARY KEY,
                                                  brand_id    BIGINT NOT NULL REFERENCES brands(id) ON DELETE CASCADE,
    name        VARCHAR(150) NOT NULL,
    company_fee NUMERIC(12,2) NOT NULL DEFAULT 0
    );

CREATE UNIQUE INDEX IF NOT EXISTS uq_shipping_company_brand_name
    ON shipping_companies (brand_id, name);

CREATE TABLE IF NOT EXISTS shipping_company_cities (
                                                       id            BIGSERIAL PRIMARY KEY,
                                                       company_id    BIGINT NOT NULL REFERENCES shipping_companies(id) ON DELETE CASCADE,
    city          VARCHAR(120) NOT NULL,
    price         NUMERIC(12,2) NOT NULL DEFAULT 0
    );

CREATE UNIQUE INDEX IF NOT EXISTS uq_company_city
    ON shipping_company_cities (company_id, city);

-- === Orders (Header + Items) ============================================
CREATE TABLE IF NOT EXISTS orders (
                                      id               BIGSERIAL PRIMARY KEY,
                                      brand_id         BIGINT NOT NULL REFERENCES brands(id) ON DELETE CASCADE,

    customer_name    VARCHAR(160) NOT NULL,
    phone            VARCHAR(40)  NOT NULL,
    email            VARCHAR(160),
    government       VARCHAR(120),
    address          VARCHAR(300),

    payment_method   payment_method NOT NULL,
    shipping_company_id BIGINT REFERENCES shipping_companies(id),

    discount         NUMERIC(12,2) NOT NULL DEFAULT 0,
    status           order_status  NOT NULL DEFAULT 'PENDING',
    total_amount     NUMERIC(12,2) NOT NULL DEFAULT 0, -- before discount + shipping
    net_amount       NUMERIC(12,2) NOT NULL DEFAULT 0, -- after discount (+/-) fees
    order_date       TIMESTAMPTZ   NOT NULL DEFAULT now()
    );

-- بنود الأوردر (بدل ما نخزن المنتج/اللون/المقاس في نفس الجدول)
CREATE TABLE IF NOT EXISTS order_items (
                                           id           BIGSERIAL PRIMARY KEY,
                                           order_id     BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id   BIGINT,             -- اختياري لو عايز تربطه بمنتج
    product_name VARCHAR(200) NOT NULL, -- snapshot name
    color        VARCHAR(60),
    size         VARCHAR(60),
    quantity     INT NOT NULL CHECK (quantity > 0),
    unit_price   NUMERIC(12,2) NOT NULL DEFAULT 0,
    line_total   NUMERIC(12,2) NOT NULL DEFAULT 0
    );

-- فهارس مساعدة
CREATE INDEX IF NOT EXISTS idx_orders_brand_date ON orders(brand_id, order_date DESC);
CREATE INDEX IF NOT EXISTS idx_expenses_brand_date ON expenses(brand_id, expense_date DESC);
