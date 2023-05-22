package com.starqeem.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starqeem.reggie.common.CustomException;
import com.starqeem.reggie.dto.SetmealDto;
import com.starqeem.reggie.mapper.SetmealMapper;
import com.starqeem.reggie.pojo.Setmeal;
import com.starqeem.reggie.pojo.SetmealDish;
import com.starqeem.reggie.service.SetmealDishService;
import com.starqeem.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /*
     * 新增套餐,同时需要保存套餐和菜品的关联关系
     * */
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息,操作setmeal,执行insert操作
        this.save(setmealDto); //save方法是保存

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息,操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes); //saveBatch方法是批量保存
    }
    /*
     * 删除套餐,同时删除套餐与菜品的关联信息
     */
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //查询套餐状态,确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if (count > 0){
            //如果不能删除,抛出一个业务异常
            throw new CustomException("套餐正在售卖中,无法删除!");
        }
        //如果可以删除,先删除套餐表中的数据----setmeal
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据
        setmealDishService.remove(lambdaQueryWrapper);
    }
    /*
    * 修改菜品(回显)
    * */
    @Override
    public SetmealDto getByIdWithDish(Long id) {
        //根据id查询setmeal表中的基本信息
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        //对象拷贝
        BeanUtils.copyProperties(setmeal,setmealDto);
        //查询关联表setmeal_dish的菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        List<SetmealDish> setmealDishList = setmealDishService.list(queryWrapper);
        //设置套餐菜品属性
        setmealDto.setSetmealDishes(setmealDishList);

        return setmealDto;
    }
    /*
    * 修改菜品(提交)
    * */
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //保存setmeal表中的基本数据
        this.updateById(setmealDto);
        //先删除原来的套餐所对应的菜品数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());

        setmealDishService.remove(queryWrapper);
        //更新套餐关联菜品信息。setmeal_dish表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            //设置setmeal_id字段
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //重新保存套餐对应的菜品数据
        setmealDishService.saveBatch(setmealDishes);
    }

}
