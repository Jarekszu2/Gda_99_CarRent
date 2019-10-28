package com.javagda25.securitytemplate.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCar;

    @NotNull
    private String name;
    @NotNull
    private double price;
    @NotEmpty
    @NotNull
    private int mileage;
    @NotEmpty
    @NotNull
    private CarStatus carStatus;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private Set<Booking> bookings;
}
