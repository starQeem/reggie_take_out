package com.starqeem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starqeem.reggie.mapper.ShoppingCartMapper;
import com.starqeem.reggie.pojo.ShoppingCart;
import com.starqeem.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
