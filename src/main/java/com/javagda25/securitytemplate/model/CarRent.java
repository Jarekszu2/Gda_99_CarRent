package com.javagda25.securitytemplate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CarRent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarRent;

    @OneToOne
    private Booking booking;

    @OneToOne
    private CarReturn carReturn;

    @Formula(value = "1000")
    private int revenue;

    private boolean carReturned;

    private String commentsCarRent;
}
