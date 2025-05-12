package com.chasion.eventbackend.service.impl;

import com.chasion.eventbackend.entity.Article;
import com.chasion.eventbackend.entity.PageBean;
import com.chasion.eventbackend.mapper.ArticleMapper;
import com.chasion.eventbackend.service.ArticleService;
import com.chasion.eventbackend.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    // 新增文章
    public void addArticle(Article article) {
        articleMapper.addArticle(article);
    }

    // 更新文章
    public void updateArticle(Article article) {
        articleMapper.updateArticle(article);
    }

    @Override
    public Article findArticleById(int id) {
        return articleMapper.findArticleById(id);
    }

    @Override
    public void deleteArticleById(int id) {
        articleMapper.deleteArticleById(id);
    }

    @Override
    public PageBean<Article> findArticleByPage(int pageNum, int pageSize, Integer categoryId, String state) {
        PageBean<Article> pb = new PageBean<>();
        // 开启分页（紧贴实际查询语句）
        PageHelper.startPage(pageNum, pageSize);
        HashMap<String, Object> claim = ThreadLocalUtil.get();
        int userId = (Integer) claim.get("id");
        List<Article> articleByPage = articleMapper.findArticleByPage(userId, categoryId, state);
        Page<Article> p = (Page<Article>) articleByPage;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }
}
