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
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(path = "/account/")
public class BookingController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private GruntService carService;

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
    public String carAvailableList(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "2") int size,
                                   @RequestParam(name = "avail", defaultValue = "AVAILABLE") String avail) {
            Page<Car> carPage = carService.getPageCars(PageRequest.of(page, size));
            model.addAttribute("cars", carPage);
            return "carForClient-list";
    }

    @GetMapping("/details")
    public String details(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "carId") Long carId) {
        Optional<Car> optionalCar = carService.getById(carId);
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
        return "booking-summary";
    }

    @GetMapping("/cancellation")
    public String cancellation(Model model,
                               HttpServletRequest request,
                               @RequestParam(name = "idBooking") Long idBooking) {

        Booking booking = bookingService.getBookingById(idBooking);
        model.addAttribute("booking", booking);
        model.addAttribute("referer", request.getHeader("referer"));

        return "booking-cancellation";
    }

    @PostMapping("/cancellation")
    public String postCancellation(Long idBooking) {
        Booking booking = bookingService.getBookingById(idBooking);
        booking.setCanceled(true);
        Car car = booking.getCar();
        car.setCarStatus(CarStatus.AVAILABLE);
        booking.setCar(car);
        bookingService.save(booking);
        return "redirect:/account/bookings";
    }
}
