package com.api.apibackend.service.impl;

import com.api.apibackend.mapper.UserMapper;
import com.api.apibackend.pojo.User;
import com.api.apibackend.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
