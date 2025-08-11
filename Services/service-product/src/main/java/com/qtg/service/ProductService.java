package com.qtg.service;

import com.qtg.entity.Product;
import com.qtg.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public Product getProductById(BigInteger id) {
        return productMapper.findById(id);
    }

    public boolean deductStock(BigInteger id, BigInteger quantity) {
        return productMapper.updateStock(id, quantity) > 0;
    }
}
