package com.example.delivery.controller;

import com.example.delivery.exception.BadRequestException;
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
    private final WeatherDataServiceImpl weatherDataServiceImpl;
    @GetMapping("/totalFee/{city}/{carType}")
    public double calculateTotalDeliveryFee(@PathVariable("city") String city, @PathVariable("carType") String vehicleType) {
        validateParameters(city, vehicleType);
        double regionalBaseFee = getRegionalBaseFee(city, vehicleType);

        return 32;
    }
    private void validateParameters(String city, String vehicleType) {
        if (!city.equalsIgnoreCase("Tallinn")
                && !city.equalsIgnoreCase("Tartu")
                && !city.equalsIgnoreCase("Pärnu")
                && !vehicleType.equalsIgnoreCase("Car")
                && !vehicleType.equalsIgnoreCase("Scooter")
                && !vehicleType.equalsIgnoreCase("Bike")) {
            throw new BadRequestException("Wrong input!");
        }
    }
    private double getRegionalBaseFee(String city, String vehicleType) {
        if (city.equalsIgnoreCase("Tallinn")) {
            return vehicleType.equalsIgnoreCase("Car") ? 4 :
                    vehicleType.equalsIgnoreCase("Scooter") ? 3.5 : 3;
        } else if (city.equalsIgnoreCase("Tartu")) {
            return vehicleType.equalsIgnoreCase("Car") ? 3.5 :
                    vehicleType.equalsIgnoreCase("Scooter") ? 3 : 2.5;
        }
        return vehicleType.equalsIgnoreCase("Car") ? 3 :
                vehicleType.equalsIgnoreCase("Scooter") ? 2.5 : 2;
    }
    private double getExtraFee(String vehicleType) {
        return 0;
    }
}
