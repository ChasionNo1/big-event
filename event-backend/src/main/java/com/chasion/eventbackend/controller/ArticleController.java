package com.chasion.eventbackend.controller;

import com.chasion.eventbackend.entity.Article;
import com.chasion.eventbackend.entity.Result;
import com.chasion.eventbackend.mapper.ArticleMapper;
import com.chasion.eventbackend.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleMapper articleMapper;


    @GetMapping("/list")
    public Result<String> list() {
        return Result.success("success");
    }

    @PostMapping
    public Result<String> addArticle(@RequestBody Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        HashMap<String, Object> claim = ThreadLocalUtil.get();
        article.setCreateUser((Integer) claim.get("id"));
        articleMapper.addArticle(article);
        return Result.success(null);
    }



}
