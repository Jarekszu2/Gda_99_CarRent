package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Account;
import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarStatus;
import com.javagda25.securitytemplate.service.AccountService;
import com.javagda25.securitytemplate.service.BookingService;
import com.javagda25.securitytemplate.service.GruntService;
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
@RequestMapping(path = "/account/")
public class BookingController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GruntService gruntService;

    @GetMapping("/client")
    public String client(Model model,
                         Principal principal) {
        if (principal == null) {
            // nie jest zalogowany
            return "login-form";
        } else {
            Account account = accountService.findByUsername(principal.getName());
            model.addAttribute("account", account);
            return "booking-account";
        }
    }

    @GetMapping("/car_list")
    public String carsForClient(Model model,
                                Principal principal) {

        List<Car> cars = gruntService.getCarsAVAILABLE();
        model.addAttribute("cars", cars);

        Account account = accountService.findByUsername(principal.getName());
        Long idClient = account.getId();
        List<Booking> bookingList = bookingService.listBookingsOfClient(idClient);
        Set<Car> carSetBooked = bookingList.stream()
                .map(booking -> booking.getCar())
                .filter(car -> car.getCarStatus().equals(CarStatus.BOOKED))
                .collect(Collectors.toSet());
        model.addAttribute("carsBooked", carSetBooked);

//        Account account = accountService.findByUsername(principal.getName());
//        Long idClient = account.getId();
//        List<Booking> bookingList = bookingService.listBookingsOfClient(idClient);
        Set<Car> carSetRented = bookingList.stream()
                .map(booking -> booking.getCar())
                .filter(car -> car.getCarStatus().equals(CarStatus.RENTED))
                .collect(Collectors.toSet());
        model.addAttribute("carsRented", carSetRented);

        return "carForClient-list";
    }
//    public String carAvailableList(Model model,
//                                   @RequestParam(name = "page", defaultValue = "0") int page,
//                                   @RequestParam(name = "size", defaultValue = "5") int size,
//                                   @RequestParam(name = "available", defaultValue = "false") boolean available,
//                                   @RequestParam(name = "booked", defaultValue = "false") boolean booked,
//                                   @RequestParam(name = "serviced", defaultValue = "false") boolean serviced) {
//        boolean allfalse = !available && !booked && !serviced;
//        List<String> statuses = new ArrayList<>();
//        if (available || allfalse) statuses.add("AVAILABLE");
//        if (booked) statuses.add("BOOKED");
//        if (serviced) statuses.add("SERVICED");
//
//        Page<Car> carPage = gruntService.getPageCarsByStatus(statuses, PageRequest.of(page, size));
//        model.addAttribute("cars", carPage);
//        model.addAttribute("statuses", statuses);
//        return "car-list";

    @GetMapping("/details")
    public String details(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "carId") Long carId) {
        Optional<Car> optionalCar = gruntService.getById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();

            model.addAttribute("car", car);
            model.addAttribute("referer", request.getHeader("referer"));
            return "carForClient-details";
        }
        return "redirect:/account/car_list";
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
                Car car = gruntService.getCarById(carId);
                model.addAttribute("cars", car);
            } else {
                List<Car> carsAvailable = gruntService.getCarsByStatus(CarStatus.AVAILABLE);
                model.addAttribute("cars", carsAvailable);
            }

            model.addAttribute("booking", booking);
            model.addAttribute("referer", request.getHeader("referer"));
            return "booking-add";
        }
    }

    @PostMapping("/booking_add")
    public String addBook(Model model, Booking booking, Principal principal, Long carId) {
        Car car = gruntService.getCarById(carId);
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
        return "redirect:/account/bookings";
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
        return "redirect:/account/bookings";
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
        car.setCarStatus(CarStatus.RENTED);
        booking.setCar(car);
        booking.setAccepted(true);
        bookingService.save(booking);
        return "redirect:/account/bookings_accepted";
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
}
