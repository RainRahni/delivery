package com.example.delivery.model;

import com.example.delivery.model.type.City;
import com.example.delivery.model.type.FeeType;
import com.example.delivery.model.type.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rules")
public class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private City city;
    @Enumerated(EnumType.STRING)
    private Vehicle vehicle;
    @Enumerated(EnumType.STRING)
    private FeeType feeType;
    private String phenomenon;
    private BigDecimal upperBound;
    private BigDecimal lowerBound;
    private BigDecimal fee;
}
