package com.aizs.service;

import com.aizs.entity.User;

import java.util.List;

public interface UserService {
    User register(User user);
    User login(String username, String password);
    List<User> getAllUsers();
    User getUserById(Long userid);
    void updateUser(User user);
    void deleteUser(Long userid);
}