package com.mana.blogapi.service;

import com.mana.blogapi.vo.CategoryVo;
import com.mana.blogapi.vo.Result;


public interface CategoryService {


    /**
     * 根据id查询类别
     *
     * @param categoryId
     * @return
     */
    CategoryVo findCategoryById(Long categoryId);


    /**
     * 查询全部类别
     *
     * @return
     */
    Result findAll();


    /**
     * 该方法比 findAll()多查询了一个字段 private String description
     *
     * @return
     */
    Result findAllDetail();

    /**
     * 根据id查询类别详情
     *
     * @param id
     * @return
     */
    Result categoryDetailById(Long id);
}
