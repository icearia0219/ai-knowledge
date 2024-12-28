package com.aizs.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.aizs.dto.UserDTO;
import com.aizs.entity.User;
import com.aizs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setAge(userDTO.getAge());
        user.setProvince(userDTO.getProvince());
        user.setCity(userDTO.getCity());
        user.setBloodtype(userDTO.getBloodtype());
        user.setGender(userDTO.getGender());
        User registeredUser = userService.register(user);
        return registeredUser != null ? "Registration successful" : "Registration failed";
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        // 调用用户登录服务，检查用户名和密码
        User user = userService.login(userDTO.getUsername(), userDTO.getPassword());

        // 如果登录成功
        if (user != null) {
            // 通过 Sa-Token 登录
            StpUtil.login(user.getUserid().toString());

            // 设置角色信息到会话
            StpUtil.getSession().set("role", user.getRole());

            // 打印会话中的角色信息以便调试
            System.out.println("Role set in session: " + StpUtil.getSession().get("role"));

            // 获取登录 Token
            String token = StpUtil.getTokenValue();

            // 返回登录成功的消息和 Token
            return "Login successful, token: " + token;
        } else {
            // 如果用户名或密码错误
            return "Invalid username or password";
        }
    }


    @GetMapping("/user/info")
    public ResponseEntity<User> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 打印从会话中获取的角色信息用于调试
        String roleInSession = (String) StpUtil.getSession().get("role");
        System.out.println("Role from session: " + roleInSession);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/user/update")
    public String updateUser(@RequestBody UserDTO userDTO) {
        Long userId = StpUtil.getLoginIdAsLong();
        if (userId == null) {
            return "Unauthorized";
        }
        User user = userService.getUserById(userId);
        if (user == null) {
            return "User not found";
        }
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setGender(userDTO.getGender());
        user.setAge(userDTO.getAge());
        user.setProvince(userDTO.getProvince());
        user.setCity(userDTO.getCity());
        user.setBloodtype(userDTO.getBloodtype());
        userService.updateUser(user);
        return "Update successful";
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<String> handleOptionsRequest() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}