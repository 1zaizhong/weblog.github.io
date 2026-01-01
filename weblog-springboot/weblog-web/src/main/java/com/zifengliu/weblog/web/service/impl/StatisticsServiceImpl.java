package com.zifengliu.weblog.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.*;
import com.zifengliu.weblog.common.domain.mapper.*;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import com.zifengliu.weblog.common.utils.Response;
import com.zifengliu.weblog.web.model.vo.author.FindAuthorDashboardReqVO;
import com.zifengliu.weblog.web.model.vo.author.FindAuthorDashboardRspVO;
import com.zifengliu.weblog.web.model.vo.statistics.FindAuthorInfoByArticleIdRspVO;
import com.zifengliu.weblog.web.model.vo.statistics.FindStatisticsInfoRspVO;
import com.zifengliu.weblog.web.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/16 下午2:42
 * @description
 **/

@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogSettingsMapper blogSettingsMapper;
    @Autowired
    private UserFollowMapper userFollowMapper;

    /**
     * 获取文章总数、分类总数、标签总数、总访问量统计信息
     *
     * @return
     */
    @Override
    public Response findInfo(Long userId) {
        // 是否为管理员 (ID 为 1)
        boolean isAdmin = Objects.equals(userId, 1L);

        // 查询文章总数
        // 如果是管理员，userId 传 null，MyBatis Plus 的 .eq(Objects.nonNull(userId),
        Long articleCount = articleMapper.selectCount(Wrappers.<ArticleDO>lambdaQuery()
                .eq(!isAdmin, ArticleDO::getUserId, userId));

        // 查询分类总数
        Long categoryCount = categoryMapper.selectCount(Wrappers.<CategoryDO>lambdaQuery()
                .eq(!isAdmin, CategoryDO::getUserId, userId));

        // 查询标签总数
        Long tagCount = tagMapper.selectCount(Wrappers.<TagDO>lambdaQuery()
                .eq(!isAdmin, TagDO::getUserId, userId));

        // 总浏览量
        Long pvTotalCount = articleMapper.selectSumPvByUserId(isAdmin ? null : userId);




        // 组装 VO 类
        FindStatisticsInfoRspVO vo = FindStatisticsInfoRspVO.builder()
                .articleTotalCount(articleCount)
                .categoryTotalCount(categoryCount)
                .tagTotalCount(tagCount)
                .pvTotalCount(pvTotalCount)
                .build();;

        return Response.success(vo);
    }
    /**
     * 通过文章Id来获取文章总数、分类总数、标签总数、总访问量统计信息
     *
     * @return
     */
    @Override
    public Response findAuthorInfoByArticleId(Long articleId) {
        // 1. 获取作者 ID
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if (Objects.isNull(articleDO))
            throw  new BizException(ResponseCodeEnum.ARTICLE_NOT_EXISTED);

        Long authorUserId = articleDO.getUserId();

        // 获取博主设置信息
        LambdaQueryWrapper<BlogSettingsDO> settingsWrapper = new LambdaQueryWrapper<>();
        settingsWrapper.eq(BlogSettingsDO::getUserId, authorUserId);
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectOne(settingsWrapper);
        if (Objects.isNull(blogSettingsDO)) {
            blogSettingsDO = blogSettingsMapper.selectById(1L);
        }

        // 获取基础统计 (文章数、分类、标签、PV)
        FindStatisticsInfoRspVO stats = (FindStatisticsInfoRspVO) findInfo(authorUserId).getData();

        //  获取粉丝数 调用UserFollowService 方法
        Long fansCount = userFollowMapper.selectCount(Wrappers.<UserFollowDO>lambdaQuery()
                .eq(UserFollowDO::getFollowingUserId, authorUserId));
        //  组装终极 VO
        FindAuthorInfoByArticleIdRspVO vo = FindAuthorInfoByArticleIdRspVO.builder()
                .author(blogSettingsDO.getAuthor())
                .avatar(blogSettingsDO.getAvatar())
                .introduction(blogSettingsDO.getIntroduction())
                .articleTotalCount(stats.getArticleTotalCount())
                .categoryTotalCount(stats.getCategoryTotalCount())
                .tagTotalCount(stats.getTagTotalCount())
                .pvTotalCount(stats.getPvTotalCount())
                .fansTotalCount(fansCount) // 设置粉丝数
                .build();

        return Response.success(vo);
    }

    /**
     * 获取博主主页看板数据 (通过博主 ID)
     */
    @Override
    public Response findAuthorDashboardData(FindAuthorDashboardReqVO reqVO) {
        // 1. 获取传入的博主 ID
        Long authorUserId = reqVO.getAuthorId();
        if (Objects.isNull(authorUserId)) {
            return Response.fail("博主ID不能为空");
        }

        // 2. 获取博主设置信息 (头像、昵称、简介)
        LambdaQueryWrapper<BlogSettingsDO> settingsWrapper = new LambdaQueryWrapper<>();
        settingsWrapper.eq(BlogSettingsDO::getUserId, authorUserId);
        BlogSettingsDO blogSettingsDO = blogSettingsMapper.selectOne(settingsWrapper);

        // 如果该用户没设置，尝试取 ID 为 1 的默认设置（兼容你之前的逻辑）
        if (Objects.isNull(blogSettingsDO)) {
            blogSettingsDO = blogSettingsMapper.selectById(1L);
        }

        if (Objects.isNull(blogSettingsDO)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID); // 或者自定义异常
        }

        // 3. 复用现有的统计逻辑 (获取 文章数、分类数、标签数、总访问量 PV)
        // 注意：确保你的 findInfo(Long userId) 方法是支持传入用户 ID 进行过滤统计的
        Response infoRes = findInfo(authorUserId);
        if (!infoRes.isSuccess() || Objects.isNull(infoRes.getData())) {
            return Response.fail("获取博主统计数据失败");
        }
        // 强制转换为你的统计 VO
        FindStatisticsInfoRspVO stats = (FindStatisticsInfoRspVO) infoRes.getData();

        // 4. 获取粉丝数 (从关注表统计)
        Long fansCount = userFollowMapper.selectCount(Wrappers.<UserFollowDO>lambdaQuery()
                .eq(UserFollowDO::getFollowingUserId, authorUserId));

        // 5. 组装响应 VO (FindAuthorDashboardRspVO)
        FindAuthorDashboardRspVO vo = FindAuthorDashboardRspVO.builder()
                .author(blogSettingsDO.getAuthor())
                .avatar(blogSettingsDO.getAvatar())
                .introduction(blogSettingsDO.getIntroduction())
                .articleTotalCount(stats.getArticleTotalCount())
                .categoryTotalCount(stats.getCategoryTotalCount())
                .tagTotalCount(stats.getTagTotalCount())
                .pvTotalCount(stats.getPvTotalCount())
                .fansTotalCount(fansCount)
                .build();

        return Response.success(vo);
    }
}
