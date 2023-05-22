package com.starqeem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starqeem.reggie.mapper.OrderDetailMapper;
import com.starqeem.reggie.pojo.OrderDetail;
import com.starqeem.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
