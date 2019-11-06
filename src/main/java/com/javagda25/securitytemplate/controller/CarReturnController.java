package com.javagda25.securitytemplate.controller;

import com.javagda25.securitytemplate.model.Booking;
import com.javagda25.securitytemplate.model.Car;
import com.javagda25.securitytemplate.model.CarRent;
import com.javagda25.securitytemplate.model.CarReturn;
import com.javagda25.securitytemplate.service.CarRentService;
import com.javagda25.securitytemplate.service.CarReturnService;
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
import java.time.LocalDate;

@Controller
@RequestMapping("/carReturn")
public class CarReturnController {

    @Autowired
    private CarReturnService carReturnService;

    @Autowired
    private CarRentService carRentService;

    @GetMapping("/add_carReturn")
    public String addCarReturn(Model model,
                               HttpServletRequest request,
                               @RequestParam(name = "idCarRent") Long idCarRent) {
        CarRent carRent = carRentService.getCarRentById(idCarRent);
        Booking booking = carRent.getBooking();
        Car car = booking.getCar();
        int additionalPaymentForDelay = carReturnService.getDelayPayment(idCarRent);
        int cleaningPayment = car.getFeeForCleaning();
        int fuelPayment = car.getFeeForFuel();
        String comment = "ok";
        LocalDate dateEnd = carReturnService.getDateEnd(idCarRent);
        model.addAttribute("idCarRent", carRent.getIdCarRent());
        model.addAttribute("additionalPaymentForDelay", additionalPaymentForDelay);
        model.addAttribute("additionalPaymentForFuel", fuelPayment);
        model.addAttribute("cleaningPayment", cleaningPayment);
        model.addAttribute("commentCarReturn", comment);
        model.addAttribute("dateEnd", dateEnd);
        model.addAttribute("dateOfReturn", LocalDate.now());
        model.addAttribute("referer", request.getHeader("referer"));
        return "carReturn-add";
    }

//    @PostMapping("/add_carReturn")
////    public String postAddCarReturn(CarReturn carReturn, Long idCarRent, int delayPayment, int cleaningPayment) {
//    public String postAddCarReturn(CarReturn carReturn, Long idCarRent, int cleaningPayment) {
//        CarRent carRent = carRentService.getCarRentById(idCarRent);
//        carReturn.setAdditionalPaymentForCleaning(cleaningPayment);
////        carReturn.setAdditionalPaymentForDelay(delayPayment);
//        carReturnService.save(carReturn);
//        carRent.setCarReturned(true);
//        carRentService.saveCarRent(carRent);
//        return "redirect:/carReturn/list_carReturns";
//    }

    @PostMapping("/add_carReturn")
    public String postAddCarReturn(CarReturn carReturn, Long idCarRent) {
//        carReturn.setAdditionalPaymentForFuel(additionalPaymentForFuel);
//        carReturn.setAdditionalPaymentForCleaning(cleaningPayment);
        carReturnService.save(carReturn);
        CarRent carRent = carRentService.getCarRentById(idCarRent);
        carRent.setCarReturn(carReturn);
        carRent.setCarReturned(true);
        carRentService.saveCarRent(carRent);
        return "redirect:/carReturn/list_carReturns";
    }


    @GetMapping("/list_carReturns")
    public String getCarReturnsList(Model model,
                                    @RequestParam(name = "page", defaultValue = "0") int page,
                                    @RequestParam(name = "size", defaultValue = "2") int size) {
        Page<CarReturn> carReturnPage = carReturnService.getPageCarReturnss(PageRequest.of(page, size));
        model.addAttribute("carReturns", carReturnPage);
        return "carReturn-list";
    }
}
