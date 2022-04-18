package com.mana.blogapi.controller;


import com.mana.blogapi.common.aop.LogAnnotation;
import com.mana.blogapi.common.cache.Cache;
import com.mana.blogapi.service.ArticleService;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.params.ArticleParam;
import com.mana.blogapi.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//json数据进行交互
@RestController
@RequestMapping("/articles")
public class ArticleController {

    /**
     * 首页 文章列表
     *
     * @param pageParams
     * @return
     */

    @Autowired
    private ArticleService articleService;

    /**
     * 首页展示全部文章
     *
     * @param pageParams
     * @return
     */
    //加上此接口，代表要对此接口记录日志
//    @Cache(expire = 5 * 60 * 1000, name = "listArticle")
    @LogAnnotation(module = "文章", operator = "获取文章列表")
    @PostMapping
    public Result listArticle(@RequestBody PageParams pageParams) {
        //System.out.println("6666666666666666666666666666666");
        //return new Result(true,200,"success",articleService);
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页最热文章（浏览量最多）
     *
     * @return
     */
//    @Cache(expire = 5 * 60 * 1000, name = "hot_article")
    @PostMapping("/hot")
    public Result hotArticle() {
        int limit = 3;//取前几条
        return articleService.hotArticle(limit);
    }

    /**
     * 首页最新文章
     *
     * @return
     */
//    @Cache(expire = 5 * 60 * 1000, name = "new_article")
    @PostMapping("/new")
    public Result newArticles() {
        int limit = 3;//取前几条
        return articleService.newArticles(limit);
    }

    /**
     * 文章归档
     *
     * @return
     */
    @PostMapping("/listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    @PostMapping("/view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        return articleService.findArticleById(articleId);
    }

    /**
     * 发布文章
     */
    @PostMapping("/publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }
}
