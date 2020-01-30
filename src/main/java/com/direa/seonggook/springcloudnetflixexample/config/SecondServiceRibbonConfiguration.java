package com.direa.seonggook.springcloudnetflixexample.config;

import com.netflix.loadbalancer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SecondServiceRibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new RoundRobinRule();
    }

    @Bean
    public IPing ribbonPing() {
        return new DummyPing();
    }
}
