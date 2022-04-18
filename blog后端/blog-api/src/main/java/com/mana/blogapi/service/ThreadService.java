package com.mana.blogapi.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mana.blogapi.dao.mapper.ArticleMapper;
import com.mana.blogapi.dao.pojo.Article;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {

    @Autowired
    ArticleMapper articleMapper;

    @Async("taskExecutor")
    public void updateArticleViewCount(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        Article updateArticle = new Article();
        /**
         * 通过copy所有属性值的方式
         * 防止因为 Article类和 ArticleVo的 commentCounts，weight属性为 int类型而非 Integer类型
         * 导致修改的时候被重置为默认值 0
         * 但是最好属性不要出现基本数据类型
         * 所以我就把 int也改成 Integer了
         * 但是下面的复制全部属性 会导致 sql语句更新的时候 修改全部字段
         * 可能会降低效率？？？？？？？？？？？？
         * mana的一些感想及疑问
         */
        BeanUtils.copyProperties(article, updateArticle);
        updateArticle.setViewCounts(article.getViewCounts() + 1);
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Article::getId, article.getId());
        //设置一个 为了在多线程环境下 线程安全
        lambdaUpdateWrapper.eq(Article::getViewCounts, article.getViewCounts());
        //update article set view_count=xx+1 where view_count=xx and id=xx;
        articleMapper.update(updateArticle, lambdaUpdateWrapper);
//        try {
//            //睡眠5秒 证明不会影响主线程的使用
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
