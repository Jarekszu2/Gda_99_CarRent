package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarStatus;
import com.javagda25.securitytemplate.service.GruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping(path = "/grunt/")
public class GruntController {

    private GruntService gruntService;

    @Autowired
    public GruntController(GruntService carService) {
        this.gruntService = carService;
    }

    @GetMapping("/list_cars")
    public String listCars(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "2") int size) {
        Page<Car> carPage = gruntService.getPageCars(PageRequest.of(page, size));
        model.addAttribute("cars", carPage);
        return "car-list";
    }

    @GetMapping("/add_car")
    public String addCar(Model model, Car car) {
        car.setCarStatus(CarStatus.AVAILABLE);
        CarStatus[] statuses = CarStatus.values();

        model.addAttribute("statuses", statuses);
        model.addAttribute("car", car);
        return "car-add";
    }

    @PostMapping("/add_car")
    public String postAddCar(Car car) {
        gruntService.save(car);
        return "redirect:/grunt/list_cars";
    }

    @GetMapping("/details")
    public String details(Model model, HttpServletRequest request, @RequestParam(name = "carId") Long carId) {
        Optional<Car> optionalCar = gruntService.getById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
//            CarStatus[] statuses = CarStatus.values();

            model.addAttribute("car", car);
//            model.addAttribute("statuses", statuses);
            model.addAttribute("referer", request.getHeader("referer"));
            return "car-details";
        }
        return "redirect:/grunt/list_cars";
    }

    @GetMapping("/list_bookings")
    public String listBookings(Model model,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "2") int size) {
        Page<Booking> bookingPage = gruntService.getPageBookings(PageRequest.of(page, size));
        model.addAttribute("bookings", bookingPage);
        return "bookingAll-list";
    }

    @GetMapping("/add_rent")
    public String addRent(Model model, Booking booking) {
        model.addAttribute(booking);
        return "rent-add";
    }

    @GetMapping("/remove")
    public String remove(
            HttpServletRequest request,
            @RequestParam(name = "carId") Long carId) {
        String referer = request.getHeader("referer");
        carService.remove(carId);

        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/car/list";
    }

    @GetMapping("/edit")
    public String editCar(Model model, @RequestParam(name = "carId") Long carId) {
        Optional<Car> carOptional = carService.getById(carId);
        CarStatus[] statuses = CarStatus.values();


        if (carOptional.isPresent()) {

            model.addAttribute("car", carOptional.get());
            model.addAttribute("statuses", statuses);

            return "car-add";
        }

        return "redirect:/car/list";
    }

}
