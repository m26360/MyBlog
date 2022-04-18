package com.mana.blogapi.service;

import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);


    /**
     * 最热标签
     *
     * @param limit
     * @return
     */
    Result hots(int limit);

    /**
     * 查询所有文章标签
     *
     * @return
     */
    Result findAll();

    /**
     * 查询标签，比findAll查询的字段更多更详细，和category类似
     *
     * @return
     */
    Result findAllDetail();

    /**
     * 根据 id查询标签
     *
     * @param id
     * @return
     */
    Result findDetailById(Long id);
}
