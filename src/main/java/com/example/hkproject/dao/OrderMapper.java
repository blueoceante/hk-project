package com.example.hkproject.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.hkproject.po.OrderTab;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper extends BaseMapper<OrderTab> {
}
