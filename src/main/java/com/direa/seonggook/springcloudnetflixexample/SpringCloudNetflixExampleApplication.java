package com.direa.seonggook.springcloudnetflixexample;

import com.direa.seonggook.springcloudnetflixexample.filters.pre.FirstPreFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class SpringCloudNetflixExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudNetflixExampleApplication.class, args);
    }

    @Bean
    public FirstPreFilter firstPreFilter() {
        return new FirstPreFilter();
    }
}
