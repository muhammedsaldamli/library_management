package com.muhammed.library_management;

import com.muhammed.library_management.entity.Book;
import com.muhammed.library_management.entity.Loan;
import com.muhammed.library_management.entity.Member;
import com.muhammed.library_management.exception.BookNotAvailableException;
import com.muhammed.library_management.repository.LoanRepository;
import com.muhammed.library_management.service.BookService;
import com.muhammed.library_management.service.LoanService;
import com.muhammed.library_management.service.MemberService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookService bookService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private LoanService loanService;

    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Suç ve Ceza");
        book.setAvailableCopies(2);

        member = new Member();
        member.setId(1L);
        member.setName("Ahmet Yılmaz");
    }

    @Test
    void borrowBook_shouldDecreaseAvailableCopies_whenAvailable() {
        when(bookService.getBookById(1L)).thenReturn(book);
        when(memberService.getMemberById(1L)).thenReturn(member);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan loan = loanService.borrowBook(1L, 1L);

        assertThat(book.getAvailableCopies()).isEqualTo(1);
        assertThat(loan.getBook()).isEqualTo(book);
        assertThat(loan.getMember()).isEqualTo(member);
        assertThat(loan.getDueDate()).isEqualTo(loan.getLoanDate().plusDays(14));
    }

    @Test
    void borrowBook_shouldThrowException_whenNoCopiesAvailable() {
        book.setAvailableCopies(0);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(memberService.getMemberById(1L)).thenReturn(member);

        assertThatThrownBy(() -> loanService.borrowBook(1L, 1L))
                .isInstanceOf(BookNotAvailableException.class)
                .hasMessageContaining("Suç ve Ceza");
    }

    @Test
    void returnBook_shouldIncreaseAvailableCopies() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setMember(member);
        book.setAvailableCopies(1);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan result = loanService.returnBook(1L);

        assertThat(book.getAvailableCopies()).isEqualTo(2);
        assertThat(result.getReturnDate()).isNotNull();
    }

    @Test
    void returnBook_shouldThrowException_whenAlreadyReturned() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setBook(book);
        loan.setReturnDate(java.time.LocalDate.now().minusDays(1));

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));

        assertThatThrownBy(() -> loanService.returnBook(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("zaten iade edilmiş");
    }
}