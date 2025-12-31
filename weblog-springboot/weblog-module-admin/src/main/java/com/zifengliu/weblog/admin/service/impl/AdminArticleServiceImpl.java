package com.zifengliu.weblog.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.zifengliu.weblog.admin.convert.ArticleDetailConvert;
import com.zifengliu.weblog.admin.event.DeleteArticleEvent;
import com.zifengliu.weblog.admin.event.PublishArticleEvent;
import com.zifengliu.weblog.admin.event.UpdateArticleEvent;
import com.zifengliu.weblog.admin.model.vo.article.*;
import com.zifengliu.weblog.admin.service.AdminArticleService;
import com.zifengliu.weblog.common.domain.dos.*;
import com.zifengliu.weblog.common.domain.mapper.*;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.PageResponse;
import com.zifengliu.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/5/13 下午10:00
 * @description 文章管理
 **/
@Service
@Slf4j
public class AdminArticleServiceImpl implements AdminArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
   private ArticleTagRelMapper articleTagRelMapper;
    @Autowired
   private ArticleCategoryRelMapper articleCategoryRelMapper;
    @Autowired
   private CategoryMapper categoryMapper;
    @Autowired
   private ArticleContentMapper articleContentMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private  UserMapper userMapper;
    //登录人的id
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
    * 发布文章
    * @param publishArticleReqVO
    * @return
    * */
    @Override
    @Transactional(rollbackFor =  Exception.class)
    public Response publishArticle(PublishArticleReqVO publishArticleReqVO) {
        // 1. 获取当前发帖人 ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery().eq(UserDO::getUsername, username));

        // 2. 构建 ArticleDO 时，把 userId 塞进去
        ArticleDO articleDO = ArticleDO.builder()
                .title(publishArticleReqVO.getTitle())
                .cover(publishArticleReqVO.getCover())
                .summary(publishArticleReqVO.getSummary())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .userId(userDO.getUserId())
                .type(1) // 默认普通文章，或者从 VO 中取
                .build();

        articleMapper.insert(articleDO);

        //拿到插入记录的主键ID
        Long articleId = articleDO.getId();

        //VO 转 ArticleContentDO 并保存

        ArticleContentDO articleContentDO = ArticleContentDO.builder()
                .content(publishArticleReqVO.getContent())
                .articleId(articleId)
                .build();
        articleContentMapper.insert(articleContentDO);

        //处理文章关联的分类
        Long categoryId = publishArticleReqVO.getCategoryId();

        //校验提交时的分类是否存在
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if (categoryDO == null ) {
            throw  new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }


        //VO 转 ArticleCategoryRelDO 并保存
        ArticleCategoryRelDO articleCategoryRelDO = ArticleCategoryRelDO.builder()
                .articleId(articleId)
                .categoryId(categoryId)
                .build();
        articleCategoryRelMapper.insert(articleCategoryRelDO);

        //保存文章关联的标签集合

        List<String> publishTags = publishArticleReqVO.getTags();
        insertTags(articleId,publishTags);

        // 发送文章发布事件
        eventPublisher.publishEvent(new PublishArticleEvent(this, articleId));

    return Response.success();

    }





    /**
     * 保存标签
     * @param articleId
     * @param publishTags
     */
    private void insertTags(Long articleId, List<String> publishTags) {
        if (CollectionUtils.isEmpty(publishTags)) return;
        Long userId = getLoginUserId();

        List<Long> tagIdsToRelate = Lists.newArrayList();
        List<String> needCheckNames = Lists.newArrayList();

        // 1. 第一步：分拣数据
        for (String tag : publishTags) {
            if (tag.matches("\\d+")) {
                // 如果是纯数字，说明是前端传回来的标签 ID
                tagIdsToRelate.add(Long.valueOf(tag));
            } else {
                // 如果是文本，说明是用户手动输入的“新标签名称”
                needCheckNames.add(tag);
            }
        }

        // 2. 第二步：处理名称类型的标签（查重或新增）
        if (!CollectionUtils.isEmpty(needCheckNames)) {
            // 根据名字查出已存在的标签
            List<TagDO> existedTags = tagMapper.selectByUserIdAndTagNames(needCheckNames, userId);
            Map<String, Long> nameIdMap = existedTags.stream()
                    .collect(Collectors.toMap(t -> t.getName().toLowerCase(), TagDO::getId));

            for (String tagName : needCheckNames) {
                String lowerName = tagName.toLowerCase();
                if (nameIdMap.containsKey(lowerName)) {
                    // 数据库已有同名标签，直接取 ID
                    tagIdsToRelate.add(nameIdMap.get(lowerName));
                } else {
                    // 数据库没有，执行真正的插入
                    TagDO newTag = TagDO.builder()
                            .name(tagName)
                            .userId(userId)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build();
                    tagMapper.insert(newTag);
                    tagIdsToRelate.add(newTag.getId());
                }
            }
        }

        // 3. 第三步：统一插入关联表（记得去重）
        if (!CollectionUtils.isEmpty(tagIdsToRelate)) {
            List<ArticleTagRelDO> rels = tagIdsToRelate.stream()
                    .distinct() // 防止 ID 重复
                    .map(tid -> ArticleTagRelDO.builder()
                            .articleId(articleId)
                            .tagId(tid)
                            .build())
                    .collect(Collectors.toList());
            articleTagRelMapper.insertBatchSomeColumn(rels);
        }
    }
    /**
    删除文章
    * @param deleteArticleReqVO
    * @return
    * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO) {

        Long articleId = deleteArticleReqVO.getId();
        //删除文章
        articleMapper.deleteById(articleId);

        //删除文章内容
        articleContentMapper.deleteByArticleId(articleId);

        //删除文章分类联系
        articleCategoryRelMapper.deleteByArticleId(articleId);

        //删除文章标签联系
        articleTagRelMapper.deleteByArticleId(articleId);
        // 发布文章删除事件
        eventPublisher.publishEvent(new DeleteArticleEvent(this, articleId));
        return Response.success();

    }

/**
*查询文章分页
*
* */
@Override
public PageResponse findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO) {
    // 获取当前登录人的身份信息
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName(); // 拿到登录的用户名

    // 根据用户名从数据库获取完整的用户信息 ---
    UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
            .eq(UserDO::getUsername, username));

    if (userDO == null) {
        throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
    }

    Long loginUserId = userDO.getUserId(); // 当前操作者的 ID

    // -p判断
    Long searchUserId = Objects.equals(loginUserId, 1L) ? null : loginUserId;

    // 获取前端传来的查询参数
    String title = findArticlePageListReqVO.getTitle();
    LocalDate startDate = findArticlePageListReqVO.getStartDate();
    LocalDate endDate = findArticlePageListReqVO.getEndDate();
    Integer type = findArticlePageListReqVO.getType();

    ///获取分页参数
    Long current = findArticlePageListReqVO.getCurrent();
    Long size = findArticlePageListReqVO.getSize();

    Page<ArticleDO> page = new Page<>(current, size);

    //
    Page<ArticleDO> articleDOPage = articleMapper.selectPageList(
            page,               // 第 1 个参数：IPage 对象
            title,              // 第 2 个参数：标题
            startDate,          // 第 3 个参数：开始日期
            endDate,            // 第 4 个参数：结束日期
            type,               // 第 5 个参数：类型
            searchUserId,       // 第 6 个参数：用户 ID
            null                // 第 7 个参数：状态
    );

    // 数据转换 (DO -> VO) ---
    List<ArticleDO> articleDOS = articleDOPage.getRecords();
    List<FindArticlePageListRspVO> vos = null;

    if (!CollectionUtils.isEmpty(articleDOS)) {
        vos = articleDOS.stream()
                .map(articleDO -> FindArticlePageListRspVO.builder()
                        .id(articleDO.getId())
                        .title(articleDO.getTitle())
                        .cover(articleDO.getCover())
                        .createTime(articleDO.getCreateTime())
                        .isTop(articleDO.getWeight() > 0)
                        .userId(articleDO.getUserId().intValue()) // 返回 userId 供前端判断权限
                        .status(articleDO.getStatus()) // 返回 status 显示“私人/公开”标签
                        .build())
                .collect(Collectors.toList());
    }

    return PageResponse.success(articleDOPage, vos);
}
    /**
     * 查询文章详情
     *
     * @param findArticleDetailReqVO
     * @return
     */
    @Override
    public Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long articleId = findArticleDetailReqVO.getId();

        ArticleDO articleDO = articleMapper.selectById(articleId);

        if (Objects.isNull(articleDO)) {
            log.warn("==> 查询的文章不存在，articleId: {}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXISTED);
        }

        ArticleContentDO articleContentDO = articleContentMapper.selectByArticleId(articleId);

        // 所属分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelMapper.selectByArticleId(articleId);

        // 对应标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelMapper.selectByArticleId(articleId);
        // 获取对应标签 ID 集合
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());

        // DO 转 VO
        FindArticleDetailRspVO vo = ArticleDetailConvert.INSTANCE.convertDO2VO(articleDO);

        vo.setContent(articleContentDO.getContent());

        vo.setCategoryId(articleCategoryRelDO.getCategoryId());

        vo.setTagIds(tagIds);


        return Response.success(vo);
    }



    /*
     * 更新文章
     * @param updateArticleReqVO
     * @return
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateArticle(UpdateArticleReqVO updateArticleReqVO) {

        Long articleId = updateArticleReqVO.getId();
        //1. VO 转 Article DO 保存
        ArticleDO articleDO = ArticleDO.builder()
                .id(articleId)
                .title(updateArticleReqVO.getTitle())
                .cover(updateArticleReqVO.getCover())
                .summary(updateArticleReqVO.getSummary())
                .updateTime(LocalDateTime.now())
                .build();
        int count = articleMapper.updateById(articleDO);

        //判断是否更新成功,用来判断文章是否存在
        if(count == 0){
            log.warn("==> 更新文章失败，文章不存在，articleId: {}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXISTED);
        }
        //2 Vo 转 ArticleContentDO 保存
        ArticleContentDO articleContentDO = ArticleContentDO.builder()
                .articleId(articleId)
                .content(updateArticleReqVO.getContent())
                .build();
          articleContentMapper.updateById(articleContentDO);

          //3更新文章分类
        Long categoryId = updateArticleReqVO.getCategoryId();
        //3.1校验是否存在提交的分类
        CategoryDO categoryDO = categoryMapper.selectById(categoryId);
        if(Objects.isNull(categoryDO)){
            log.warn("==> 分类不存在，categoryId: {}", categoryId);
        }
        //存在,先删除该文章的关联分类记录,再插入新的关联关系
        articleCategoryRelMapper.deleteByArticleId(articleId);

        ArticleCategoryRelDO articleCategoryRelDO = ArticleCategoryRelDO.builder()
                .articleId(articleId)
                .categoryId(updateArticleReqVO.getCategoryId())
                .build();
         articleCategoryRelMapper.insert(articleCategoryRelDO);

         //4.保存文章关联的标签集合
        //先删除该文章的关联标签记录,再插入新的关联关系
        articleTagRelMapper.deleteByArticleId(articleId);
        List<String> Tags = updateArticleReqVO.getTags();
        insertTags(articleId, Tags);

        // 发布文章修改事件
        eventPublisher.publishEvent(new UpdateArticleEvent(this, articleId));
        return  Response.success();


    }


    /**
     * 更新文章是否置顶
     *
     * @param updateArticleIsTopReqVO
     * @return
     */
    @Override
    public Response updateArticleIsTop(UpdateArticleIsTopReqVO updateArticleIsTopReqVO) {
        Long articleId = updateArticleIsTopReqVO.getId();
        Boolean isTop = updateArticleIsTopReqVO.getIsTop();

        // 默认权重为 0
        Integer weight = 0;
        // 若设置为置顶
        if (isTop) {
            // 查询出表中最大的权重值
            ArticleDO articleDO = articleMapper.selectMaxWeight();
            Integer maxWeight = articleDO.getWeight();
            // 最大权重值加一
            weight = maxWeight + 1;
        }

        // 更新该篇文章的权重值
        articleMapper.updateById(ArticleDO.builder()
                .id(articleId)
                .weight(weight)
                .build());

        return Response.success();
    }

    /**
     * 文章是否进行公布
     *
     * @param updateArticleStatusReqVO
     * @return
     */
    @Override
    public Response updateArticleStatus(UpdateArticleStatusReqVO updateArticleStatusReqVO) {
        Long articleId = updateArticleStatusReqVO.getId();
        Integer status = updateArticleStatusReqVO.getStatus();

        // 1. 获取当前登录用户 ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDO userDO = userMapper.selectOne(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, authentication.getName()));
        Long loginUserId = userDO.getUserId();

        // 2. 查询文章信息
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (articleDO == null) {
            return Response.fail("该文章不存在");
        }

        // 3. 权限校验：如果不是管理员(1L)，且文章不属于当前用户，则无权修改
        if (!Objects.equals(loginUserId, 1L) && !Objects.equals(articleDO.getUserId(), loginUserId)) {
            return Response.fail("无权操作他人的文章");
        }

        // 4. 更新状态
        articleMapper.update(null, Wrappers.<ArticleDO>lambdaUpdate()
                .set(ArticleDO::getStatus, status)
                .eq(ArticleDO::getId, articleId));

        return Response.success();
    }
}
