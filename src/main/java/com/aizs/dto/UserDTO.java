package com.aizs.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
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