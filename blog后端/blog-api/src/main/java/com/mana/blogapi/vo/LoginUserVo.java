package com.mana.blogapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserVo {

    private Long id;

    private String account;

    private String nickname;

    private String avatar;


}
