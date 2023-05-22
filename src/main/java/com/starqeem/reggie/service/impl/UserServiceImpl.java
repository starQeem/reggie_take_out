package com.starqeem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starqeem.reggie.mapper.UserMapper;
import com.starqeem.reggie.pojo.User;
import com.starqeem.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
