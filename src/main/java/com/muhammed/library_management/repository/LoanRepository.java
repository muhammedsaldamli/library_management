package com.muhammed.library_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muhammed.library_management.entity.Loan;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    // Bir üyenin henüz iade etmediği kitapları bulmak için
    List<Loan> findByMemberIdAndReturnDateIsNull(Long memberId);

    // Bir kitabın şu an ödünçte olup olmadığını kontrol etmek için
    List<Loan> findByBookIdAndReturnDateIsNull(Long bookId);
}