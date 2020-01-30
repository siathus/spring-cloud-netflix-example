package com.direa.seonggook.springcloudnetflixexample;

import com.direa.seonggook.springcloudnetflixexample.config.SecondServiceRibbonConfiguration;
import com.direa.seonggook.springcloudnetflixexample.filters.pre.FirstPreFilter;
import com.direa.seonggook.springcloudnetflixexample.filters.pre.SecondPreFilter;
import com.direa.seonggook.springcloudnetflixexample.filters.route.FirstRouteFilter;
import com.direa.seonggook.springcloudnetflixexample.filters.route.SecondRouteFilter;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.PolledConfigurationSource;
import com.netflix.config.sources.URLConfigurationSource;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@EnableHystrixDashboard
@EnableHystrix
@EnableZuulProxy
@SpringBootApplication
@RibbonClients({
        @RibbonClient(name = "first"),
        @RibbonClient(name = "second", configuration = SecondServiceRibbonConfiguration.class)
})
public class SpringCloudNetflixExampleApplication {

    public static void main(String[] args) {
        for (String str : args) {
            if (str.contains("spring.config.location")) {
                String configLocation = str.split("=")[1];
                System.out.println("spring.config.location : " + configLocation);
                System.setProperty("archaius.configurationSource.additionalUrls", configLocation);
            }
        }

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

//    @Bean
//    public FirstRouteFilter firstRouteFilter() {
//        return new FirstRouteFilter();
//    }

    @Bean
    public SecondRouteFilter secondRouteFilter() {
        return new SecondRouteFilter();
    }

    @Bean
    public AbstractConfiguration addApplicationPropertiesSource() {
        PolledConfigurationSource source = new URLConfigurationSource();
        return new DynamicConfiguration(source, new FixedDelayPollingScheduler(10000, 2000, false));
    }

}

@Component
class ZuulFallbackProvider implements FallbackProvider {
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return response(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                String message = "Fallback : ";
                message = message + this.getStatusCode().toString();
                return new ByteArrayInputStream(message.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }
}
