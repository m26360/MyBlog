package com.mana.blogapi.service;

import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.params.CommentParam;

public interface CommentsService {


    /**
     * 根据文章id 查询评论列表
     *
     * @param id 文章id
     * @return
     */
    Result commentsByArticleId(Long id);

    /**
     * 评论功能
     *
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}
