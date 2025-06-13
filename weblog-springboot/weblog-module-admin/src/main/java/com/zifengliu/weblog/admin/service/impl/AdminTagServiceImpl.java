package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zifengliu.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.zifengliu.weblog.admin.model.vo.category.FindCategoryPageListRspVO;
import com.zifengliu.weblog.admin.model.vo.tag.*;
import com.zifengliu.weblog.admin.service.AdminTagService;
import com.zifengliu.weblog.common.domain.dos.ArticleTagRelDO;
import com.zifengliu.weblog.common.domain.dos.TagDO;
import com.zifengliu.weblog.common.domain.dos.TagDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleTagRelMapper;
import com.zifengliu.weblog.common.domain.mapper.TagMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.model.vo.SelectRspVO;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/4/21 下午7:54
 * @description  标签的实现类
 **/
@Service
public class AdminTagServiceImpl extends ServiceImpl<TagMapper, TagDO> implements AdminTagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    /*
    * 添加标签集合
    * @param addTagReqVO
    * @return */
    @Override
    public Response addTags(AddTagReqVO addTagReqVO) {

        //VO 转成DO
        List<TagDO> tagDOS = addTagReqVO.getTags()
                .stream().map(tagName -> TagDO.builder()
                        .name(tagName.trim())//去掉前后空格
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
        //批量插入
      try {
          saveBatch(tagDOS);
      }catch (Exception e){
          log.warn("标签已经存在");
          throw new BizException(ResponseCodeEnum.TAG_CANT_DUPLICATE);
      }

        return Response.success();
    }


    /*
     * 标签分页数据查询
     * @param findTagePageListReqVO
     * @return
     * */
    @Override
    public PageResponse findTagPageList(FindTagPageListReqVO findTagePageListReqVO) {
        // 获取当前页、以及每页需要展示的数据数量
        Long current = findTagePageListReqVO.getCurrent();
        Long size = findTagePageListReqVO.getSize();
        String name = findTagePageListReqVO.getName();
        LocalDate startDate = findTagePageListReqVO.getStartDate();
        LocalDate endDate = findTagePageListReqVO.getEndDate();



        // 执行分页查询
        Page<TagDO> page = tagMapper.selectPageList(current, size,name, startDate, endDate);

        List<TagDO> records = page.getRecords();

        // DO 转 VO
        List<FindTagPageListRspVO> vos = null;
        //如果标签数据不为空
        if (!CollectionUtils.isEmpty(records)) {
            vos = records.stream()
                    .map(tagDO -> FindTagPageListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .createTime(tagDO.getCreateTime())
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(page, vos);
    }


    /*
    * 根据标签模糊查询
    * @return
    * */
    @Override
    public Response searchTag(SearchTagReqVO searchTagReqVO) {
        String key = searchTagReqVO.getKey();


        //执行模糊查询
       List<TagDO> tagDOS=  tagMapper.selectByKey(key);

       //DO 转 VO
       List<SelectRspVO> vos = null;
       if (!CollectionUtils.isEmpty(tagDOS)){
           vos = tagDOS.stream()
                   .map(tagDO -> SelectRspVO.builder()
                           .label(tagDO.getName())
                           .value(tagDO.getId())
                           .build())
                   .collect(Collectors.toList());
       }


       return  Response.success(vos);

    }
    /**
     * 删除标签
     *
     * @param deleteTagReqVO
     * @return
     */
    @Override
    public Response deleteTag(DeleteTagReqVO deleteTagReqVO) {
        // 标签 ID
        Long tagId = deleteTagReqVO.getId();

        // 校验该标签下是否有关联的文章，若有，则不允许删除，提示用户需要先删除标签下的文章
        ArticleTagRelDO articleTagRelDO = articleTagRelMapper.selectOneByTagId(tagId);

        if (Objects.nonNull(articleTagRelDO)) {
            log.warn("==> 此标签下包含文章，无法删除，tagId: {}");
            throw new BizException(ResponseCodeEnum.TAG_CAN_NOT_DELETE);
        }

        // 根据标签 ID 删除
        int count = tagMapper.deleteById(tagId);

        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.TAG_NOT_EXISTED);
    }

    /*
    * 获取标签下拉列表
    * @return
    * */
    @Override
    public Response findTagSelectList() {
        // 查询所有标签, Wrappers.emptyWrapper() 表示查询条件为空
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.emptyWrapper());

        // DO 转 VO
        List<SelectRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> SelectRspVO.builder()
                            .label(tagDO.getName())
                            .value(tagDO.getId())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(vos);
    }

}
