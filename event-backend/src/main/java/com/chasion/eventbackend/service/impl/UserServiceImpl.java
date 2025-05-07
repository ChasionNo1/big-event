package com.chasion.eventbackend.service.impl;

import com.chasion.eventbackend.entity.User;
import com.chasion.eventbackend.mapper.UserMapper;
import com.chasion.eventbackend.service.UserService;
import com.chasion.eventbackend.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public void register(String username, String password) {
//        先对密码进行加密
        String md5String = Md5Util.getMD5String(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(md5String);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insertUser(user);
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public void updatePwd(int id, String newPwd) {
        newPwd = Md5Util.getMD5String(newPwd);
        LocalDateTime updateTime = LocalDateTime.now();
        userMapper.updatePwd(id, newPwd, updateTime);
    }

}
