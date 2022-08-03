package com.smartdubai.controller;

import com.smartdubai.model.Book;
import com.smartdubai.model.BookDTO;
import com.smartdubai.model.CheckOutDTO;
import com.smartdubai.service.BookService;
import com.smartdubai.service.PromoCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "Manage Books", tags = {"books"})
@RequestMapping("/api")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    PromoCodeService promoCodeService;

    @GetMapping("/book")
    @ApiOperation(value = "API to get all books ", response = Book.class)
    public ResponseEntity<List<Book>> getAllBooks() {
        try {

            List<Book> books = new ArrayList<>(bookService.getAllBooks());

            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/book/{bookid}")
    @ApiOperation(value = "API to get book by id ", response = Book.class)
    public ResponseEntity<Book> getBookByID(@PathVariable("bookid") @NonNull int bookId) {
        Optional<Book> book = bookService.getBooksById(bookId);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/book/{bookid}")
    @ApiOperation(value = "API to delete book by id ", response = HttpStatus.class)
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("bookid") @NonNull int bookId) {
        try {
            Optional<Book> bookData = bookService.getBooksById(bookId);
            if (bookData.isPresent()) {
                bookService.delete(bookId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books")
    @ApiOperation(value = "API to add book ", response = Book.class)
    public ResponseEntity<Book> saveBook(@RequestBody BookDTO books) {
        try {
            Book book = bookService.saveOrUpdate(new Book(books.getBookId(), books.getBookName(), books.getBookDescription(), books.getType(), books.getAuthor(), books.getPrice()));
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/books/checkout")
    @ApiOperation(value = "API to checkout book ", response = Book.class)
    public ResponseEntity<BigDecimal> checkout(@RequestParam() String promoCode, @RequestBody List<CheckOutDTO> checkOutBooks) {
        try {
            return bookService.checkOutBooks(promoCode, checkOutBooks);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/books")
    @ApiOperation(value = "API to update book ", response = Book.class)
    public ResponseEntity<Book> updateBook(@RequestBody BookDTO books) {
        Optional<Book> bookData = bookService.getBooksById(books.getBookId());
        if (bookData.isPresent()) {
            return new ResponseEntity<>(bookService.saveOrUpdate(new Book(books.getBookId(), books.getBookName(), books.getBookDescription(), books.getType(), books.getAuthor(), books.getPrice())), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
