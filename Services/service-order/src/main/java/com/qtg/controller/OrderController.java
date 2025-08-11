package com.qtg.controller;


import com.qtg.feign.ProductFeignClient;
import com.qtg.properties.OrderProperties;
//import com.sun.org.apache.xpath.internal.operations.Or;
import jakarta.annotation.PostConstruct;
import com.qtg.entity.Order;
import com.qtg.mapper.OrderMapper;
import com.qtg.vo.OrderVO;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

//@RefreshScope//自动刷新配置
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    DiscoveryClient discoveryClient;
    @Resource
    LoadBalancerClient loadBalancerClient;

    @Autowired
    OrderProperties orderProperties;
//    @Value("${order.timeout}")
//    String orderTimeout;
//    @Value("${order.auto-confirm}")
//    String autoConfirm;
    @Autowired
    ProductFeignClient productFeignClient;

    @GetMapping("/config")//获取配置
    public String getConfig(){
        return "order.timeout=" + orderProperties.getTimeout() + " order.auto-confirm=" + orderProperties.getAutoConfirm();
    }
    @PostConstruct
    public void initRestTemplate() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 使用openFeign进行远程调用
     * （自动实现负载均衡）
     */
    // 1. 调用商品服务扣减库存
    @PostMapping("/ded")
    public ResponseEntity<String> deductProduct(@RequestBody OrderVO request) {

        //服务发现
        //List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
       //System.out.println("成功得到了列表" );
       // ServiceInstance instance = instances.get(0);
        //System.out.println("返回的服务信息：" + instance);
        //String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/api/products/" + request.getProductId() + "/deduct/" + request.getQuantity();
        try {
            //使用openFeign进行远程调用
            ResponseEntity<String> response = productFeignClient.passDeductInfo(request.getProductId(),request.getQuantity());

            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(500).body("库存扣减失败：" + response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("调用商品服务失败：" + e.getMessage());
        }
        return ResponseEntity.ok("库存扣减成功");
    }
    @PostMapping("/balancer")
    public ResponseEntity<String> deductProductBalancer(@RequestBody OrderVO request) {

        //负载均衡
        ServiceInstance choice = loadBalancerClient.choose("service-product");
        //List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        System.out.println("成功得到了列表" );
        //ServiceInstance instance = instances.get(0);
        //System.out.println("返回的服务信息：" + instance);
        String url = "http://" + choice.getHost() + ":" + choice.getPort() + "/api/products/" + request.getProductId() + "/deduct/" + request.getQuantity();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    url, null, String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(500).body("库存扣减失败：" + response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("调用商品服务失败：" + e.getMessage());
        }
        return ResponseEntity.ok("库存扣减成功");
    }

    @PostMapping("/save")
    public ResponseEntity<String> createOrder(@RequestBody Order request) {
        // 2. 保存订单
        Order order = new Order();
        order.setProduct_name(request.getProduct_name());
        order.setId(request.getId());
        order.setOrder_name(request.getOrder_name());
        order.setQuantity(request.getQuantity());
        //order.setCreateTime(LocalDateTime.now());
        orderMapper.insert(order);
        return ResponseEntity.ok("订单创建成功，ID: " + order.getId());
    }


}