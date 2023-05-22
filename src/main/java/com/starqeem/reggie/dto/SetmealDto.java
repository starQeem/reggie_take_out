package com.starqeem.reggie.dto;

import com.starqeem.reggie.pojo.Setmeal;
import com.starqeem.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
