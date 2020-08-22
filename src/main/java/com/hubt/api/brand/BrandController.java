package com.hubt.api.brand;

import com.hubt.data.Response;
import com.hubt.data.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/brand")
@RestController
public class BrandController {

    @Autowired
    private BrandRepository brandRepository;

    @GetMapping
    public ResponseEntity getAll(){
        return Response.data(brandRepository.findAll());
    }

}
