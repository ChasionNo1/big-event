package com.chasion.eventbackend.service;

import com.chasion.eventbackend.entity.Article;
import com.chasion.eventbackend.entity.PageBean;

import java.util.List;

public interface ArticleService {

//    新增文章
    void addArticle(Article article);

    // 更新文章
    void updateArticle(Article article);

    // 查找文章
    Article findArticleById(int id);

    // 删除文章
    void deleteArticleById(int id);

    // 分页查询
    PageBean<Article> findArticleByPage(int pageNum, int pageSize, Integer categoryId, String state);
}
