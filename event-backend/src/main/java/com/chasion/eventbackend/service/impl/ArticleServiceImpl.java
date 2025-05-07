package com.chasion.eventbackend.service.impl;

import com.chasion.eventbackend.entity.Article;
import com.chasion.eventbackend.mapper.ArticleMapper;
import com.chasion.eventbackend.service.ArticleService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    // 新增文章
    public void addArticle(Article article) {
        articleMapper.addArticle(article);
    }
}
