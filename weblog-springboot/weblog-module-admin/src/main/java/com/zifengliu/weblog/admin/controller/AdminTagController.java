package com.zifengliu.weblog.admin.controller;


import com.zifengliu.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.zifengliu.weblog.admin.model.vo.tag.AddTagReqVO;

import com.zifengliu.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.zifengliu.weblog.admin.model.vo.tag.FindTagPageListReqVO;
import com.zifengliu.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.zifengliu.weblog.admin.service.AdminTagService;
import com.zifengliu.weblog.common.aspect.ApiOperationLog;

import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/10 下午5:57
 * @description   标签
 **/
@RestController
@RequestMapping("/admin/tag")
@Api(tags = "Admin 标签模块")
public class AdminTagController {

    @Autowired
    private AdminTagService tagService;

    @PostMapping("/add")
    @ApiOperation(value = "添加标签")
    @ApiOperationLog(description = "添加标签")
    public Response addTags(@RequestBody @Validated AddTagReqVO addTagReqVO) {
        return tagService.addTags(addTagReqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "标签分页数据获取")
    @ApiOperationLog(description = "标签分页数据获取")
    public PageResponse findTagPageList(@RequestBody @Validated FindTagPageListReqVO findTagPageListReqVO) {
        return tagService.findTagPageList(findTagPageListReqVO);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除标签")
    @ApiOperationLog(description = "删除标签")
    public Response deleteTag(@RequestBody @Validated DeleteTagReqVO deleteTagReqVO) {
        return tagService.deleteTag(deleteTagReqVO);
    }

     @PostMapping("/search")
     @ApiOperation(value = "分类 Select 下拉列表数据获取")
     @ApiOperationLog(description = "分类 Select 下拉列表数据获取")
     public Response searchTag(SearchTagReqVO searchTagReqVO) {
         return tagService.searchTag(searchTagReqVO);
     }

    @PostMapping("/select/list")
    @ApiOperation(value = "查询标签 Select 列表数据")
    @ApiOperationLog(description = "查询标签 Select 列表数据")
    public Response findTagSelectList() {
        return tagService.findTagSelectList();
    }

}
