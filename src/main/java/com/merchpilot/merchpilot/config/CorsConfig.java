// src/main/java/com/merchpilot/merchpilot/config/CorsConfig.java
package com.merchpilot.merchpilot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                           // كل المسارات
                .allowedOriginPatterns("http://localhost:5173") // أصل الفرونت
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("Authorization","Content-Type","X-Requested-With")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
