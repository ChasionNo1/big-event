package com.chasion.eventbackend.mapper;

import com.chasion.eventbackend.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper {

    // 新增文章
    int addArticle(Article article);

    // 更新文章
    int updateArticle(Article article);

    // 查找文章
    @Select("select * from article where id = #{id}")
    Article findArticleById(int id);

    // 删除文章
    @Delete("delete from article where id = #{id}")
    int deleteArticleById(int id);

    // 分页查询
    List<Article> findArticleByPage(@Param("id") int id, @Param("categoryId") Integer categoryId, @Param("state") String state);
}
