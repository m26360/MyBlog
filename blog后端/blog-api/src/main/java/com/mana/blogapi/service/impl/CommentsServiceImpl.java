package com.mana.blogapi.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mana.blogapi.dao.mapper.CommentMapper;
import com.mana.blogapi.dao.pojo.Comment;
import com.mana.blogapi.dao.pojo.SysUser;
import com.mana.blogapi.service.CommentsService;
import com.mana.blogapi.service.SysUserService;
import com.mana.blogapi.utils.UserThreadLocal;
import com.mana.blogapi.vo.CommentVo;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.UserVo;
import com.mana.blogapi.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1.根据文章id查询评论列表 从 comment表中查询
         * 2.根据作者id查询作者信息
         * 3.判断 level，如果level=1，去查询它有没有子评论
         * 4.如果有子评论，根据评论id（parent_id）进行查询
         */
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper();
        //根据id查询
        lambdaQueryWrapper.eq(Comment::getArticleId, id);
        //查询 level = 1
        lambdaQueryWrapper.eq(Comment::getLevel, 1);
        List<Comment> commentsList = commentMapper.selectList(lambdaQueryWrapper);
        List<CommentVo> commentVoList = copyList(commentsList);
        return Result.success(commentVoList);
    }

    private List<CommentVo> copyList(List<Comment> commentsList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentsList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    /**
     * 将Comment转换为CommentVo
     * 1.根据作者id查询作者信息
     * 2判断 level，如果level=1，去查询它有没有子评论
     * 3.如果有子评论，根据评论id（parent_id）进行查询
     */
    private CommentVo copy(Comment comment) {
        /**
         * 父评论下面有子评论，所以父评论的 level=1
         * 子评论是给父亲评论，所以子评论的 level>1  (等于2)
         */
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        //作者信息(UserVo)
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();
        /**
         * 父评论下面有子评论，所以父评论的 level=1
         * 子评论是给父亲评论，所以子评论的 level>1  (等于2)
         */
        if (level == 1) {
            Long id = comment.getId();//此处id作为父id（parentsId进行查询）
            List<CommentVo> commentVoList = findCommentsByParentsId(id);
            //把通过自己的id找到的儿子 放入自己的children属性中
            commentVo.setChildren(commentVoList);
        }
        /**
         * //toUser 给谁评论
         * 父评论下面有子评论，所以父评论的 level=1
         * 子评论是给父亲评论，所以子评论的 level>1  (等于2)
         */
        if (level > 1) {
            Long toUid = comment.getToUid();
            //找到被自己(也就是儿子)评论的那个UserVo
            //儿子的toUid属性值应该是对应着父亲
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            //setToUser 设置自己(也就是儿子)的父亲 为 userVo
            commentVo.setToUser(userVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentsId(Long id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //通过父亲的id和儿子的getParentId匹配，找到儿子
        lambdaQueryWrapper.eq(Comment::getParentId, id);
        /**
         * 父评论下面有子评论，所以父评论的 level=1
         * 子评论是给父亲评论，所以子评论的 level>1  (等于2)
         * 所以子评论（儿子）的 level还必须 =2
         */
        lambdaQueryWrapper.eq(Comment::getLevel, 2);
        List<Comment> commentList = commentMapper.selectList(lambdaQueryWrapper);
        return copyList(commentList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }
}
