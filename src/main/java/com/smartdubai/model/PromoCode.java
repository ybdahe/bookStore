package com.smartdubai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCode {
    //Defining book id as primary key
    @Id
    @Column
    private int codeId;
    @Column
    private String codeName;
}