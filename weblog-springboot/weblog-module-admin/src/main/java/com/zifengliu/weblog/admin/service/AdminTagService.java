package com.zifengliu.weblog.admin.service;

import com.zifengliu.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.zifengliu.weblog.admin.model.vo.tag.AddTagReqVO;
import com.zifengliu.weblog.admin.model.vo.tag.DeleteTagReqVO;
import com.zifengliu.weblog.admin.model.vo.tag.FindTagPageListReqVO;
import com.zifengliu.weblog.admin.model.vo.tag.SearchTagReqVO;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/21 下午7:50
 * @description
 **/
public interface AdminTagService {

    /*
     * 添加标签
     * @param addTagReqVO
     * @return
     * */
    Response addTags(AddTagReqVO addTagReqVO);

    /*
     * 分类分页数据查询
     * @param findCategoryPageListReqVO
     * @return
     */
    PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO);

    /*
     * 获取标签分类的 Select 列表数据
     * @return
     * */
    Response searchTag(SearchTagReqVO searchTagReqVO);

    /*
     * 删除标签
     * @return
     * */
    Response deleteTag(DeleteTagReqVO deleteTagReqVO);

    /*
     * 获取标签分类的 Select 列表数据
     * @return
     * */
    Response findTagSelectList( );
}
