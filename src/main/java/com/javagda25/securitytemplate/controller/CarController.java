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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/car/")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Autowired
    private BookingService bookingService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/list_cars")
//    public String listCars(Model model,
//                           @RequestParam(name = "page", defaultValue = "0") int page,
//                           @RequestParam(name = "size", defaultValue = "2") int size) {
//        Page<Car> carPage = gruntService.getPageCars(PageRequest.of(page, size));
//        model.addAttribute("cars", carPage);
//        return "car-list";
//    }
    public String carList(Model model,
                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "6") int size,
                                   @RequestParam(name = "available", defaultValue = "false") boolean available,
                                   @RequestParam(name = "booked", defaultValue = "false") boolean booked,
                                   @RequestParam(name = "rented", defaultValue = "false") boolean rented,
                                   @RequestParam(name = "serviced", defaultValue = "false") boolean serviced) {
        boolean allfalse = !available && !booked && !serviced;
        List<String> statuses = new ArrayList<>();
        if (available || allfalse) statuses.add("AVAILABLE");
        if (booked) statuses.add("BOOKED");
        if (rented) statuses.add("RENTED");
        if (serviced) statuses.add("SERVICED");

        Page<Car> carPage = carService.getPageCarsByStatus(statuses, PageRequest.of(page, size));
        model.addAttribute("cars", carPage);
        model.addAttribute("statuses", statuses);
        model.addAttribute("available", available);
        model.addAttribute("booked", booked);
        model.addAttribute("rented", rented);
        model.addAttribute("serviced", serviced);
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
        carService.save(car);
        return "redirect:/car/list_cars";
    }

    @GetMapping("/details")
    public String details(Model model, HttpServletRequest request, @RequestParam(name = "carId") Long carId) {
        Optional<Car> optionalCar = carService.getById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
//            CarStatus[] statuses = CarStatus.values();

            model.addAttribute("car", car);
//            model.addAttribute("statuses", statuses);
            model.addAttribute("referer", request.getHeader("referer"));
            return "car-details";
        }
        return "redirect:/car/list_cars";
    }

    @GetMapping("/car_list")
    public String carsForClient(Model model,
                                Principal principal) {

        List<Car> cars = carService.getCarsAVAILABLE();
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

    @GetMapping("/details2")
    public String details2(Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "carId") Long carId) {
        Optional<Car> optionalCar = carService.getById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();

            model.addAttribute("car", car);
            model.addAttribute("referer", request.getHeader("referer"));
            return "carForClient-details";
        }
        return "redirect:/car/car_list";
    }

//    @GetMapping("/remove")
//    public String remove(
//            HttpServletRequest request,
//            @RequestParam(name = "carId") Long carId) {
//        String referer = request.getHeader("referer");
//        gruntService.remove(carId);
//
//        if (referer != null) {
//            return "redirect:" + referer;
//        }
//        return "redirect:/car/list";
//    }

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
