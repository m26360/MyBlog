package com.mana.blogapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryVo {
    private Long id;

    private String avatar;

    private String categoryName;

    public CategoryVo() {

    }

}
