package com.javagda25.securitytemplate.service;

import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Booking> listBookingsOfClient(Long idClient) {
        return bookingRepository.findByClient_Id(idClient);
    }

    public Optional<Booking> getById(Long idBooking) {
        return bookingRepository.findById(idBooking);
    }
}
