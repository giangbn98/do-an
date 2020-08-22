package com.hubt.api.post.dto;

import com.hubt.data.model.Contact;
import com.hubt.data.model.Image;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class PostRequest {

    private long id;

    private String brand;

    private String colorInside;

    private String colorOutside;

    private String description;

    private long fuelConsumption;

    private String fuelType;

    private String gearType;

    private String name;

    private int numberDoor;

    private long numberOfKmMoved;

    private int numberSeat;

    private String originType;

    private long price;

    private String carType;

    private String version;

    private String wheelType;

    private Date yearOfManufacture;

    private String vehicleType;

    private double capacity;

    private List<Image> images = new ArrayList<>();

    private Contact contact;

    private long userId;
}
