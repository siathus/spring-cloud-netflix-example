package com.direa.seonggook.springcloudnetflixexample.filters.pre;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FirstPreFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (ctx.getRequest().getRequestURI().contains("favicon.ico")) {
            System.out.println("favicon.ico Request Ignored");
            throw new RuntimeException("ignore favicon.ico");
        }
        return true;
    }

    @Override
    public Object run() {
        System.out.println("========== First Pre Filter Run =========");

        DynamicStringProperty tmp = DynamicPropertyFactory.getInstance().getStringProperty("first.ribbon.listOfServers", null);
        System.out.println(tmp.getValue());
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        System.out.println("Request Method : " + request.getMethod());
        System.out.println("Request URL : " + ctx.getRequest().getRequestURL().toString());

        System.out.println(ctx.get("requestURI").toString());
        System.out.println(ctx.get("serviceId").toString());
        System.out.println(ctx.getRouteHost());
        System.out.println("========== First Pre Filter End ==========");

        return null;
    }
}
