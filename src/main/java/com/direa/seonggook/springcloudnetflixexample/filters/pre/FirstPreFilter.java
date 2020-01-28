package com.direa.seonggook.springcloudnetflixexample.filters.pre;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import javax.servlet.http.HttpServletRequest;

public class FirstPreFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
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
    public Object run() throws ZuulException {
        System.out.println("========== First Pre Filter Run =========");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        System.out.println("Request Method : " + request.getMethod());
        System.out.println("Request URL : " + ctx.getRequest().getRequestURL().toString());

        System.out.println("========== First Pre Filter End ==========");

        return null;
    }
}
