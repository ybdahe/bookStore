package com.smartdubai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    //Defining book id as primary key
    @Id
    @Column
    private int bookId;
    @Column
    private String bookName;
    @Column
    private String bookDescription;
    @Column
    private String type;
    @Column
    private String author;
    @Column
    private BigDecimal price;
}