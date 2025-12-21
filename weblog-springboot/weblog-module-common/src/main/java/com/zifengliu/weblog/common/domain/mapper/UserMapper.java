package com.zifengliu.weblog.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zifengliu.weblog.common.domain.dos.UserDO;

import java.time.LocalDateTime;

/**
 * @author 自风流
 * @version 0.0.3
 * @description UserMapper
 * @date 2025/3/3 下午7:07
 * @description 用户
 **/
    public interface UserMapper extends BaseMapper<UserDO> {

        default UserDO findByUsername(String username) {
            LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserDO::getUsername, username);
            return selectOne(wrapper);
        }

        //修改密码
        default int updatePasswordByUsername(String username, String password) {
            LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();

            //设置要更新的字段
            wrapper.set(UserDO::getPassword,password);
            wrapper.set(UserDO::getUpdateTime, LocalDateTime.now());

            //更新条件
            wrapper.eq(UserDO::getUsername, username);
            return  update(null, wrapper);
        }
    }

