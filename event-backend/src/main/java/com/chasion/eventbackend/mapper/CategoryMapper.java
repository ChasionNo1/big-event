package com.chasion.eventbackend.mapper;

import com.chasion.eventbackend.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("select * from category")
    List<Category> getCategories();

    int addCategory(Category category);

    @Select("select * from category where id = #{id}")
    Category findCategoryById(int id);

    @Update("update category set category_name=#{categoryName},category_alias=#{categoryAlias},update_time=#{updateTime} where id=#{id}")
    int updateCategory(Category category);

    @Delete("delete from category where id=#{id}")
    int deleteCategoryById(int id);
}
