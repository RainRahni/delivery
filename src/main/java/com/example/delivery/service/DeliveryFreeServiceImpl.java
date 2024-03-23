package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class DeliveryFreeServiceImpl {
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

    /**
     * Validate that given parameters are correct.
     * @param city which city.
     * @param vehicleType which type is the vehicle.
     */
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

    /**
     * Calculate regional base fee based on what is the
     * given city and which vehicle type is given.
     * @param city which city.
     * @param vehicleType which type is the vehicle.
     * @return regional base fee.
     */
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

    /**
     * Calculate extra fee based on latest weather report
     * in given city and vehicle type.
     * @param city which city.
     * @param vehicleType which type is the vehicle
     * @return extra fee.
     */
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

    /**
     *
     * @param airTemp
     * @return
     */
    private double getAtef(double airTemp) {
        return  airTemp >= -10 && airTemp <= 0 ? 0.5 : airTemp < -10 ? 1 : 0;
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
       if (phenomenon.contains("snow") || phenomenon.contains("sleet")) {
           return 1;
       } else if (phenomenon.contains("rain")) {
           return 0.5;
       } else if (phenomenon.equalsIgnoreCase("glaze")
               || phenomenon.equalsIgnoreCase("Hail")
               || phenomenon.equalsIgnoreCase("Thunder")) {
           throw new BadRequestException("Usage of selected vehicle type is forbidden");
       }
       return 0;
    }
}
