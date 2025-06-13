package com.zifengliu.weblog.admin.service.impl;

import com.zifengliu.weblog.admin.model.vo.file.UploadFileRspVO;
import com.zifengliu.weblog.admin.service.AdminFileService;
import com.zifengliu.weblog.admin.utils.MinioUtil;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/10 下午10:06
 * @description
 **/
@Service
@Slf4j
public class AdminFileServiceImpl implements AdminFileService {
    @Autowired
    private MinioUtil minioUtil;
    /*
    * 上传文件
    * @param file
    * @return
    * */
    @Override
    public Response uploadFile(MultipartFile file) {
        //上传文件,
        try {
            String url = minioUtil.uploadFile(file);

            //构建成功返参,将图片的访问链路返回
            return  Response.success(UploadFileRspVO.builder().url(url).build());
        } catch (Exception e) {
            log.error("上传到minio 错误:",e);
            throw new BizException(ResponseCodeEnum.File_UPLOAD_ERROR);
        }
    }
}
