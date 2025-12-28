package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zifengliu.weblog.admin.model.vo.tag.*;
import com.zifengliu.weblog.admin.service.AdminTagService;
import com.zifengliu.weblog.common.domain.dos.ArticleTagRelDO;
import com.zifengliu.weblog.common.domain.dos.TagDO;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleTagRelMapper;
import com.zifengliu.weblog.common.domain.mapper.TagMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.model.vo.SelectRspVO;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    @Autowired
    private UserMapper  userMapper;


    //拿到登录信息id,用来后面用
    private Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, authentication.getName()));
        if (userDO == null) {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }
        return userDO.getUserId();
    }
    /*
    * 添加标签集合
    * @param addTagReqVO
    * @return */
    @Override
    public Response addTags(AddTagReqVO addTagReqVO) {

        Long loginUserId = getLoginUserId();
        List<String> tags = addTagReqVO.getTags();

        tags.forEach(tagName -> {
            // 每个用户只能在自己名下创建不重名的标签
            TagDO existsTag = tagMapper.selectOne(Wrappers.<TagDO>lambdaQuery()
                    .eq(TagDO::getName, tagName)
                    .eq(TagDO::getUserId, loginUserId));

            if (Objects.isNull(existsTag)) {
                TagDO tagDO = TagDO.builder()
                        .name(tagName)
                        .userId(loginUserId)
                        .articlesTotal(0)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(false)
                        .build();
                tagMapper.insert(tagDO);
            }
        });

        return Response.success();
    }


    /*
     * 标签分页数据查询
     * @param findTagePageListReqVO
     * @return
     * */
    @Override
    public PageResponse findTagPageList(FindTagPageListReqVO findTagePageListReqVO) {

        Long loginUserId = getLoginUserId();

        // 特权逻辑：如果是 ID 为 1 的 Admin，则 userId 传 null 以查询全表
        Long searchUserId = Objects.equals(loginUserId, 1L) ? null : loginUserId;

        // 注意：TagMapper.selectPageList 需要支持 userId 为 null 时的动态判断
        Page<TagDO> tagDOPage = tagMapper.selectPageList(
                findTagePageListReqVO.getCurrent(),
                findTagePageListReqVO.getSize(),
                findTagePageListReqVO.getName(),
                findTagePageListReqVO.getStartDate(),
                findTagePageListReqVO.getEndDate(),
                searchUserId
        );

        List<TagDO> tagDOS = tagDOPage.getRecords();
        List<FindTagPageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> FindTagPageListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .createTime(tagDO.getCreateTime())
                            .articlesTotal(tagDO.getArticlesTotal())
                            .build())
                    .collect(Collectors.toList());
        }

        return PageResponse.success(tagDOPage, vos);
    }


    /*
    * 根据标签模糊查询
    * @return
    * */
    @Override
    public Response searchTag(SearchTagReqVO searchTagReqVO) {
        Long loginUserId = getLoginUserId();
        // Admin 搜全，普通用户搜自己
        Long searchUserId = Objects.equals(loginUserId, 1L) ? null : loginUserId;

        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.<TagDO>lambdaQuery()
                .eq(Objects.nonNull(searchUserId), TagDO::getUserId, searchUserId)
                .like(TagDO::getName, searchTagReqVO.getKey()));

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
    /**
     * 删除标签
     *
     * @param deleteTagReqVO
     * @return
     */
    @Override
    public Response deleteTag(DeleteTagReqVO deleteTagReqVO) {
        Long tagId = deleteTagReqVO.getId();
        Long loginUserId = getLoginUserId();

        TagDO tagDO = tagMapper.selectById(tagId);
        if (Objects.isNull(tagDO)) {
            return Response.fail(ResponseCodeEnum.TAG_NOT_EXISTED);
        }

        // 越权校验：非 Admin 且 标签不属于自己
        if (!Objects.equals(loginUserId, 1L) && !Objects.equals(tagDO.getUserId(), loginUserId)) {
            throw new BizException(ResponseCodeEnum.UNAUTHORIZED);
        }

        // 引用校验：只要全站有文章在引用此标签，谁都不能删
        ArticleTagRelDO articleTagRelDO = articleTagRelMapper.selectOneByTagId(tagId);
        if (Objects.nonNull(articleTagRelDO)) {
            throw new BizException(ResponseCodeEnum.TAG_CAN_NOT_DELETE);
        }

        tagMapper.deleteById(tagId);
        return Response.success();
    }

    /*
    * 获取标签下拉列表
    * @return
    * */
    @Override
    public Response findTagSelectList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userMapper.findByUsername(authentication.getName()).getUserId();

        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.<TagDO>lambdaQuery()
                .eq(TagDO::getUserId, userId)
                .orderByDesc(TagDO::getCreateTime));

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
