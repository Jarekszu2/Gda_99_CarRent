package com.javagda25.securitytemplate.model;

import lombok.*;
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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    private Booking booking;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne
    private CarReturn carReturn;

    @Transient
//    @Formula(value = "1000")
    private int revenue;

    private boolean carReturned;

    private String commentsCarRent;
}
