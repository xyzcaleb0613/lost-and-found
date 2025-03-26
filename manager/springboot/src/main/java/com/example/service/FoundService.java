package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Found;
import com.example.entity.User;
import com.example.mapper.FoundMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 招领信息表业务处理
 **/
@Service
public class FoundService {

    @Resource
    private FoundMapper foundMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 新增
     */
    public void add(Found found) {
        found.setTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        Account currentUser = TokenUtils.getCurrentUser();
        found.setUserId(currentUser.getId());
        foundMapper.insert(found);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        foundMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            foundMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Found found) {
        foundMapper.updateById(found);
    }

    /**
     * 根据ID查询
     */
    public Found selectById(Integer id) {
        return foundMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Found> selectAll(Found found) {
        return foundMapper.selectAll(found);
    }

    /**
     * 分页查询
     */
    public PageInfo<Found> selectPage(Found found, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Found> list = foundMapper.selectAll(found);
        return PageInfo.of(list);
    }

    public List<Found> selectNew4() {
        List<Found> list = foundMapper.selectNew4();
        for (Found found : list) {
            User user = userMapper.selectById(found.getUserId());
            if (ObjectUtil.isNotEmpty(user)) {
                found.setUserName(((User) user).getName());
            }

        }
        return list;
    }
}
