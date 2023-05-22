package com.starqeem.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starqeem.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
