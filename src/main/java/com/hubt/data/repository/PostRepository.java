package com.hubt.data.repository;

import com.hubt.data.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("select o from Post o where " +
            "( :brand is null or lower(o.brand) = :brand ) " +
            "and ( o.name like concat('%', :nameCar, '%') ) " +
            "and ( :carType is null or o.carType = :carType ) " +
            "and ( :fromDate is null or o.yearOfManufacture >= :fromDate ) " +
            "and ( :toDate is null or o.yearOfManufacture <= :toDate) " +
            "and ( :minPrice is null or o.price >= :minPrice ) " +
            "and ( :maxPrice is null  or o.price <= :maxPrice ) " +
            "and ( :fuelType is null or o.fuelType = :fuelType) " +
            "and ( :vehicleType is null or o.vehicleType = :vehicleType) " +
            "and ( :originType is null or o.originType = :originType ) " +
            "and ( lower(o.colorOutside) like concat('%', :color, '%') ) " +
            "and ( :minSeat is null or o.numberSeat >= :minSeat ) " +
            "and ( :maxSeat is null or o.numberSeat <= :maxSeat )" +
            "and ( :wheelType is null or o.wheelType = :wheelType ) " +
            "and ( :gearType is null or o.gearType = :gearType )" +
            "and o.isAccept = true ")
    Page<Post> filter(@Param("brand") String brand,
                      @Param("nameCar") String nameCar,
                      @Param("carType")CarType carType,
                      @Param("fromDate") Integer fromDate,
                      @Param("toDate") Integer toDate,
                      @Param("minPrice") Long minPrice,
                      @Param("maxPrice") Long maxPrice,
                      @Param("fuelType")FuelType fuelType,
                      @Param("vehicleType")VehicleType vehicleType,
                      @Param("originType")OriginType originType,
                      @Param("color") String color,
                      @Param("minSeat")Integer minSeat,
                      @Param("maxSeat")Integer maxSeat,
                      @Param("wheelType")WheelType wheelType,
                      @Param("gearType")GearType gearType,
                      Pageable pageble);

    @Query("select o from Post o where lower(o.brand) like concat('%', lower(:brand), '%') and o.isAccept = true ")
    Page<Post> findByBrand(@Param("brand") String key, Pageable pageable);

    @Modifying
    @Query("update Post set isAccept = true where id = :postId")
    void updateAcceptPost(@Param("postId") long postId);

    @Query("select p from Post p where p.user.id = :userId and ( :accept is null or p.isAccept = :accept )")
    Page<Post> findByUserId(@Param("userId") long userId,
                            @Param("accept") Boolean accept,
                            Pageable pageable);

    @Query("select p from Post p where ( :accept is null or p.isAccept = :accept )")
    Page<Post> findByAccept(@Param("accept") Boolean accept, Pageable pageable);
}
