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
    private String regNr;

    @NotNull
    private double price;

    @NotNull
    private int mileage;

    @NotNull
    private CarStatus carStatus;

    @NotNull
    private double feeForCleaning;

    @NotNull
    private double feeForFuel;

    @NotNull
    private double cancellationCost;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    private Set<Booking> bookings;
}
