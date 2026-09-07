package com.muhammed.library_management.service;

import java.util.List;

import com.muhammed.library_management.entity.Book;
import com.muhammed.library_management.exception.BookNotFoundException;
import com.muhammed.library_management.repository.BookRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class BookService {

    private final BookRepository bookRepository;

    public  Book addBook(Book book){
        book.setAvailableCopies(book.getTotalCopies());
        return bookRepository.save(book);
    }



    public List<Book> getAllBooks(){
        return bookRepository.findAll();


    }

    public Book getBookById(Long id){
        return bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException("Kitap Bulunamadı, id: " + id));

    }

    public Book updateBook(Long id,Book updateBook){
        Book existing = getBookById(id);
        existing.setTitle(updateBook.getTitle());
        existing.setAuthor(updateBook.getAuthor());
        existing.setIsbn(updateBook.getIsbn());
        existing.setTotalCopies(updateBook.getTotalCopies());

        return bookRepository.save(existing);


    }

    public void deleteBook (Long id){
        Book book = getBookById(id);
        bookRepository.delete(book);
    }


    
}
