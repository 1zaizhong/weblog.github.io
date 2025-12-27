package com.zifengliu.weblog.common.enums;

import com.zifengliu.weblog.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 自风流
 * @version 0.0.3
 * @description ResponseCodeEnum
 * @date 2025/2/20 下午4:50
 * @description 响应状态码枚举
 **/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {


    //====通用的枚举异常状态码--------------------------
    SYSTEM_ERROR("10000","出错啦,在努力敲键盘修复啦....."),
    //------通用异常码------
    PARAM_NOT_VALID("10001","参数错误"),

    //---------------业务异常状态码------
    PRODUCT_NOT_FOUND("20000","该功能不存在"),
    //权限异常码
    UNAUTHORIZED("20002","无访问权限,"),
    //登录异常码
    LOGIN_FAIL("20000", "登录失败"),
    USERNAME_OR_PWD_ERROR("20001", "用户名或密码错误"),
    //用户不存在
    USERNAME_NOT_FOUND("20003", "该用户不存在"),
    //该类已存在
    CATEGORY_NAME_IS_EXISTED("20005", "该类已存在"),
    TAG_CANT_DUPLICATE("20006", "该标签已存在"),
    File_UPLOAD_ERROR("20008", "文件上传失败"),
    CATEGORY_NOT_EXISTED("20009", "该分类不存在"),
    ARTICLE_NOT_EXISTED("20010","该文章不存在"),
    CATEGORY_CAN_NOT_DELETE("20011", "该分类下包含文章，请先删除对应文章，才能删除！"),
    TAG_CAN_NOT_DELETE("20012", "该标签下包含文章，请先删除对应文章，才能删除！"),
    TAG_NOT_EXISTED("20007", "该标签不存在"),
    NOT_QQ_NUMBER("20014", "QQ 号格式不正确"),

    COMMENT_CONTAIN_SENSITIVE_WORD("20015", "评论内容中包含敏感词，请重新编辑后再提交"),
    COMMENT_WAIT_EXAMINE("20016", "评论已提交, 等待博主审核通过"),
    COMMENT_NOT_FOUND("20017", "该评论不存在"),
    COMMENT_STATUS_NOT_WAIT_EXAMINE("20018", "该评论未处于待审核状态"),
    URL_ID_NO_FOUND("20020", "评论对应的文章作者ID没找到"),
    WIKI_NOT_FOUND("20013", "该知识库不存在");



    //异常码
    private String errorCode;

    //错误信息
    private String errorMessage;
}
