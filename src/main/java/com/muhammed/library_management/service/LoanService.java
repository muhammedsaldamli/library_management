package com.muhammed.library_management.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.muhammed.library_management.entity.Book;
import com.muhammed.library_management.entity.Loan;
import com.muhammed.library_management.entity.Member;
import com.muhammed.library_management.exception.BookNotAvailableException;
import com.muhammed.library_management.exception.LoanNotFoundException;
import com.muhammed.library_management.repository.LoanRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final MemberService memberService;

    private static final int LOAN_PERIOD_DAYS = 14;

    public Loan borrowBook(Long bookId, Long memberId) {
        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException("Kitabın müsait kopyası yok: " + book.getTitle());
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));

        return loanRepository.save(loan);
    }

    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Ödünç kaydı bulunamadı, id: " + loanId));

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Bu kitap zaten iade edilmiş.");
        }

        loan.setReturnDate(LocalDate.now());

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        return loanRepository.save(loan);
    }

    public boolean isOverdue(Loan loan) {
        if (loan.getReturnDate() != null) {
            return loan.getReturnDate().isAfter(loan.getDueDate());
        }
        return LocalDate.now().isAfter(loan.getDueDate());
    }
}