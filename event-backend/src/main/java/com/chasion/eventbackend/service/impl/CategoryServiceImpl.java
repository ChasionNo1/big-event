package com.chasion.eventbackend.service.impl;

import com.chasion.eventbackend.entity.Category;
import com.chasion.eventbackend.mapper.CategoryMapper;
import com.chasion.eventbackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        return categoryMapper.getCategories();
    }

    @Override
    public void addCategory(Category category) {
        categoryMapper.addCategory(category);
    }

    @Override
    public Category findCategoryById(int id) {
        return categoryMapper.findCategoryById(id);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.updateCategory(category);
    }

    @Override
    public void deleteCategoryById(int id) {
        categoryMapper.deleteCategoryById(id);
    }


}
