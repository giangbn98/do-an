package com.hubt.service;

import com.hubt.data.model.FuelType;
import com.hubt.data.model.VehicleType;
import com.hubt.data.model.WheelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class PostService {

    public VehicleType convertVehicleType(String vehicle){
        VehicleType vehicleType= null;
        switch (vehicle){
            case "SUV":
                vehicleType = VehicleType.SUV;
                break;
            case "SEDAN":
                vehicleType = VehicleType.SEDAN;
                break;
            case "COUPE":
                vehicleType = VehicleType.COUPE;
                break;
            case "CROSSOVER":
                vehicleType = VehicleType.CROSSOVER;
                break;
            case "HATCHBACK":
                vehicleType = VehicleType.HATCHBACK;
                break;
            case "CONVERTIBLE":
                vehicleType = VehicleType.CONVERTIBLE;
                break;
            case "TRUCK":
                vehicleType = VehicleType.TRUCK;
                break;
            case "VAN":
                vehicleType = VehicleType.VAN;
                break;
            case "WAGON":
                vehicleType = VehicleType.WAGON;
                break;
        }
        return vehicleType;
    }

    public WheelType convertWheelType(String wheel){
        WheelType wheelType = null;
        switch (wheel){
            case "FWD":
                wheelType = WheelType.FWD;
                break;
            case "RWD":
                wheelType = WheelType.RWD;
                break;
            case "4WD":
                wheelType = WheelType.FOURWD;
                break;
            case "AWD":
                wheelType = WheelType.AWD;
                break;
        }
        return wheelType;
    }

    public FuelType convertFuel(String fuel){
        FuelType fuelType = null;
        switch (fuel){
            case "GASOLINE":
                fuelType = FuelType.GASOLINE;
                break;
            case "DIESEL":
                fuelType = FuelType.DIESEL;
                break;
            case "HYBRID":
                fuelType = FuelType.HYBRID;
                break;
            case "ELECTRIC":
                fuelType = FuelType.ELECTRIC;
                break;
        }
        return fuelType;
    }

    public List<Integer> convertNumberSeat(String seat){
        int minSeat = 0;
        int maxSeat = 0;
        List<Integer> list = new ArrayList<>();
        switch (seat){
            case "1TO3":
                minSeat = 1;
                maxSeat = 3;
                break;
            case "4TO6":
                minSeat = 4;
                maxSeat = 6;
                break;
            case "7TO9":
                minSeat = 7;
                maxSeat = 9;
                break;
            case "9TO16":
                minSeat = 9;
                maxSeat = 16;
                break;
        }
        list.add(0,minSeat);
        list.add(1,maxSeat);
        return list;
    }
}
