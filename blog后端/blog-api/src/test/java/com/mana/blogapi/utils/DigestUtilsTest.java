package com.mana.blogapi.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

public class DigestUtilsTest {

    @Test
    public void test1() {
        String slat = "mana!@#";
        String password ="m26360m26360";
        password = DigestUtils.md5Hex(password + slat);
        System.out.println(password);
    }
}
