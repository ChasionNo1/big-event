package com.chasion.eventbackend.mapper;

import com.chasion.eventbackend.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {

    // 新增文章
    int addArticle(Article article);
}
