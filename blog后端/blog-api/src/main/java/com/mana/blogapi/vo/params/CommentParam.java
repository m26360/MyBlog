package com.mana.blogapi.vo.params;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class CommentParam {
    //防止前端 精度损失 把id转为string
    // 分布式id 比较长，传到前端 会有精度损失
    // 导致子评论的id无法完全传到前端，导致无法显示子评论
    // 必须转为string类型 进行传输，就不会有问题了
    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    private String content;

    private Long parent;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long toUserId;

}
