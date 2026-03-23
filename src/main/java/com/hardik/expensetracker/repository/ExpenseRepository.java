package com.hardik.expensetracker.repository;

import com.hardik.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    
    List<Expense> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    Double getTotalAmountByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId AND e.date BETWEEN :startDate AND :endDate")
    Double getTotalAmountByUserIdAndDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}