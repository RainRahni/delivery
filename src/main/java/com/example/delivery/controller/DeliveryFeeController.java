package com.example.delivery.controller;

import com.example.delivery.service.DeliveryFreeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("deliveryFee")
@RequiredArgsConstructor
public class DeliveryFeeController {
    private final DeliveryFreeServiceImpl deliveryFreeService;

    /**
     * Calculate total delivery fee for specific vehicle in specific
     * city, delivery is the sum of regular base fee and extra fee
     * based on weather conditions.
     * @param city given city.
     * @param vehicle given vehicle.
     * @return total delivery fee.
     */
    @GetMapping("/totalFee/{city}/{vehicleType}")
    public BigDecimal calculateTotalDeliveryFee(@PathVariable("city") String city,
                                                @PathVariable("vehicleType") String vehicle) {
        return deliveryFreeService.getDeliveryFee(city, vehicle);
    }

}
