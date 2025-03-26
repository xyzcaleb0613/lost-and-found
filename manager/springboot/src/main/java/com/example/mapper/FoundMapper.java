package com.example.mapper;

import com.example.entity.Found;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作found相关数据接口
*/
public interface FoundMapper {

    /**
      * 新增
    */
    int insert(Found found);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Found found);

    /**
      * 根据ID查询
    */
    Found selectById(Integer id);

    /**
      * 查询所有
    */
    List<Found> selectAll(Found found);

    @Select("select * from found where status='未找到失主' order by id desc limit 4")
    List<Found> selectNew4();
}