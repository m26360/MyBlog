package com.mana.blogapi.controller;


import com.mana.blogapi.utils.QiniuUtils;
import com.mana.blogapi.vo.ErrorCode;
import com.mana.blogapi.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile multipartFile) {
        //原始文件名称 例如mana.png
        String originalFilename = multipartFile.getOriginalFilename();
        //生成随机上传文件名
        String fileName = UUID.randomUUID().
                toString() +
                "." +
                StringUtils.substringAfterLast(originalFilename, ".");
        //上传到七牛云
        boolean upload = qiniuUtils.upload(multipartFile, fileName);
        if (upload) {
            //成功则返回域名+文件名
            return Result.success(QiniuUtils.url + "/" + fileName);
        }
        //返回上传失败
        return Result.fail(ErrorCode.FAIL_UPLOAD.getCode(), ErrorCode.FAIL_UPLOAD.getMsg());
    }
}
