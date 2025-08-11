package com.qtg.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
//无感自动刷新
//配置类
@ConfigurationProperties(prefix = "order")//配置批量绑定在nacos下，无需@RefreshScope就可以实现自动刷新
@Component
public class OrderProperties {
    String timeout;
    String autoConfirm;

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getAutoConfirm() {
        return autoConfirm;
    }

    public void setAutoConfirm(String autoConfirm) {
        this.autoConfirm = autoConfirm;
    }
}
