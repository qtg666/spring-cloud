package com.qtg.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.qtg.entity.Order;

@Mapper
public interface OrderMapper {
    void insert(Order order);
}
