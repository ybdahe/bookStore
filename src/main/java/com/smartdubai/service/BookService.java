package com.smartdubai.service;

import com.smartdubai.model.Book;
import com.smartdubai.model.CheckOutDTO;
import com.smartdubai.model.PromoCode;
import com.smartdubai.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String NOVEL_10 = "NOVEl10";
    public static final String COMIC_15 = "COMIC15";

    public static final String NOVEL = "NOVEl";
    public static final String COMIC = "COMIC";

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PromoCodeService promoCodeService;

    public ResponseEntity<BigDecimal> checkOutBooks(String promoCode, List<CheckOutDTO> checkOutBooks) {
        logger.info("checkOutBooks started ");
        try {
            List<Book> books = new ArrayList<>();
            Optional<BigDecimal> totalPrice;
            Optional<BigDecimal> otherPrice = Optional.of(BigDecimal.ZERO);
            getAllBooksForCheckOut(checkOutBooks, books);
            if (promoCode.isEmpty()) {
                totalPrice = books.stream().map(Book::getPrice).reduce(BigDecimal::add);
            } else {
                Optional<PromoCode> code = promoCodeService.getPromoCodeByCodeName(promoCode);
                if (code.isPresent()) {
                    if (code.get().getCodeName().equalsIgnoreCase(NOVEL_10)) {
                        totalPrice = books.stream().filter(e -> e.getType().equalsIgnoreCase(NOVEL)).map(e -> e.getPrice().subtract(e.getPrice().multiply(BigDecimal.valueOf(0.1)))).reduce(BigDecimal::add);
                        otherPrice = books.stream().filter(e -> !e.getType().equalsIgnoreCase(NOVEL)).map(Book::getPrice).reduce(BigDecimal::add);
                    } else if (code.get().getCodeName().equalsIgnoreCase(COMIC_15)) {
                        totalPrice = books.stream().map(e -> e.getPrice().subtract(e.getPrice().multiply(BigDecimal.valueOf(0.15)))).reduce(BigDecimal::add);
                        otherPrice = books.stream().filter(e -> !e.getType().equalsIgnoreCase(COMIC)).map(Book::getPrice).reduce(BigDecimal::add);
                    } else {
                        totalPrice = books.stream().map(Book::getPrice).reduce(BigDecimal::add);
                    }
                } else {
                    totalPrice = books.stream().map(Book::getPrice).reduce(BigDecimal::add);
                }
            }
            if (totalPrice.isPresent()) {
                if (otherPrice.isPresent()) {
                    totalPrice = Optional.of(totalPrice.get().add(otherPrice.get()));
                }
            } else {
                logger.error("Total price null ");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return totalPrice.map(bigDecimal -> new ResponseEntity<>(bigDecimal, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        } catch (Exception e) {
            logger.error("Exception {}" ,e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void getAllBooksForCheckOut(List<CheckOutDTO> checkOutBooks, List<Book> books) {
        for (CheckOutDTO book : checkOutBooks) {
            Optional<Book> bookData = getBooksById(book.getBookId());
            bookData.ifPresent(books::add);
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    public Optional<Book> getBooksById(int id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return book;
        } else {
            return Optional.ofNullable(null);
        }
    }

    public Book saveOrUpdate(Book books) {
        return bookRepository.save(books);
    }

    public void delete(int id) {
        bookRepository.deleteById(id);
    }
}