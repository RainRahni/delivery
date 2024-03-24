package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryFreeServiceImpl implements DeliveryFeeService {
    private final WeatherDataServiceImpl weatherDataService;

    /**
     * Calculate total delivery fee by adding
     * regional base fee and extra fee on business rules.
     * @param city which city.
     * @param vehicleType which type is the vehicle.
     * @return total delivery fee.
     */
    public double getDeliveryFee(String city, String vehicleType) {
        validateParameters(city, vehicleType);
        double regionalBaseFee = calculateRegionalBaseFee(city, vehicleType);
        double extraFee = calculateExtraFee(city, vehicleType);
        return regionalBaseFee + extraFee;
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
    private double calculateRegionalBaseFee(String city, String vehicleType) {
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
    private double calculateExtraFee(String city, String vehicleType) {
        Weather weather = weatherDataService.getLatestWeatherReport(city);
        double atef = 0;
        double wsef = 0;
        double wpef = 0;
        if (vehicleType.equalsIgnoreCase("Scooter") || vehicleType.equalsIgnoreCase("Bike")) {
            atef = getAtef(weather.getAirTemperature());
            wpef = getWpef(weather.getPhenomenon());
        }
        if (vehicleType.equalsIgnoreCase("Bike")) {
            wsef = getWsef(weather.getWindSpeed());
        }
        return atef + wsef + wpef;
    }
    private double getAtef(double airTemp) {
        return  airTemp > -10 && airTemp < 0 ? 0.5 : airTemp < -10 ? 1 : 0;
    }
    private double getWsef(double windSpeed) {
        if (windSpeed >= 10  && windSpeed <= 20) {
            return 0.5;
        } else if (windSpeed > 20) {
            throw new BadRequestException("Usage of selected vehicle type is forbidden");
        }
        return 0;
    }
    private double getWpef(String phenomenon) {
        if (phenomenon == null) {
            return 0;
        }
        if (phenomenon.toLowerCase().contains("snow") || phenomenon.toLowerCase().contains("sleet")) {
           return 1;
       } else if (phenomenon.toLowerCase().contains("rain")) {
           return 0.5;
       } else if (phenomenon.equalsIgnoreCase("Glaze")
               || phenomenon.equalsIgnoreCase("Hail")
               || phenomenon.equalsIgnoreCase("Thunder")) {
           throw new BadRequestException("Usage of selected vehicle type is forbidden");
       }
       return 0;
    }
}
