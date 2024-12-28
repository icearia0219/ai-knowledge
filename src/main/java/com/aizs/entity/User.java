package com.aizs.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class User {
    @Id
    private Long userid;
    private String username;
    private String password;
    private String role;
    private String gender;
    private Integer age;
    private String province;
    private String city;
    private String bloodtype;

    // Getters and Setters


}