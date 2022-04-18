package com.mana.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mana.blogapi.dao.dos.Archives;
import com.mana.blogapi.dao.mapper.ArticleBodyMapper;
import com.mana.blogapi.dao.mapper.ArticleMapper;
import com.mana.blogapi.dao.mapper.ArticleTagMapper;
import com.mana.blogapi.dao.pojo.*;
import com.mana.blogapi.service.*;
import com.mana.blogapi.utils.UserThreadLocal;
import com.mana.blogapi.vo.*;
import com.mana.blogapi.vo.params.ArticleParam;
import com.mana.blogapi.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page, pageParams.getCategoryId(), pageParams.getTagId(), pageParams.getYear(), pageParams.getMonth());

        return Result.success(copyList(articleIPage.getRecords(), true, true));
    }

    /**
     * listArticle 方法调用 copyList 方法，copyList 方法进而调用 copy 方法
     */
//    @Override
//    public Result listArticle(PageParams pageParams) {
//        //分页查询article数据库表
//        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
//        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper();
//        //判断是否按照类别查询
//        if (pageParams.getCategoryId() != null) {
//            lambdaQueryWrapper.eq(Article::getCategoryId, pageParams.getCategoryId());
//        }
//        //判断是否按照标签查询
//        if (pageParams.getTagId() != null) {
//            //加入查询条件  标签
//            // article表中没有Tag字段，因为一个文章可能对应多个标签
//            //article_tag这张关联表中 有article_id和tag_id（一对多）
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId, pageParams.getTagId());
//            //查出很多tag_id 等于 前端传来的参数的 article_tag表的 数据
//            List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            List<Long> articleIdList = new ArrayList<>();//用来存储 带有 所查询标签的 全部articleId
//            //为了获得 article_tag表的 tag_id相同的 每一行的article_id,并放入articleIdList
//            for (ArticleTag articleTag : articleTagList) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0) {
//                //相当于加入 and id in(x,x,x)查询条件
//                lambdaQueryWrapper.in(Article::getId, articleIdList);
//            }
//            if (pageParams.getYear() != null) {
//                lambdaQueryWrapper.
//            }
//
//        }
//        //进行置顶排序
//        //时间降序排序
//        //order by create_data desc
//        lambdaQueryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, lambdaQueryWrapper);
//        //获取Article的List
//        List<Article> records = articlePage.getRecords();
//        //不能直接返回records 需要转换
//        List<ArticleVo> articleVoList = copyList(records, true, true);
//        return Result.success(articleVoList);
//    }

    //    将 Article 转换为 ArticleVo
//    isTag和isAuthor判断是否需要添加 标签 和 作者 属性
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        //BeanUtils由Spring提供,讲article中的属性值赋值给articleVo(共同的属性)
        BeanUtils.copyProperties(article, articleVo);
        //article中的createDate属性类型为long, articleVo中的createDate属性类型为String
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //是否添加标签
        if (isTag) {
            Long articleId = article.getId();
            //多表联查 查找id所对应的Tags
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        //是否添加作者(sysUser的Nickname属性)
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            SysUser sysUser = sysUserService.findUserById(authorId);
            articleVo.setAuthor(sysUser.getNickname());
        }
        //是否添加Body信息
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        //是否添加文章类别
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            CategoryVo categoryVo = categoryService.findCategoryById(categoryId);
            articleVo.setCategory(categoryVo);
