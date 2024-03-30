package com.example.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Weather {
    @Column(name = "air_temp")
    private BigDecimal airTemperature;
    @Column(name = "wind_speed")
    private BigDecimal windSpeed;
    @Column(name = "weather_phenomenon")
    private String phenomenon;
    @Column(name = "timestamp")
    private Date date;
}
