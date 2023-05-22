package com.starqeem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starqeem.reggie.common.R;
import com.starqeem.reggie.dto.SetmealDto;
import com.starqeem.reggie.pojo.Category;
import com.starqeem.reggie.pojo.Setmeal;
import com.starqeem.reggie.service.CategoryService;
import com.starqeem.reggie.service.SetmealDishService;
import com.starqeem.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    /*
     * 新增套餐
     * */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) { //json格式数据加RequestBody
        log.info("套餐信息: {}" + setmealDto.toString());

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐信息成功!");
    }
    /*
    * 套餐管理分页查询
    * */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.eq(name != null,Setmeal::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行分页查询
        setmealService.page(pageInfo,queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }
    /*
    * 删除套餐
    * */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("id: {}",ids);

        setmealService.deleteWithDish(ids);

        return R.success("套餐数据删除成功!");
    }
    /*
    * 套餐停售启售状态修改
    * */
    @PostMapping("/status/{status}")
    public R<String> Status(@PathVariable int status,@RequestParam List<Long> ids){
        //将每个id new出来一个Dish对象，并设置状态
        List<Setmeal> collect = ids.stream().map((item) -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(item);
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());

        setmealService.updateBatchById(collect);
        return R.success("修改套餐状态成功!");
    }
    /*
    * 修改套餐(回显)
    * */
    @GetMapping("/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        log.info("id: {}",id);

        SetmealDto setmealDto = setmealService.getByIdWithDish(id);

        return R.success(setmealDto);
    }
    /*
    * 修改套餐(提交)
    * */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());

        setmealService.updateWithDish(setmealDto);

        return R.success("修改套餐成功!");
    }
    /*
    * 根据条件查询套餐数据
    * */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

}
