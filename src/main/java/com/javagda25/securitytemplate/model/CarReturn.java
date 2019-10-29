package com.javagda25.securitytemplate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CarReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarReturn;

    @CreationTimestamp
    private LocalDateTime dateOfReturn;

    @OneToOne
    private CarRent carRent;

//    @Formula(value = )
    private double additionalPaymentForDelay;

    private double additionalPaymentForCleaning;

    private double additionalPaymentForFuel;

    private String commentsCarReturn;
}
