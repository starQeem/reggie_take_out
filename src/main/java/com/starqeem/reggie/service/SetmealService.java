package com.starqeem.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starqeem.reggie.dto.DishDto;
import com.starqeem.reggie.dto.SetmealDto;
import com.starqeem.reggie.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐,同时需要保存套餐和菜品的关联关系
    public void saveWithDish(SetmealDto setmealDto);
    //删除套餐,同时删除套餐和菜品的关联关系
    public void deleteWithDish(List<Long> ids);
    //修改套餐(回显)
    public SetmealDto getByIdWithDish(Long id);
    //修改套餐(提交)
    public void updateWithDish(SetmealDto setmealDto);
}
