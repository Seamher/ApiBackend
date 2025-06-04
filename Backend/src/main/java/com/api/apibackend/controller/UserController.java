package com.api.apibackend.controller;

import com.api.apibackend.pojo.User;
import com.api.apibackend.service.UserService;
import com.api.apibackend.utils.JWTUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Validated
@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/user")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        logger.info("register attempt for account: {}", username);

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        logger.info("Login attempt for account: {}", username);

        // 验证用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User userLogin = userService.getOne(queryWrapper);
        if (userLogin == null) {
            logger.warn("Login failed. User not found: {}", username);

            return ResponseEntity.status(404).body(""); // 用户不存在
        } else {
            // 验证密码是否正确
            if (Objects.equals(password, user.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("account", username);
                logger.info("User logged in successfully: {}", username);
                var token = JWTUtils.genToken(claims);
                logger.warn("User token: {}", token);

                return ResponseEntity.ok(token);

            } else {
                logger.warn("Login failed. Incorrect password for account: {}", username);

                return ResponseEntity.status(422).body(""); // 密码错误
            }
        }
    }



}
