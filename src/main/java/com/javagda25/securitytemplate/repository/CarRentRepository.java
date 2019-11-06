package com.javagda25.securitytemplate.repository;

import com.javagda25.securitytemplate.model.CarRent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRentRepository extends JpaRepository<CarRent, Long> {

    @Query(nativeQuery = true,
    value = "SELECT b.hires_days*b.price+ret.additional_payment_for_cleaning+ret.additional_payment_for_delay+ret.additional_payment_for_fuel FROM \n" +
            "        car_rent cr join \n" +
            "                (select hires_days,price,id_booking from booking x join car c on x.car_id_car= c.id_car) b \n" +
            "                on cr.booking_id_booking = b.id_booking\n" +
            "        join car_return ret on cr.car_return_id_car_return = ret.id_car_return\n" +
            "    where cr.id_car_rent= ?1")
    Integer calculateRevenue(Long carRentId);

    Page<CarRent> findAllByCarReturned(boolean carReturned, Pageable pageable);
}
