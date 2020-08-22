package com.hubt.service;

import com.hubt.data.model.Brand;
import com.hubt.data.repository.BrandRepository;
import com.hubt.utils.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private BrandRepository brandRepository;

//    @PostConstruct
//    private void inti(){
//        List<Brand> list = new ArrayList<>();
//        for (String item : Common.list){
//            Brand brand = new Brand();
//            brand.setName(item);
//            list.add(brand);
//        }
//        brandRepository.saveAll(list);
//    }
}
