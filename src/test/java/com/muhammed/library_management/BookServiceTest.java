package com.muhammed.library_management;

import com.muhammed.library_management.entity.Book;
import com.muhammed.library_management.exception.BookNotFoundException;
import com.muhammed.library_management.repository.BookRepository;
import com.muhammed.library_management.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Suç ve Ceza");
        book.setAuthor("Dostoyevski");
        book.setIsbn("1234567890");
        book.setTotalCopies(3);
        book.setAvailableCopies(3);
    }

    @Test
    void addBook_shouldSetAvailableCopiesEqualToTotalCopies() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book newBook = new Book();
        newBook.setTitle("1984");
        newBook.setAuthor("Orwell");
        newBook.setIsbn("999");
        newBook.setTotalCopies(5);

        Book result = bookService.addBook(newBook);

        assertThat(result.getAvailableCopies()).isEqualTo(5);
        verify(bookRepository, times(1)).save(newBook);
    }

    @Test
    void getBookById_shouldReturnBook_whenExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBookById(1L);

        assertThat(result.getTitle()).isEqualTo("Suç ve Ceza");
    }

    @Test
    void getBookById_shouldThrowException_whenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<Book> result = bookService.getAllBooks();

        assertThat(result).hasSize(1);
    }

    @Test
    void deleteBook_shouldCallRepositoryDelete_whenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(book);
    }
}