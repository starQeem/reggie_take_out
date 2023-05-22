package com.starqeem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starqeem.reggie.mapper.DishFlavorMapper;
import com.starqeem.reggie.pojo.DishFlavor;
import com.starqeem.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
