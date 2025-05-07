package com.chasion.eventbackend.service;

import com.chasion.eventbackend.entity.Category;

import java.util.List;

public interface CategoryService {
//    获取文章分类列表
    List<Category> getCategories();

    // 新增文章分类
    void addCategory(Category category);

    // 根据id查找
    Category findCategoryById(int id);

    // 更新
    void updateCategory(Category category);

    // 删除
    void deleteCategoryById(int id);
}
