package com.example.delivery.repository;

import com.example.delivery.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    @Query(value = "SELECT e FROM WeatherData e WHERE e.station.name LIKE :city% ORDER BY e.weather.date DESC")
    WeatherData findLatestByCity(String city);
}
