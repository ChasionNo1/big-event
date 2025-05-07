package com.chasion.eventbackend.controller;

import com.chasion.eventbackend.entity.Category;
import com.chasion.eventbackend.entity.Result;
import com.chasion.eventbackend.service.CategoryService;
import com.chasion.eventbackend.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
public class CategoryController {
// 获取文章分类列表
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public Result<List<Category>> getCategories() {
        List<Category> categories = categoryService.getCategories();
        if (categories.isEmpty()) {
            return Result.error("没有分类数据");
        }else {
            return Result.success(categories);
        }
    }
//    新增文章分类
    @PostMapping("/category")
    public Result addCategory(@RequestBody Category category) {
        String categoryName = category.getCategoryName();
        String categoryAlias = category.getCategoryAlias();
        if (!StringUtils.hasLength(categoryName) && !StringUtils.hasLength(categoryAlias)) {
            return Result.error("输入内容有误");
        }else {
            Category newCategory = new Category();
            newCategory.setCategoryName(categoryName);
            newCategory.setCategoryAlias(categoryAlias);
            HashMap<String, Object> claim = ThreadLocalUtil.get();
            newCategory.setCreateUser((Integer) claim.get("id"));
            newCategory.setCreateTime(LocalDateTime.now());
            newCategory.setUpdateTime(LocalDateTime.now());
            categoryService.addCategory(newCategory);
            return Result.success();
        }
    }

    // 更新文章分类，修改
    @PutMapping("/category")
    public Result updateCategory(@RequestBody Category category) {
        int id = category.getId();
        String categoryName = category.getCategoryName();
        String categoryAlias = category.getCategoryAlias();
        Category categoryById = categoryService.findCategoryById(id);
        if (categoryById == null) {
            return Result.error("数据有误");
        }else {
            categoryById.setCategoryName(categoryName);
            categoryById.setCategoryAlias(categoryAlias);
            categoryById.setUpdateTime(LocalDateTime.now());
            categoryService.updateCategory(categoryById);
            return Result.success();
        }
    }

    // 获取文章分类详情
    @GetMapping("/category/detail")
    public Result<Category> getCategoryById(@RequestParam("id") int id) {
        Category category = categoryService.findCategoryById(id);
        if (category == null) {
            return Result.error(null);
        }else {
            return Result.success(category);
        }
    }

    // 删除文章分类
    @DeleteMapping("/category")
    public Result deleteCategoryById(@RequestParam("id") int id) {
        Category category = categoryService.findCategoryById(id);
        if (category == null) {
            return Result.error(null);
        }else {
            categoryService.deleteCategoryById(id);
            return Result.success();
        }
    }
}
