package com.chasion.eventbackend.service;

import com.chasion.eventbackend.entity.User;


public interface UserService {

   User findByUsername(String username);
   void register(String username, String password);
   void updateUserInfo(User user);
   void updatePwd(int id, String newPwd);

}
