package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.zifengliu.weblog.common.utils.Response;


/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/3/27 下午7:05
 * @description
 **/
public interface AdminUserService {
/*
* 修改密码
* @param  UpdateAdminUserPasswordReqVO
* @return
* */
    Response updatePassword(UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO);


    /*
    * 获取当前登录信息
    * @return
    */
    Response findUserInfo();
}
