package com.qtg.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigInteger;

@FeignClient(value = "service-product")
public interface ProductFeignClient {
    //⭐spring mvc注解的两套使用模式（二者相反）
    //⭐1.放在controller上：表示接收这样的请求
    //⭐2.放在FeignClient上：表示发送这样的请求

    //⭐这里FeignClient的写法要与controller的写法一致！！！！！！！！！！！！
    //（指：1.注解部分一致；2.返回类型一致；3.参数一致（不绝对））
    @PostMapping("/api/products/{id}/deduct/{quantity}")
    ResponseEntity<String> passDeductInfo(@PathVariable("id") BigInteger id,
                                          @PathVariable("quantity") BigInteger quantity);

}

