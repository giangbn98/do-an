package com.hubt.data.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@Data
public class Brand extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
