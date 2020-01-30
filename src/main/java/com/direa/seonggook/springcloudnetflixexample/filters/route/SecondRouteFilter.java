package com.direa.seonggook.springcloudnetflixexample.filters.route;

import com.netflix.loadbalancer.*;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import java.util.List;

public class SecondRouteFilter extends ZuulFilter {

    @Autowired
    private SpringClientFactory factory;

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return FilterConstants.RIBBON_ROUTING_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {

        RequestContext ctx = RequestContext.getCurrentContext();
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        System.out.println("========== Route Filter Start ==========");
        String serviceId = (String)ctx.get("serviceId");

        DynamicServerListLoadBalancer lb = (DynamicServerListLoadBalancer)factory.getLoadBalancerContext(serviceId).getLoadBalancer();

        System.out.println("Rule : " + lb.getRule());

        List<Server> serverList = lb.getAllServers();
        for (Server server : serverList) {
            System.out.println(factory.getLoadBalancerContext(serviceId).getServerStats(server).toString());
        }

        System.out.println("========== Route Filter End ==========");
        return null;
    }
}
