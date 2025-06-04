package com.api.apibackend.controller;

import com.api.apibackend.pojo.ApiResponse;
import com.api.apibackend.pojo.DTO.ListMyVideosDTO;
import com.api.apibackend.pojo.User;
import com.api.apibackend.service.UserService;
import com.api.apibackend.utils.JWTUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    public ResponseEntity<ApiResponse<Object>> register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        logger.info("register attempt for account: {}", username);

        // 验证用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User userRegister = userService.getOne(queryWrapper);
        if (userRegister != null) {
            logger.warn("Register failed. User already exit: {}", username);

            return ApiResponse.usernameUsedError();
        } else {
            User user = new User(null, username, password);
            userService.save(user);

            return ApiResponse.ok(200, null);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        logger.info("Login attempt for account: {}", username);

        // 验证用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User userLogin = userService.getOne(queryWrapper);
        if (userLogin == null) {
            logger.warn("Login failed. User not found: {}", username);

            return ApiResponse.userNotExistError(); // 用户不存在
        } else {
            // 验证密码是否正确
            if (Objects.equals(password, user.getPassword())) {
                Map<String, Object> claims = new HashMap<>();
                claims.put("account", username);
                logger.info("User logged in successfully: {}", username);
                var token = JWTUtils.genToken(claims);
                logger.warn("User token: {}", token);

                return ApiResponse.ok(200, token);

            } else {
                logger.warn("Login failed. Incorrect password for account: {}", username);

                return ApiResponse.wrongPasswordError(); // 密码错误
            }
        }
    }

    @DeleteMapping(value = "/user")
    public ResponseEntity<ApiResponse<Object>> logout(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        logger.info("logout attempt for account: {}", username);

        // 验证用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User userRegister = userService.getOne(queryWrapper);
        if (userRegister == null) {
            logger.warn("logout failed. User already exit: {}", username);

            return ApiResponse.userNotExistError();
        } else if (!Objects.equals(password, userRegister.getPassword())) {
            logger.warn("logout failed. Password wrong: {}", username);

            return ApiResponse.wrongPasswordError();
        } else {
            logger.warn("User logout successfully: {}", username);

            return ApiResponse.ok(204, null);
        }
    }

    @GetMapping(value = "/listMyVideos")
    public ResponseEntity<ApiResponse<Object>> listMyVideos(@RequestBody ListMyVideosDTO listMyVideosDTO) {
        Integer userId = listMyVideosDTO.getUserId();
        Integer limit = listMyVideosDTO.getLimit();
        Integer offset = listMyVideosDTO.getOffset();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        User userRegister = userService.getOne(queryWrapper);
        if (userRegister == null) {
            logger.warn("logout failed. User already exit: {}", userId);

            return ApiResponse.userNotExistError();
        } else {
            return ApiResponse.ok(200, null);
        }
    }

}
