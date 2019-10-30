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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDateTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateStart;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateEnd;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Account employee;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;

    @OneToOne
    private CarRent carRent;

    private boolean cancellation = false;

//    @Formula(value = "select timestampdiff(DAY, booking.date_end, booking.date_start) * car.getPrice()")
//    private double amount;
}
