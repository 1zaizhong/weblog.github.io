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
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.ArticleTagRelMapper;
import com.zifengliu.weblog.common.domain.mapper.TagMapper;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.model.vo.SelectRspVO;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private UserMapper  userMapper;


    //拿到登录信息id,用来后面用
    private Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, authentication.getName()));
        if (userDO == null) {
            throw new BizException(ResponseCodeEnum.USERNAME_NOT_FOUND);
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

        // 循环处理前端传来的标签集合
        tags.forEach(tagName -> {
            // 校验该用户下是否已存在同名标签
            TagDO existsTag = tagMapper.selectOne(Wrappers.<TagDO>lambdaQuery()
                    .eq(TagDO::getName, tagName)
                    .eq(TagDO::getUserId, loginUserId));

            if (Objects.isNull(existsTag)) {
                TagDO tagDO = TagDO.builder()
                        .name(tagName)
                        .userId(loginUserId) // 绑定当前用户
                        .articlesTotal(0)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(false)
                        .build();
                tagMapper.insert(tagDO);
            } else {
                new BizException(ResponseCodeEnum.TAG_CANT_DUPLICATE);
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
        // 获取当前页、以及每页需要展示的数据数量
        Long current = findTagePageListReqVO.getCurrent();
        Long size = findTagePageListReqVO.getSize();
        String name = findTagePageListReqVO.getName();
        LocalDate startDate = findTagePageListReqVO.getStartDate();
        LocalDate endDate = findTagePageListReqVO.getEndDate();
        Long loginUserId = getLoginUserId();



        // 执行分页查询
        Page<TagDO> tagDOPage = tagMapper.selectPageList(
                current,
                size,
                name,
                startDate,
                endDate,
                loginUserId
        );
        List<TagDO> tagDOS = tagDOPage.getRecords();

        // DO 转 VO
        List<FindTagPageListRspVO> vos = null;
        //如果标签数据不为空
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
        String key = searchTagReqVO.getKey();

        // 只在当前用户的标签库中模糊搜索
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.<TagDO>lambdaQuery()
                .eq(TagDO::getUserId, loginUserId)
                .like(TagDO::getName, key));

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
        // 1. 获取标签 ID
        Long tagId = deleteTagReqVO.getId();

        // 2. 获取当前登录用户的 ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 假设你有一个方法可以通过用户名获取 UserDO
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, authentication.getName()));
        Long loginUserId = userDO.getUserId();

        // 3. 权限校验：先查询该标签是否存在，且是否属于当前用户
        TagDO tagDO = tagMapper.selectById(tagId);
        if (Objects.isNull(tagDO)) {
            return Response.fail(ResponseCodeEnum.TAG_NOT_EXISTED);
        }

        // 如果不是管理员(假设ID为1)，且标签的归属者不是当前登录人，则无权删除
        if (!Objects.equals(loginUserId, 1L) && !Objects.equals(tagDO.getUserId(), loginUserId)) {
            return Response.fail("无权删除该标签");
        }

        // 引用校验：校验该标签下是否有关联的文章
        //如果有文章正关联着它，不能直接删除
        ArticleTagRelDO articleTagRelDO = articleTagRelMapper.selectOneByTagId(tagId);

        if (Objects.nonNull(articleTagRelDO)) {
            throw new BizException(ResponseCodeEnum.TAG_CAN_NOT_DELETE);
        }


        int count = tagMapper.deleteById(tagId);

        return count == 1 ? Response.success() : Response.fail(ResponseCodeEnum.TAG_NOT_EXISTED);
    }

    /*
    * 获取标签下拉列表
    * @return
    * */
    @Override
    public Response findTagSelectList() {
        Long loginUserId = getLoginUserId();

        // 只返回当前用户的标签
        List<TagDO> tagDOS = tagMapper.selectList(Wrappers.<TagDO>lambdaQuery()
                .eq(TagDO::getUserId, loginUserId));

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
