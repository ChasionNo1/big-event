package com.chasion.eventbackend.mapper;

import com.chasion.eventbackend.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    User findByUsername(String username);

    int insertUser(User user);

    int updateUser(User user);

    int updatePwd(int id, String newPwd, LocalDateTime updateTime);
}
