package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryFreeServiceImpl {
    private final WeatherDataServiceImpl weatherDataService;
    public double getDeliveryFee(String city, String vehicleType) {
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
    private double getExtraFee(String city, String vehicleType) {
        return 0;
    }
}
