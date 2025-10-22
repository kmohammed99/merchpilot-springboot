package com.merchpilot.merchpilot.repository;

import com.merchpilot.merchpilot.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
