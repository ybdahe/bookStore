package com.smartdubai.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CheckOutDTO {
    private int bookId;
    private String bookName;
}