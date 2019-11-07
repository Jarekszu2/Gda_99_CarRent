package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarStatus;
import com.javagda25.securitytemplate.service.AccountService;
import com.javagda25.securitytemplate.service.BookingService;
import com.javagda25.securitytemplate.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/booking/")
public class BookingController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CarService carService;

    @GetMapping("/list_bookings")
    public String listBookings(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "6") int size) {
        Page<Booking> bookingPage = carService.getNotAcceptedPageBookings(PageRequest.of(page, size));
        model.addAttribute("bookings", bookingPage);
        return "bookingAll-list";
    }

    @GetMapping("/bookings")
    public String addRent(Model model, Principal principal) {
        if (principal == null) {
            // nie jest zalogowany
            return "login-form";
        } else {
            Account account = accountService.findByUsername(principal.getName());
            model.addAttribute("bookings", account.getBookingsClient());
            return "booking-list";
        }
    }

    @GetMapping("/booking_add")
    public String addRent(Model model,
                          Principal principal,
                          HttpServletRequest request,
                          Booking booking,
                          @RequestParam(name = "carId", required = false) Long carId) {
        if (principal == null) {
            return "login-form";
        } else {
            Account account = accountService.findByUsername(principal.getName());
            booking.setClient(account);

            if (carId != null) {
                Car car = carService.getCarById(carId);
                model.addAttribute("cars", car);
            } else {
                List<Car> carsAvailable = carService.getCarsByStatus(CarStatus.AVAILABLE);
                model.addAttribute("cars", carsAvailable);
            }

            model.addAttribute("booking", booking);
            model.addAttribute("referer", request.getHeader("referer"));
            return "booking-add";
        }
    }

    @PostMapping("/booking_add")
    public String addBook(Model model, Booking booking, Principal principal, Long carId) {
        Car car = carService.getCarById(carId);
        car.setCarStatus(CarStatus.BOOKED);
        Account account = accountService.findByUsername(principal.getName());
        booking.setClient(account);
        booking.setCar(car);
        bookingService.save(booking);
        Set<Booking> bookingSet = account.getBookingsClient();
        bookingSet.add(booking);
        account.setBookingsClient(bookingSet);
        accountService.save(account);

        model.addAttribute("unconfirmed", booking);
        return "redirect:/booking/bookings";
    }

    @GetMapping("/cancel")
    public String cancellation(Model model,
                               HttpServletRequest request,
                               @RequestParam(name = "idBooking") Long idBooking) {

        Booking booking = bookingService.getBookingById(idBooking);
        model.addAttribute("booking", booking);
        model.addAttribute("referer", request.getHeader("referer"));

        return "booking-cancellation";
    }

    @PostMapping("/cancel")
    public String postCancellation(Long idBooking) {
        Booking booking = bookingService.getBookingById(idBooking);
        booking.setCanceled(true);
        Car car = booking.getCar();
        car.setCarStatus(CarStatus.AVAILABLE);
        booking.setCar(car);
        bookingService.save(booking);
        return "redirect:/booking/bookings";
    }

    @GetMapping("/accept")
    public String accept(Model model,
                         HttpServletRequest request,
                         @RequestParam(name = "idBooking") Long idBooking) {

        Booking booking = bookingService.getBookingById(idBooking);
        int dni = booking.getHiresDays();
        Car car = booking.getCar();
        int price = car.getPrice();
        int rentValue = dni * price;
        model.addAttribute("rentValue", rentValue);
        model.addAttribute("booking", booking);
        model.addAttribute("referer", request.getHeader("referer"));

        return "booking-accept";
    }

    @PostMapping("/accept")
    public String postAccept(Long idBooking) {
        Booking booking = bookingService.getBookingById(idBooking);
        Car car = booking.getCar();
        car.setCarStatus(CarStatus.BOOKED);
        booking.setCar(car);
        booking.setAccepted(true);
        bookingService.save(booking);
        return "redirect:/booking/bookings_accepted";
    }

    @GetMapping("/bookings_accepted")
    public String getAcceptedBookings(Model model, Principal principal) {
        if (principal == null) {
            // nie jest zalogowany
            return "login-form";
        } else {
            Account account = accountService.findByUsername(principal.getName());
            model.addAttribute("bookings", account.getBookingsClient());
            return "booking-accepted-list";
        }
    }

    @GetMapping("/find")
//    public String findBooking(Model model, Long idBooking) {
    public String findBooking() {

//        model.addAttribute("id", idBooking);
        return "booking-find";
    }

    @PostMapping("/find")
    public String postFindBooking(Model model,
                                  @RequestParam(name = "id") Long id) {
        Page<Booking> page = bookingService.getAllById(id);
        model.addAttribute("bookings", page);
        return "bookingAll-list";
    }
}
