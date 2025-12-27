package com.zifengliu.weblog.admin.utils;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zifengliu.weblog.common.domain.dos.UserDO;
import com.zifengliu.weblog.common.domain.mapper.UserMapper;
import com.zifengliu.weblog.common.enums.ResponseCodeEnum;
import com.zifengliu.weblog.common.exception.BizException;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * @author 粟英朝
 * @version 0.0.3
 * @date 2025/12/27 上午12:08
 * @description
 **/
/**
 * Security 工具类
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户的用户名
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    /**
     * 获取当前登录用户 ID
     * 注意：这取决于你在登录存入 Authentication 时，Principal 放的是什么对象
     * 如果你自定义了 User 对象包含 ID，可以在这里强转获取
     */
    public static Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return null;
        }

        // 假设你的 UserDetail 实现类里存储了用户 ID
        // 如果 principal 只是 String，你可能需要根据用户名去数据库查 ID（不推荐，效率低）
        // 推荐在登录成功存 Token 时，将 ID 放入 UserDetail 包装类中
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            // 这里需要根据你自己的系统实现来获取 ID
            // 示例：return ((BaseUser) principal).getUserId();
            return 1L; // 暂时兜底，建议根据你的 UserDetail 实际情况修改
        }

        return null;
    }
}