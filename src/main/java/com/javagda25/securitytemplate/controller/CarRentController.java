package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.*;
import com.javagda25.securitytemplate.service.BookingService;
import com.javagda25.securitytemplate.service.CarRentService;
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
import java.util.List;

@Controller
@RequestMapping("/carRent")
public class CarRentController {

    @Autowired
    private CarRentService carRentService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CarService carService;

    @GetMapping("/add_rent")
    public String addRent(Model model,
                          @RequestParam(name = "idBooking") Long idBooking,
                          HttpServletRequest request) {
        Booking booking = bookingService.getBookingById(idBooking);
        Car car = booking.getCar();
//        Long idCar = car.getIdCar();
        String carName = car.getName();
        Account client = booking.getClient();
//        Long idClient = client.getId();
        String clientName = client.getName();
        String clientSurname = client.getSurname();
        model.addAttribute("booking", booking);
//        model.addAttribute("idCar", idCar);
        model.addAttribute("carName", carName);
//        model.addAttribute("idClient", idClient);
        model.addAttribute("clientName", clientName);
        model.addAttribute("clientSurname", clientSurname);
        model.addAttribute("referer", request.getHeader("referer"));
        return "carRent-add";
    }

    @PostMapping("/add_rent")
    public String postAddRent(CarRent carRent, Long idBooking) {
        Booking booking = bookingService.getBookingById(idBooking);
        booking.setCarRented(true);
        bookingService.save(booking);
        Car car = booking.getCar();
        car.setCarStatus(CarStatus.RENTED);
        carService.save(car);
        carRent.setBooking(booking);
        carRentService.saveCarRent(carRent);
        return "redirect:/carRent/list_carRents";
    }

    @GetMapping("/list_carRents")
    public String listCarRents(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "2") int size) {
        Page<CarRent> carRentPage = carRentService.getPageCarRents(PageRequest.of(page, size));
        model.addAttribute("carRents", carRentPage);
        return "carRent-list";
    }
}
