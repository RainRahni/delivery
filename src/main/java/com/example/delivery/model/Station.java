package com.example.delivery.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station {
    @Column(name = "WMO_code")
    private String wmoCode;
    @Column(name = "station_name")
    private String name;
}
