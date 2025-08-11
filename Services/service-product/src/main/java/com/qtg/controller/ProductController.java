package com.qtg.controller;

import com.qtg.entity.Product;

import com.qtg.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/{id}/deduct/{quantity}")
    public ResponseEntity<String> deductStock(@PathVariable("id") BigInteger id, @PathVariable("quantity") BigInteger quantity) {
        if (productService.deductStock(id, quantity)) {
            return ResponseEntity.ok("库存扣减成功");
        } else {
            return ResponseEntity.status(400).body("库存不足或商品不存在");
        }
    }

    @GetMapping("/select/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") BigInteger id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}
