package com.hubt.api.image;

import com.hubt.data.GbException;
import com.hubt.data.Response;
import com.hubt.data.model.Image;
import com.hubt.data.model.User;
import com.hubt.data.repository.ImageRepository;
import com.hubt.data.repository.UserRepository;
import com.hubt.service.ImageService;
import com.hubt.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/api/image")
public class ImageController {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile[] list,
                                      @RequestParam("username") String username,
                                      HttpServletRequest request){
        User user = userRepository.findByUsername(username).orElseThrow(()-> new GbException("user does not exits"));

        for (MultipartFile multipartFile : list) {
            imageService.readImage(multipartFile);
            Image image = new Image();
            image.setUrl("http://"+request.getServerName()+":9090/images/"+multipartFile.getOriginalFilename());
            image.setCreatedDate(DateUtils.convertDate(new Date(),"dd-MM-yyyy hh:mm:ss"));
            image.setUserId(user.getId());
            imageRepository.save(image);
        }
        return Response.data("Upload Success");
    }

    @GetMapping
    public ResponseEntity getAllImage(@RequestParam String username){
        User user = userRepository.findByUsername(username).orElseThrow(()-> new GbException("user does not exits"));
        return Response.data(imageRepository.findByUserId(user.getId()));
    }

    @PostMapping("/remove")
    public ResponseEntity removeImage(@RequestParam long imageId){
        imageRepository.deleteById(imageId);
        return Response.data("ok");
    }
}
