package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarStatus;
import com.javagda25.securitytemplate.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/car/")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Car> cars = carService.getAll();
        model.addAttribute("cars", cars);
        return "car-list";
    }

    @GetMapping("/add")
    public String addCar(Model model, Car car) {
        car.setCarStatus(CarStatus.AVAILABLE);
        CarStatus[] statuses = CarStatus.values();

        model.addAttribute("statuses", statuses);
        model.addAttribute("car", car);
        return "car-add";
    }

    @PostMapping("/add")
    public String add(Car car) {
        carService.save(car);
        return "redirect:/car/list";
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
        return "redirect:/car/list";
    }
}
