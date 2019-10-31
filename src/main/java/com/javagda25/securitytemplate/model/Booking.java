package com.javagda25.securitytemplate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBooking;

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate bookingDate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateStart;

    @NotNull
    private int hiresDays;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Account employee;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;

    @OneToOne
    private CarRent carRent;

    private boolean canceled = false;

//    @Formula(value = "select timestampdiff(DAY, booking.date_end, booking.date_start) * car.getPrice()")
//    private double amount;
}
