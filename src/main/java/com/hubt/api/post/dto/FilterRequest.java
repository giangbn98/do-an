package com.hubt.api.post.dto;


import lombok.Data;

import java.util.Date;

@Data
public class FilterRequest {
    private String brand;

    private String name;

    private String carType;

    private String fuelType;

    private String origin;

    private String color;

    private String vehicleType;

    private String wheelType;

    private String gearType;

    private Date fromDate;

    private Date toDate;

    private long minPrice;

    private long maxPrice;

    private String numberSeat;

}
