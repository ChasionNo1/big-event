package com.chasion.eventbackend.controller;

import com.chasion.eventbackend.entity.Article;
import com.chasion.eventbackend.entity.PageBean;
import com.chasion.eventbackend.entity.Result;
import com.chasion.eventbackend.mapper.ArticleMapper;
import com.chasion.eventbackend.service.ArticleService;
import com.chasion.eventbackend.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/article")
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*", // 或列出具体头部（如 "Authorization", "Content-Type"）
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.DELETE}
)
public class ArticleController {
    @Autowired
    private ArticleService articleService;


    @GetMapping("/list")
    public Result<String> list() {
        return Result.success("success");
    }

//    添加文章
    @PostMapping
    public Result<String> addArticle(@RequestBody Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        HashMap<String, Object> claim = ThreadLocalUtil.get();
        article.setCreateUser((Integer) claim.get("id"));
        articleService.addArticle(article);
        return Result.success("添加成功");
    }

    // 更新文章
    // 更新字段 id title content coverImg state categoryId
    @PutMapping
    public Result<String> updateArticle(@RequestBody Article article) {
        Integer id = article.getId();
        if (id == null) {
            return Result.error("id is null");
        }else {
            Article articleById = articleService.findArticleById(id);
            if (articleById == null) {
                return Result.error("article not found");
            }
        }
        article.setUpdateTime(LocalDateTime.now());
        articleService.updateArticle(article);
        return Result.success(null);
    }

    // 获取文章详情
    @GetMapping("/detail")
    public Result<Article> getArticleById(@RequestParam("id") int id) {
        Article articleById = articleService.findArticleById(id);
        if (articleById == null) {
            return Result.error("article not found");
        }
        return Result.success(null, articleById);
    }

    // 删除文章
    @DeleteMapping
    public Result deleteArticleById(@RequestParam("id") int id) {
        Article articleById = articleService.findArticleById(id);
        if (articleById == null) {
            return Result.error("article not found");
        }else {
            articleService.deleteArticleById(id);
            return Result.success(null);
        }
    }

    // 查询文章列表带分页
    @GetMapping
    public Result<PageBean<Article>> getArticles(int pageNum, int pageSize,
                                                 @RequestParam(required = false) Integer categoryId,
                                                 @RequestParam(required = false) String state) {
        PageBean<Article> result = articleService.findArticleByPage(pageNum, pageSize, categoryId, state);
        return Result.success(null, result);
    }





}
