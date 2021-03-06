package org.activiti.examples.filter;

import io.micrometer.core.instrument.util.StringUtils;
import org.activiti.examples.context.LocalUtil;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 由于系统前后端分离部署，请求会跨域，该过滤器用来解决系统跨域问题
 *
 * @author wangkai
 * @since JDK8
 */
@Component
public class MonitorFilter implements OrderedFilter {

    private final static String HEADER_NAME = "Authorization";
    private final static String Authorization_Head = "Bearer ";

    // 当前Filter的执行顺序，保证在spring security之前执行
    private int order = REQUEST_WRAPPER_FILTER_MAX_ORDER - 104;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //跨域访问支持
        response.setHeader("Access-Control-Allow-Origin", "*");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(200);
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,Authorization");
            response.setHeader("Access-Control-Allow-Methods", "HEAD,GET,PUT,OPTIONS,PATCH,DELETE,POST");
            return;
        }

        //获取当前请求的token，存入当前线程变量
        String header = request.getHeader(HEADER_NAME);
        if (StringUtils.isNotBlank(header) && header.startsWith(Authorization_Head)) {
            LocalUtil.setSession(header.substring(Authorization_Head.length()));
        }

        //设置全局响应格式为json
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf8");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
