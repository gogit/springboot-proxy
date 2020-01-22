package com.thinktag.persist.controller;

import com.thinktag.persist.service.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ProxyFilter implements Filter {

    @Autowired
    ProxyService proxyService;

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain)
            throws IOException, ServletException {

        System.out.println("Remote Host:"+request.getRemoteHost());
        System.out.println("Remote Address:"+request.getRemoteAddr());
        //filterchain.doFilter(request, response);
        HttpServletRequest hr =(HttpServletRequest)request;
        String destinationUrl = hr.getHeader("X-DEST");
        String method = hr.getMethod();
        switch(method) {
            case "GET":
                proxyService.processGet(destinationUrl, hr, (HttpServletResponse) response);
                break;
            case "POST":
                proxyService.processPost(destinationUrl, hr, (HttpServletResponse) response);
                break;
        }

    }

    @Override
    public void init(FilterConfig filterconfig) throws ServletException {}

}
