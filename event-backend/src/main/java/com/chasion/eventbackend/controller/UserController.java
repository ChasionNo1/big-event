package com.chasion.eventbackend.controller;

import com.chasion.eventbackend.entity.Result;
import com.chasion.eventbackend.entity.User;
import com.chasion.eventbackend.service.UserService;
import com.chasion.eventbackend.utils.JwtUtil;
import com.chasion.eventbackend.utils.Md5Util;
import com.chasion.eventbackend.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*", // 或列出具体头部（如 "Authorization", "Content-Type"）
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.PATCH}
)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$")String password) {
//        查询用户是否存在
        User u = userService.findByUsername(username);
        if (u == null) {
            //        注册用户
            userService.register(username, password);
            return Result.success("注册成功");
        }else {
            return Result.error("用户名已被占用");
        }

    }

    // 登录
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        // 根据用户名查询用户，用户是否存在
        User loginUser = userService.findByUsername(username);
        if (loginUser == null) {
//            用户不存在
            return Result.error("用户不存在");
        }else {
//            用户存在，进行校验
            if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
                // 生成token
                HashMap<String, Object> claim = new HashMap<>();
                claim.put("id", loginUser.getId());
                claim.put("username", loginUser.getUsername());
                String token = JwtUtil.genToken(claim);
                return Result.success("登录成功", token);
            }
        }
        return Result.error("密码错误");
    }

    // 获取用户信息
    @GetMapping("/userInfo")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        // 从token中查询用户信息
        Map<String, Object> claims = JwtUtil.parseToken(token);
        String username = (String) claims.get("username");
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(null, user);
    }

    // 更改用户信息
    @PutMapping("/update")
    public Result updateUserInfo(@RequestBody @Validated User user) {
//        更新一下修改时间
        user.setUpdateTime(LocalDateTime.now());
        userService.updateUserInfo(user);
        return Result.success("更改成功");
    }

//     更新用户头像地址
//    一开始设置一个默认的初始头像，后续用户根据需要进行修改
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam("avatarUrl") String avatarUrl) {
        // 根据id，查找修改
        HashMap<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userService.updateAvatarUrl(id, avatarUrl);
        return Result.success("更新成功");
    }

//    修改密码
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, Object> input) {
        String oldPwd = (String) input.get("oldPassword");
        String newPwd = (String) input.get("newPassword");
        String rePwd = (String) input.get("rePassword");
//        先进行校验
        if (StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(rePwd)) {
            return Result.error("输入为空!");
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username = (String) claims.get("username");
        User user = userService.findByUsername(username);
        if (user == null) {
            return Result.error("用户为空");
        }else {
            if (!user.getPassword().equals(Md5Util.getMD5String(oldPwd))){
                return Result.error("用户密码错误");
            }else {
                if (!newPwd.equals(rePwd)) {
                    return Result.error("两次输入密码不一致");
                }else {
                    userService.updatePwd(user.getId(), newPwd);
                }
            }
        }
        return Result.success("修改成功");
    }


}
