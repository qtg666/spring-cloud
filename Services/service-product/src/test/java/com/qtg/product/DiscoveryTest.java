package com.qtg.product;

import com.alibaba.cloud.nacos.discovery.NacosServiceDiscovery;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

@SpringBootTest
public class DiscoveryTest {

    @Resource
    DiscoveryClient discoveryClient;//spring的标准规范，无论用哪个注册中心都可以用的api

    @Autowired
    NacosServiceDiscovery nacosServiceDiscovery;//nacos专用的api

//服务发现
    @Test
    void NacosServiceDiscoveryTest() throws NacosException {
        for(String serviceName : nacosServiceDiscovery.getServices()){
            System.out.println(serviceName);
            //获取ip+port
            List<ServiceInstance> instances = nacosServiceDiscovery.getInstances(serviceName);
            for(ServiceInstance instance : instances){
                System.out.println("ip是：" + instance.getHost() + "port是:" + instance.getPort());
            }
        }
    }
    @Test
    void DiscoveryClientTest() {
        for(String serviceName : discoveryClient.getServices()){
            System.out.println(serviceName);
            //获取ip+port
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            for(ServiceInstance instance : instances){
                System.out.println("ip是：" + instance.getHost() + "port是:" + instance.getPort());
            }
        }
    }
}
