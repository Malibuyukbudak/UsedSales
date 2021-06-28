package com.example.application.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String city;
    private String cityDistrict;
    private String address;
    private Double price;
    private Byte[] image;
    private String description;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;

}
