package com.mana.blogapi.dao.dos;


import lombok.Data;

@Data
public class Archives {

    /**
     * do是关键字，所以包名为dos
     * dos 和 pojo差不多，但不需要持久化
     */

    private Integer year;

    private Integer month;

    private Long count;//发布文章的数量


}
