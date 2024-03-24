package com.example.delivery.service;

import org.springframework.stereotype.Service;

@Service
public interface DeliveryFeeService {
    double getDeliveryFee(String city, String vehicleType);
}
