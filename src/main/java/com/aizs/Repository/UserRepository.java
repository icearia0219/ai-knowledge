package com.aizs.Repository;

import com.aizs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 根据用户名查找用户
    Optional<User> findByUsername(String username);

    // 根据用户ID查找用户
    Optional<User> findByUserid(Long userid);

    // 你也可以添加更多自定义查询方法
}
