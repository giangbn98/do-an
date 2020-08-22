package com.hubt.api.post;

import com.hubt.api.post.dto.FilterRequest;
import com.hubt.api.post.dto.PostRequest;
import com.hubt.data.GbException;
import com.hubt.data.Response;
import com.hubt.data.model.*;
import com.hubt.data.repository.*;
import com.hubt.service.PostService;
import com.hubt.service.PushService;
import com.hubt.utils.DateUtils;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PushService pushService;

    @PostMapping("/create")
    public ResponseEntity createPost(@RequestBody PostRequest postRequest){

        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new GbException("User does not exits"));

        Post post = postRepository.findById(postRequest.getId())
                .orElse(new Post());

        post.setBrand(postRequest.getBrand());
        post.setName(postRequest.getName().toUpperCase());
        post.setVersion(postRequest.getVersion().toUpperCase());
        if (postRequest.getCarType().equals("NEW")) post.setCarType(CarType.NEW);
        else post.setCarType(CarType.OLD);
        if (postRequest.getOriginType().equals("IMPORT")) post.setOriginType(OriginType.IMPORT);
        else post.setOriginType(OriginType.ASSEMBLY);
        String year = DateUtils.convertDate(postRequest.getYearOfManufacture(),"YYYY");
        post.setYearOfManufacture(Integer.parseInt(year));
        post.setNumberOfKmMoved(postRequest.getNumberOfKmMoved());
        post.setVehicleType(postService.convertVehicleType(postRequest.getVehicleType()));
        post.setPrice(postRequest.getPrice());
        post.setColorInside(postRequest.getColorInside());
        post.setColorOutside(postRequest.getColorOutside());
        post.setNumberDoor(postRequest.getNumberDoor());
        post.setNumberSeat(postRequest.getNumberSeat());
        if (postRequest.getGearType().equals("AT")) post.setGearType(GearType.AT);
        else post.setGearType(GearType.MT);
        post.setWheelType(postService.convertWheelType(postRequest.getWheelType()));
        post.setFuelType(postService.convertFuel(postRequest.getFuelType()));
        post.setFuelConsumption(postRequest.getFuelConsumption());
        post.setCapacity(postRequest.getCapacity());
        post.setDescription(postRequest.getDescription());
        post.setUser(user);
        post = postRepository.save(post);

        for (Image image : postRequest.getImages()){
            image.setPost(post);
            imageRepository.save(image);
        }

        Contact contact = new Contact();
        contact.setAddress(postRequest.getContact().getAddress());
        contact.setPhoneNumber(postRequest.getContact().getPhoneNumber());
        contact.setAnotherPhoneNumber(postRequest.getContact().getAnotherPhoneNumber());
        contact.setFullName(postRequest.getContact().getFullName());
        contact.setAddress(postRequest.getContact().getAddress());
        contact.setProvince(postRequest.getContact().getProvince());
        contact.setPost(post);
        contact = contactRepository.save(contact);

        return Response.data(post);
    }
    @GetMapping
    public ResponseEntity getAllPost(@RequestParam String brand,
                                     @RequestParam Integer page,
                                     @RequestParam Integer pageSize){
        if (StringUtils.isEmpty(brand)) brand = "";
        Page<Post> posts = postRepository.findByBrand(brand, PageRequest.of(page - 1, pageSize));
        return Response.data(posts);
    }

    @GetMapping("/findById")
    public ResponseEntity findPostById(@RequestParam long postId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new GbException("post does not exits"));
        return Response.data(post);
    }

    @PostMapping("/filter")
    public ResponseEntity filter(@RequestBody FilterRequest request,
                                 @RequestParam Integer page,
                                 @RequestParam Integer pageSize){
        String brand = "";
        if (StringUtils.isEmpty(request.getBrand()) || request.getBrand().equals("ALL")) brand = null;
        else brand = request.getBrand().toLowerCase();

        String name = "";
        if (StringUtils.isEmpty(request.getName())) name = "";
        else name = request.getName().toUpperCase();

        CarType carType = null;
        if (!StringUtils.isEmpty(request.getCarType()) && !request.getCarType().equals("ALL")){
            if (request.getCarType().equals("NEW")) carType = CarType.NEW;
            else carType = CarType.OLD;
        }
        Integer fromDate = null;
        Integer toDate = null;
        if (request.getFromDate() != null)
            fromDate = Integer.parseInt(DateUtils.convertDate(request.getFromDate(),"YYYY"));
        if (request.getToDate() != null)
            toDate = Integer.parseInt(DateUtils.convertDate(request.getToDate(),"YYYY"));

        Long minPrice = null;
        Long maxPrice = null;
//        if (request.getLimits()[0] == 0 && request.getLimits()[1] == 0){
//            minPrice = null;
//            maxPrice = null;
//        }else {
//            minPrice = Long.valueOf(request.getLimits()[0]);
//            maxPrice = Long.valueOf(request.getLimits()[1]);
//        }
        if (request.getMinPrice() == 0) minPrice = null;
        else minPrice = request.getMinPrice();
        if (request.getMaxPrice() == 0) maxPrice = null;
        else maxPrice = request.getMaxPrice();

        FuelType fuelType = null;
        if (!StringUtils.isEmpty(request.getFuelType())) fuelType = postService.convertFuel(request.getFuelType());

        VehicleType vehicleType = null;
        if (!StringUtils.isEmpty(request.getVehicleType())) vehicleType = postService.convertVehicleType(request.getVehicleType());

        OriginType originType = null;
        if (!StringUtils.isEmpty(request.getOrigin()) && !request.getOrigin().equals("ALL")){
            if (request.getOrigin().equals("IMPORT")) originType = OriginType.IMPORT;
            else originType = OriginType.ASSEMBLY;
        }

        Integer minSeat = null;
        Integer maxSeat = null;
        if (!StringUtils.isEmpty(request.getNumberSeat()) && !request.getNumberSeat().equals("ALL")){
            List<Integer> list = postService.convertNumberSeat(request.getNumberSeat());
            minSeat = list.get(0);
            maxSeat = list.get(1);
        }

        WheelType wheelType = null;
        if (!StringUtils.isEmpty(request.getWheelType()) && !request.getWheelType().equals("ALL"))
            wheelType = postService.convertWheelType(request.getWheelType());

        GearType gearType = null;
        if (!StringUtils.isEmpty(request.getGearType()) && !request.getGearType().equals("ALL")) {
            if (request.getGearType().equals("AT")) gearType = GearType.AT;
            else gearType = GearType.MT;
        }

        String color = "";
        if (!StringUtils.isEmpty(request.getColor()) && !request.getColor().equals("ALL")){
            color = request.getColor().toLowerCase();
        }

        Page<Post> posts = postRepository
                .filter(brand,name, carType, fromDate, toDate, minPrice,maxPrice, fuelType,
                        vehicleType, originType,color, minSeat, maxSeat, wheelType, gearType,
                        PageRequest.of(page - 1, pageSize, Sort.by("createAt")));
        return Response.data(posts);
    }

    @GetMapping("/findAll")
    public ResponseEntity findAll(@RequestParam String status,
                                  @RequestParam Integer page,
                                  @RequestParam Integer pageSize){
        Boolean accept = null;
        if (status.equals("accepted")) accept = true;
        if (status.equals("nonaccept")) accept = false;
        Page<Post> posts = postRepository.findByAccept(accept,PageRequest.of(page - 1, pageSize, Sort.by("createAt").descending()));
        return Response.data(posts);
    }

    @PostMapping("/acceptPost")
    public ResponseEntity acceptPost(@RequestParam long postId,
                                     @RequestParam long userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GbException("post does not exits"));
        User user = userRepository.findById(userId).orElseThrow(() -> new GbException("username does not exits"));

        post.setAccept(true);
        post.setAcceptTime(DateUtils.convertDate(new Date(),"dd-MM-yyyy hh:mm:ss"));
        post.setUserAccept(user.getUsername());
        if (post.isConfirm()) post.setConfirm(false);
        post = postRepository.save(post);
        Notification notification = new Notification();
        notification.setContent("Admin đã duyệt bài viết của bạn.Click để xem chi tiết !");
        notification.setCreatedAt(DateUtils.convertDate(new Date(),"dd-MM-yyyy hh:mm:ss"));
        notification.setUser(post.getUser());
        notification.setPostId(post.getId());
        notification = notificationRepository.save(notification);
        pushService.pushNotification(post.getUser().getId(),notification);
        return Response.data(post);
    }

    @PostMapping("/declinePost")
    public ResponseEntity declinePost(@RequestParam long postId,
                                      @RequestParam long userId,
                                      @RequestParam String reason){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GbException("post does not exits"));
        User user = userRepository.findById(userId).orElseThrow(() -> new GbException("username does not exits"));
        post.setConfirm(true);
        postRepository.save(post);

        Notification notification = new Notification();
        notification.setPostId(post.getId());
        notification.setUser(post.getUser());
        notification.setContent("Bài viết của bạn không được duyệt. Click để xem lý do !");
        notification.setReason(reason);
        notification.setCreatedAt(DateUtils.convertDate(new Date(),"dd-MM-yyyy hh:mm:ss"));
        notification = notificationRepository.save(notification);
        pushService.pushNotification(post.getUser().getId(), notification);
        return Response.data(post);
    }

    @GetMapping("/findByUserId")
    public ResponseEntity findByUserId(@RequestParam long userId,
                                       @RequestParam String status,
                                       @RequestParam Integer page,
                                       @RequestParam Integer pageSize){
        Boolean accept = null;
        if (status.equals("accepted")) accept = true;
        if (status.equals("nonaccept")) accept = false;
        return Response.data(postRepository.findByUserId(userId, accept, PageRequest.of(page - 1, pageSize)));
    }

    @PostMapping("/removePost")
    public ResponseEntity removePost(@RequestParam long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new GbException("Post does not exits"));
        for (Image image : post.getImages()){
            image.setPost(null);
            imageRepository.save(image);
        }
        postRepository.delete(post);
        return Response.data("success");
    }

    @PostMapping("/update")
    public ResponseEntity updatePost(@RequestBody PostRequest postRequest){

        User user = userRepository.findById(postRequest.getUserId())
                .orElseThrow(() -> new GbException("User does not exits"));

        Post post = postRepository.findById(postRequest.getId())
                .orElseThrow(() -> new GbException("Post does nit exits"));

        post.setBrand(postRequest.getBrand());
        post.setName(postRequest.getName());
        post.setVersion(post.getVersion());
        if (postRequest.getCarType().equals("NEW")) post.setCarType(CarType.NEW);
        else post.setCarType(CarType.OLD);
        if (postRequest.getOriginType().equals("IMPORT")) post.setOriginType(OriginType.IMPORT);
        else post.setOriginType(OriginType.ASSEMBLY);
        String year = DateUtils.convertDate(postRequest.getYearOfManufacture(),"YYYY");
        post.setYearOfManufacture(Integer.parseInt(year));
        post.setNumberOfKmMoved(postRequest.getNumberOfKmMoved());
        post.setVehicleType(postService.convertVehicleType(postRequest.getVehicleType()));
        post.setPrice(postRequest.getPrice());
        post.setColorInside(postRequest.getColorInside());
        post.setColorOutside(postRequest.getColorOutside());
        post.setNumberDoor(postRequest.getNumberDoor());
        post.setNumberSeat(postRequest.getNumberSeat());
        if (postRequest.getGearType().equals("AT")) post.setGearType(GearType.AT);
        else post.setGearType(GearType.MT);
        post.setWheelType(postService.convertWheelType(postRequest.getWheelType()));
        post.setFuelType(postService.convertFuel(postRequest.getFuelType()));
        post.setFuelConsumption(postRequest.getFuelConsumption());
        post.setCapacity(postRequest.getCapacity());
        post.setDescription(postRequest.getDescription());
//        post.setUser(user);
        if (!postRequest.getImages().isEmpty()){
            post.getImages().clear();
        }
        post = postRepository.save(post);

        List<Image> images = imageRepository.findByPostId(post.getId());
        if (images.size() > 0){
            for (Image image : images){
                image.setPost(null);
                imageRepository.save(image);
            }
        }

        for (Image image : postRequest.getImages()) {
            image.setPost(post);
            imageRepository.save(image);
        }



        Contact contact = contactRepository.findById(postRequest.getContact().getId())
                .orElseThrow(() -> new GbException("Contact does not exits"));
        contact.setAddress(postRequest.getContact().getAddress());
        contact.setPhoneNumber(postRequest.getContact().getPhoneNumber());
        contact.setAnotherPhoneNumber(postRequest.getContact().getAnotherPhoneNumber());
        contact.setFullName(postRequest.getContact().getFullName());
        contact.setAddress(postRequest.getContact().getAddress());
        contact.setProvince(postRequest.getContact().getProvince());
        contact.setPost(post);
        contact = contactRepository.save(contact);

        return Response.data(post);
    }


}
