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

import java.util.List;

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
}
