package com.qtg.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.qtg.entity.Product;

import java.math.BigInteger;

@Mapper
public interface ProductMapper {
    Product findById(BigInteger id);
    int updateStock(@Param("id") BigInteger id, @Param("quantity") BigInteger quantity);
}
