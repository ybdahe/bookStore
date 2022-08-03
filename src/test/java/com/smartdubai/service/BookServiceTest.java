package com.smartdubai.service;

import com.smartdubai.model.Book;
import com.smartdubai.model.CheckOutDTO;
import com.smartdubai.model.PromoCode;
import com.smartdubai.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PromoCodeService promoCodeService;

    @InjectMocks
    BookService bookService;


    @Test
    void getBookByID() {
        Book book = new Book();
        book.setBookId(1);
        book.setBookName("Book 1");
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        assertThat(bookService.getBooksById(book.getBookId()).get().getBookId()).isEqualTo(book.getBookId());
    }

    @Test
    void getAllBooks() {
        Book book = new Book();
        List<Book> books = new ArrayList<>();
        book.setBookId(1);
        book.setBookName("Book 1");
        books.add(book);
        when(bookRepository.findAll()).thenReturn(books);
        assertThat(bookService.getAllBooks().stream().findAny().get().getBookId()).isEqualTo(book.getBookId());
    }

    @Test
    void addOrUpdateBook() {
        Book book = new Book();
        book.setBookId(1);
        book.setBookName("Book 1");
        when(bookRepository.save(book)).thenReturn(book);
        assertThat(bookService.saveOrUpdate(book).getBookId()).isEqualTo(book.getBookId());
    }

    @Test
    void checkOutBooks() {
        Book book = new Book();
        String promoCodeStr = "NOVEL10";
        List<Book> books = new ArrayList<>();
        List<CheckOutDTO> checkOutBooks = new ArrayList<>();
        book.setBookId(1);
        book.setBookName("Book 1");
        book.setType("NOVEL");
        book.setPrice(BigDecimal.TEN);
        books.add(book);
        PromoCode promoCode = new PromoCode(1,"NOVEL10");
        checkOutBooks.add(new CheckOutDTO(1,"Book 1"));
        when(promoCodeService.getPromoCodeByCodeName(promoCodeStr)).thenReturn(Optional.of(promoCode));
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        assertThat(bookService.checkOutBooks("NOVEL10",checkOutBooks).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void checkOutBooksError() {
        Book book = new Book();
        String promoCodeStr = "NOVEL10";
        List<CheckOutDTO> checkOutBooks = new ArrayList<>();
        book.setBookId(1);
        book.setBookName("Book 1");
        book.setType("NOVEL");
        PromoCode promoCode = new PromoCode(1,"NOVEL10");
        checkOutBooks.add(new CheckOutDTO(1,"Book 1"));
        when(promoCodeService.getPromoCodeByCodeName(promoCodeStr)).thenReturn(Optional.of(promoCode));
        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        assertThat(bookService.checkOutBooks("NOVEL10",checkOutBooks).getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}