//            List<CategoryVo> categoryVoList = new ArrayList<>();
//            categoryVoList.add(categoryVo);
//            articleVo.setCategorys(categoryVoList);
        }
        return articleVo;
    }

    //将 Article 转换为 ArticleVo
    //isTag和isAuthor判断是否需要添加 标签 和 作者 属性
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        //BeanUtils由Spring提供,讲article中的属性值赋值给articleVo(共同的属性)
        BeanUtils.copyProperties(article, articleVo);
        //article中的createDate属性类型为long, articleVo中的createDate属性类型为String
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //是否添加标签
        if (isTag) {
            Long articleId = article.getId();
            //多表联查 查找id所对应的Tags
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        //是否添加作者(sysUser的Nickname属性)
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            SysUser sysUser = sysUserService.findUserById(authorId);
            articleVo.setAuthor(sysUser.getNickname());
        }
        return articleVo;
    }

    //见名知意
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo(articleBody.getContent());
        return articleBodyVo;
    }

    //将 List<Article> 转换为 List<ArticleVo>
    //循环copy(xxxx)
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor));
        }
        return articleVoList;
    }

    //将 List<Article> 转换为 List<ArticleVo>
    //循环copy(xxxx)
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }


    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper();
        //根据浏览量进行降序排序
        lambdaQueryWrapper.orderByDesc(Article::getViewCounts);
        //只需要取id和title字段
        lambdaQueryWrapper.select(Article::getId, Article::getTitle);
        //sql语句末尾添加limit(不要忘记空格)
        lambdaQueryWrapper.last("limit " + limit);
        //select id,title from mn_article order by view_counts desc limit x;
        List<Article> articles = articleMapper.selectList(lambdaQueryWrapper);
        List<ArticleVo> articleVoList = copyList(articles, false, false);
        return Result.success(articleVoList);
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper();
        //根据发布时间进行降序排序
        lambdaQueryWrapper.orderByDesc(Article::getCreateDate);
        //只需要取id和title字段
        lambdaQueryWrapper.select(Article::getId, Article::getTitle);
        //sql语句末尾添加limit(不要忘记空格)
        lambdaQueryWrapper.last("limit " + limit);
        //select id,title from mn_article order by create_data desc limit x;
        List<Article> articles = articleMapper.selectList(lambdaQueryWrapper);
        List<ArticleVo> articleVoList = copyList(articles, false, false);
        return Result.success(articleVoList);
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1.根据id查询文章详情
         * 2.根据bodyId和categoryId 去做关联查询
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);
        //查看完文章，在这里增加阅读数
        //本应该直接返回数据，这时候做了一个更新操作（增加阅读数），更新的时候会添加 写锁，阻塞其他的操作，性能会降低
        //更新增加了此次接口的耗时 如果一旦更新出问题，不能影响查看文章操作
        //所以使用线程池技术，可以把更新操作扔到线程池中去执行，就不会影响主线程了
        threadService.updateArticleViewCount(articleId);
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        /**
         * 1.发布文章 构建 Article对象 需要以下属性值
         * 2.作者 id 当前登录用户
         * 3.标签 要将标签加入到 关联列表 当中
         * 4.body 文章的内容
         * 5.将 body储存到 article里
         * 6.更新 article
         * 7.给前端返回 文章id，为了发布完成后，跳转到 该文章
         */
        Article article = new Article();
        //将此接口（/articles/publish）添加到登录拦截器中
        SysUser sysUser = UserThreadLocal.get();//拿到作者id（也就是当前登录用户）
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);//设置权重
        article.setSummary(articleParam.getSummary());//设置摘要
        article.setCommentCounts(0);//评论数
        article.setCreateDate(System.currentTimeMillis());//发布时间
        article.setCategoryId(articleParam.getCategory().getId());//设置类别
        article.setWeight(0);//不置顶
        article.setViewCounts(0);//阅读数
        article.setTitle(articleParam.getTitle());
        //插入之后，会生成一个文章id          (mp的自动回写？？？？？？？？？？？)
        articleMapper.insert(article);

        //3.tag（为选中的 标签 和 文章 做关联，通过 mn_article_tag表的 标签id和 文章id）
        List<TagVo> tagVoList = articleParam.getTags();//拿到tagVoList
        if (tagVoList != null) {
            //一个文章id可能对应多个标签
            //一次循环为 一个标签 绑定 同一个文章id,对mn_article_tag表插入一条数据
            for (TagVo tagVo : tagVoList) {
                Long id = article.getId();
                ArticleTag articleTag = new ArticleTag();//对mn_article_tag表
                articleTag.setArticleId(article.getId());//mn_article_tag表 关联文章id
                articleTag.setTagId(tagVo.getId());//mn_article_tag表 关联标签id
                articleTagMapper.insert(articleTag);//对mn_article_tag表插入一条关联完的数据(标签对应文章id)
            }
        }

        //4.body 文章的内容
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        //从前端的articleParam中取
        articleBody.setContent(articleParam.getBody().getContent());
        //从前端的articleParam中取
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        //5.将body储存到article里
        article.setBodyId(articleBody.getId());

        //6.更新article(将新建的article，被赋值完的属性，对老的article进行更新)
        articleMapper.updateById(article);

        //7.给前端返回 id
        //这里也可以直接返回ArticleVo对象，但是Long类型的id会有精度损失，需要在属性上加注解
        //也可以通过返回map的形式传回id
        Map<String, String> map = new HashMap<>();
        map.put("id", article.getId().toString());
        return Result.success(map);
//        return Result.success(article);
    }
}
