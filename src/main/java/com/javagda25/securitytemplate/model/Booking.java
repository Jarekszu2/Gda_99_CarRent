package com.javagda25.securitytemplate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

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
    private LocalDateTime bookingDateTime;

    @NotNull
    private LocalDateTime dateStart;

    @NotNull
    private LocalDateTime dateEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account employee;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;

    @OneToOne
    private CarRent carRent;

//    @Formula(value = )
    private double cancellation;

//    @Formula(value = "select timestampdiff(DAY, booking.date_end, booking.date_start) * car.getPrice()")
//    private double amount;
}
