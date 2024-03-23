package com.example.delivery.controller;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.service.DeliveryFreeServiceImpl;
import com.example.delivery.service.WeatherDataService;
import com.example.delivery.service.WeatherDataServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("deliveryFee")
@RequiredArgsConstructor
public class DeliveryFeeController {
    private final DeliveryFreeServiceImpl deliveryFreeService;
    @GetMapping("/totalFee/{city}/{vehicleType}")
    public double calculateTotalDeliveryFee(@PathVariable("city") String city,
                                            @PathVariable("vehicleType") String vehicleType) {
        return deliveryFreeService.getDeliveryFee(city, vehicleType);
    }

}
