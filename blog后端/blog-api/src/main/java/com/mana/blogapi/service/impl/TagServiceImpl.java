package com.mana.blogapi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mana.blogapi.dao.mapper.TagMapper;
import com.mana.blogapi.dao.pojo.Tag;
import com.mana.blogapi.service.TagService;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.TagVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;


    //通过文章id寻找对应标签
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    //查询最热标签

    /**
     * 1.标签所拥有的文章数量最多
     * 2.查询 根据tag_id 分组 计数，从大到小排列，拿到前 limit个
     * 3.
     * SELECT COUNT(tag_id)AS `count`,tag_id
     * FROM mn_article_tag GROUP BY tag_id
     * ORDER BY `count` DESC LIMIT 4;
     *
     * @param limit
     * @return
     */
    @Override
    public Result hots(int limit) {
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        //如果tagIds为空
        if (CollectionUtils.isEmpty(tagIds)) {
            return Result.success(Collections.emptyList());
        }
        //需要的是tagId tagName Tag对象
        //select * from tag where id in (x,x,x,x);
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Tag::getId, Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(lambdaQueryWrapper);
        List<TagVo> tagVos = copyList(tagList);
        return Result.success(tagVos);
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<>());
        List<TagVo> tagVos = copyList(tagList);
        return Result.success(tagVos);
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVo tagVo = copy(tag);
        return Result.success(tagVo);
    }

    public TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

    public List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }
}
