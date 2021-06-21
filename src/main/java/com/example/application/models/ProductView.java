package com.example.application.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductView {
    @Id
    private Long id;

    @OneToOne
    private Product product;

}
