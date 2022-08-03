package com.smartdubai.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BookDTO {
    private int bookId;
    private String bookName;
    private String bookDescription;
    private String type;
    private String author;
    private BigDecimal price;
}