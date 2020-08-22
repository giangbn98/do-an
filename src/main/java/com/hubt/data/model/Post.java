package com.hubt.data.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Post extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String name;

    private String version;

    private double capacity;

    private int yearOfManufacture;

    private OriginType originType;

    private CarType carType;

    private long numberOfKmMoved;

    private VehicleType vehicleType;

    private long price;

    private String colorInside;

    private String colorOutside;

    private int numberDoor;

    private int numberSeat;

    private GearType gearType;

    private WheelType wheelType;

    private FuelType fuelType;

    private long fuelConsumption;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "boolean default false")
    private boolean isAccept;

    @Column(columnDefinition = "boolean default false")
    private boolean isConfirm;

    private String acceptTime;

    private String userAccept;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Image> images;

    @OneToOne(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Contact contact;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
