package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Weather;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryFreeServiceImpl implements DeliveryFeeService {
    private final WeatherDataServiceImpl weatherDataService;
    private static final List<String> VALID_CITIES = List.of(
            City.TALLINN.name().toLowerCase(),
            City.TARTU.name().toLowerCase(),
            City.PÄRNU.name().toLowerCase());
    private static final List<String> VALID_VEHICLES = List.of(
            Vehicle.CAR.name().toLowerCase(),
            Vehicle.SCOOTER.name().toLowerCase(),
            Vehicle.BIKE.name().toLowerCase()
    );
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
        if (!VALID_CITIES.contains(city.toLowerCase())
                || !VALID_VEHICLES.contains(vehicleType.toLowerCase())) {
            throw new BadRequestException("Wrong input!");
        }
    }
    private double calculateRegionalBaseFee(String city, String vehicleType) {

        String lowerCity = city.toLowerCase();
        if (lowerCity.equals("tallinn")) {
            return vehicleType.equalsIgnoreCase("Car") ? 4 :
                    vehicleType.equalsIgnoreCase("Scooter") ? 3.5 : 3;
        } else if (lowerCity.equals("tartu")) {
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
