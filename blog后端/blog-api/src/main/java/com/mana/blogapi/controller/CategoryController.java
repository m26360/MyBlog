package com.mana.blogapi.controller;


import com.mana.blogapi.service.CategoryService;
import com.mana.blogapi.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping
    public Result categorys() {
        return categoryService.findAll();
    }

    @GetMapping("/detail")
    public Result categorysDetail() {
        return categoryService.findAllDetail();
    }

    @GetMapping("/detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id) {
        return categoryService.categoryDetailById(id);
    }
}
