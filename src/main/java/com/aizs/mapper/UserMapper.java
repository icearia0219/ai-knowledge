package com.aizs.mapper;

import com.aizs.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    int insertUser(User user);
    List<User> getAllUsers();
    User getUserById(Long userid);
    void updateUser(User user);
    void deleteUser(Long userid);
}