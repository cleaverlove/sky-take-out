package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO) {
        // 向菜品表插入1条数据
        // 因为DishDTO中包含口味的数据，所以传参的时候传 实体类
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);

        // 获取insert语句生成的主键值 【需要DishMapper.xml配合】
        Long dishId = dish.getId();

        // 向口味表插入n条数据
        List<DishFlavor> flavorList = dishDTO.getFlavors();
        if (flavorList != null && flavorList.size() > 0) {
            // 遍历集合，设置菜品ID
            flavorList.forEach(dishflavor -> {
                dishflavor.setDishId(dishId);
            });
            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavorList);
        }

    }
}
