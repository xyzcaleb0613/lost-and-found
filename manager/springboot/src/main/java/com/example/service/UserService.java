package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;

import java.util.List;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * 用户业务处理
 **/
@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    public void add(User user) {
        //检验用户名是否存在
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(ObjectUtil.isNotEmpty(dbUser)){
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if(ObjectUtil.isEmpty(user.getPassword())){
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        user.setRole(RoleEnum.USER.name());
        userMapper.insert(user);

    }
    public PageInfo<User> selectPage(User user,Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<User> list=userMapper.selectAll(user);
        return PageInfo.of(list);
    }
    public void update(User user){
        userMapper.updateById(user);
    }
    public void delete(Integer id){
        userMapper.deleteById(id);
    }
    /**
     * 登录
     */
    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }
    public User selectById(Integer id){
        return userMapper.selectById(id);
    }

    public void register(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account, user);
        add(user);
    }
    public void updatePassword(Account account){
        User dbUser = userMapper.selectByUsername(account.getUsername());
        //先校验当前登录的用户合不合法
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }

}
