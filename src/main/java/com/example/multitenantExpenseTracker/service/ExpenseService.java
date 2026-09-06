package com.example.multitenantExpenseTracker.service; // adjust to your actual package

import com.example.multitenantExpenseTracker.dao.ExpenseRepo;
import com.example.multitenantExpenseTracker.dao.UserRepository; // adjust name if different
import com.example.multitenantExpenseTracker.model.expense;
import com.example.multitenantExpenseTracker.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepo expenseRepo;

    @Autowired
    private UserRepository userRepository;

    // --- Helper: resolve "who is making this request" from the JWT ---
    public user getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    // --- CREATE ---
    public expense createExpense(expense expense, Authentication authentication) {
        user currentUser = getCurrentUser(authentication);
        expense.setUser(currentUser); // attach ownership — never trust user_id from frontend
        return expenseRepo.save(expense);
    }

    // --- READ (all of current user's expenses) ---
    public List<expense> getMyExpenses(Authentication authentication) {
        user currentUser = getCurrentUser(authentication);
        return expenseRepo.findByUser(currentUser);
    }

    // --- READ (single, with ownership check) ---
    public expense getExpenseById(Long id, Authentication authentication) {
        expense expense = expenseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        checkOwnership(expense, authentication);
        return expense;
    }

    // --- UPDATE ---
    public expense updateExpense(Long id, expense updatedData, Authentication authentication) {
        expense expense = getExpenseById(id, authentication); // reuses ownership check
        expense.setTitle(updatedData.getTitle());
        expense.setAmt(updatedData.getAmt());
        expense.setCategory(updatedData.getCategory());
        expense.setDate(updatedData.getDate());
        return expenseRepo.save(expense);
    }

    // --- DELETE ---
    public void deleteExpense(Long id, Authentication authentication) {
        expense expense = getExpenseById(id, authentication); // reuses ownership check
        expenseRepo.delete(expense);
    }

    // --- OWNERSHIP CHECK (the core security rule of this whole project) ---
    private void checkOwnership(expense expense, Authentication authentication) {
        user currentUser = getCurrentUser(authentication);
        if (!expense.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You do not own this expense");
        }
    }

    // --- SUMMARY: total spend grouped by category ---
    public Map<String, Double> getCategorySummary(Authentication authentication) {
        List<expense> myExpenses = getMyExpenses(authentication);
        return myExpenses.stream()
                .collect(Collectors.groupingBy(
                        expense::getCategory,
                        Collectors.summingDouble(expense::getAmt)
                ));
    }

    // --- ADMIN ONLY: see everyone's expenses ---
    public List<expense> getAllExpenses() {
        return expenseRepo.findAll(); // already built into JpaRepository
    }
}