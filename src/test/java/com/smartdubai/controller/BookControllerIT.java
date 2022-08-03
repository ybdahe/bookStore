package com.smartdubai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartdubai.model.Book;
import com.smartdubai.model.BookDTO;
import com.smartdubai.service.BookService;
import com.smartdubai.service.PromoCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    PromoCodeService promoCodeService;


    @Test
    void getAllBooksWithNoContent() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("bookName", "xyz");
        mockMvc.perform(get("/api/book").params(paramsMap))
                .andExpect(status().isNoContent());
    }

    @Test
    void createBook() throws Exception {

        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(1);
        bookDTO.setBookName("Book 1");
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    void getAllBooks()
            throws Exception {

        Book book = new Book();
        book.setBookName("Book 1");

        List<Book> allBooks = Collections.singletonList(book);

        given(bookService
                .getAllBooks())
                .willReturn(allBooks);

        mockMvc.perform(get("/api/book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test()
    void updateBooks() throws Exception {

        Book book = new Book();
        book.setBookId(1);
        book.setBookName("Book 1");
        BookDTO bookDTO = new BookDTO(book.getBookId(),book.getBookName(),book.getBookDescription(),book.getType(),book.getAuthor(),book.getPrice());
        given(bookService.getBooksById(book.getBookId())).willReturn(Optional.of(book));
        given(bookService
                .saveOrUpdate(book))
                .willReturn(book);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/api/books")
                        .content(mapper.writeValueAsString(bookDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBooks() throws Exception {
        Book book = new Book();
        book.setBookId(1);
        book.setBookName("Book 1");

        given(bookService.getBooksById(book.getBookId())).willReturn(Optional.of(book));
        doNothing().when(bookService).delete(1);
        mockMvc.perform(delete("/api/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}