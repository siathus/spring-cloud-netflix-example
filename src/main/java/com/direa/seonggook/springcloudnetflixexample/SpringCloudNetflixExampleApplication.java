package com.direa.seonggook.springcloudnetflixexample;

import com.direa.seonggook.springcloudnetflixexample.config.CustomRibbonConfiguration;
import com.direa.seonggook.springcloudnetflixexample.filters.pre.FirstPreFilter;
import com.direa.seonggook.springcloudnetflixexample.filters.pre.SecondPreFilter;
import com.direa.seonggook.springcloudnetflixexample.filters.route.FirstRouteFilter;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;
import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableZuulProxy
@SpringBootApplication
@RibbonClients({
//        @RibbonClient(name = "first", configuration = CustomRibbonConfiguration.class),
//        @RibbonClient(name = "second", configuration = CustomRibbonConfiguration.class)
        @RibbonClient(name = "first")
})
public class SpringCloudNetflixExampleApplication {

    public static void main(String[] args) {
        System.setProperty("archaius.configurationSource.additionalUrls", "file:///C:/Users/DIR-P-42/Documents/application.properties");

        SpringApplication.run(SpringCloudNetflixExampleApplication.class, args);
    }

    @Bean
    public FirstPreFilter firstPreFilter() {
        return new FirstPreFilter();
    }

    @Bean
    public SecondPreFilter secondPreFilter() {
        return new SecondPreFilter();
    }

    @Bean
    public FirstRouteFilter firstRouteFilter() {
        return new FirstRouteFilter();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AbstractConfiguration addApplicationPropertiesSource() {
        PolledConfigurationSource source = new URLConfigurationSource();
        return new DynamicConfiguration(source, new FixedDelayPollingScheduler(10000, 2000, false));
    }
}
