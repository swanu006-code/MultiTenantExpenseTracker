package com.example.multitenantExpenseTracker.controller;

//package com.example.multitenantExpenseTracker.controller;

import com.example.multitenantExpenseTracker.model.expense;
import com.example.multitenantExpenseTracker.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private ExpenseService expenseService;

    // ADMIN ONLY — enforced by SecurityConfig's hasRole("ADMIN") rule
    @GetMapping("/expenses")
    public ResponseEntity<List<expense>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }
}
