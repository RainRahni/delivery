package com.example.delivery.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface DeliveryFeeService {
    BigDecimal getDeliveryFee(String city, String vehicleType);
}
