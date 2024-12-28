package com.aizs.service;
import cn.dev33.satoken.stp.StpUtil;
import com.aizs.entity.User;
import com.aizs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 登录方法
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户
     */
    public User login(String username, String password) {
        // 根据用户名查找用户
        User user = userMapper.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 登录成功，使用 Sa-Token 设置用户登录
            StpUtil.login(user.getUserid());

            // 设置角色信息到会话中
            StpUtil.getSession().set("role", user.getRole());

            // 返回用户信息
            return user;
        }
        return null;  // 登录失败，返回 null
    }

    /**
     * 检查当前用户是否具有指定角色
     * @param expectedRole 期望的角色名
     */
    public void checkRole(String expectedRole) {
        // 获取当前登录用户的角色信息
        String role = (String) StpUtil.getSession().get("role");

        if (!expectedRole.equals(role)) {
            throw new RuntimeException("权限不足，无法执行操作");
        }
    }
}
