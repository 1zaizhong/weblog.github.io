package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.common.utils.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/12 下午5:19
 * @description
 **/
public interface AdminFileService {
    Response uploadFile(MultipartFile file);
}
