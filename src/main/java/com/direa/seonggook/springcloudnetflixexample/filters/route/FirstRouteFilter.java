package com.direa.seonggook.springcloudnetflixexample.filters.route;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

public class FirstRouteFilter extends ZuulFilter {


    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_FORWARD_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

//        System.out.println(ctx.getRequest().getRequestURI());
//        ResponseEntity<String> responseEntity = this.restTemplate.getForEntity("http://first/", String.class);
//        System.out.println(responseEntity.getBody());
        return null;
    }
}
