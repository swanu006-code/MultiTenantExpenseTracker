package com.example.multitenantExpenseTracker.dao;

import com.example.multitenantExpenseTracker.model.expense;
import com.example.multitenantExpenseTracker.model.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepo extends JpaRepository <expense,Long> {
List<expense> findByUser(user user);
List<expense> findByUserAndCategory(user user, String category);

}
