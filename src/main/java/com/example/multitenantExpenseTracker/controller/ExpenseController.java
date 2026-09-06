package com.example.multitenantExpenseTracker.controller;

import com.example.multitenantExpenseTracker.model.expense;
import com.example.multitenantExpenseTracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/mlt")
@RestController
public class ExpenseController {
    @Autowired
    private ExpenseService expenseservice;

    @PostMapping
    public ResponseEntity<expense>addExpense(@RequestBody expense expense, Authentication authentication){
        expense saved= expenseservice.createExpense(expense,authentication);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<expense>>getexpense(Authentication authentication){
        return ResponseEntity.ok(expenseservice.getMyExpenses(authentication));
    }
    @GetMapping("/{id}")
    public ResponseEntity<expense>getExpenseById(@PathVariable("id") Long Id,Authentication authentication){
        return ResponseEntity.ok(expenseservice.getExpenseById(Id,authentication));
    }
    @PutMapping("/{id}")
    public ResponseEntity<expense> updateExpense(@PathVariable Long id, @RequestBody expense updatedData, Authentication authentication) {
        return ResponseEntity.ok(expenseservice.updateExpense(id, updatedData, authentication));
    }

    // DELETE — remove one of MY expenses
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id, Authentication authentication) {
        expenseservice.deleteExpense(id, authentication);
        return ResponseEntity.ok("Expense deleted successfully");
    }

    // SUMMARY — total spend by category
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Double>> getSummary(Authentication authentication) {
        return ResponseEntity.ok(expenseservice.getCategorySummary(authentication));
    }
}
