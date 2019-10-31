package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    private BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void save(Booking booking) {
        bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.getOne(id);
    }
}
