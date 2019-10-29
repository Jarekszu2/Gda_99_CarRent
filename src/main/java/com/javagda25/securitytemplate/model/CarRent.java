package com.javagda25.securitytemplate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
    private Income income;

    private String commentsCarRent;
}
