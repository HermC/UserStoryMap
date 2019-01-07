package edu.nju.usm.intercepter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLEncoder;
import edu.nju.usm.model.ResultMap;
import edu.nju.usm.shiro.JwtToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HermC yzy627@126.com
 * @version 1.0
 * @date 2019/01/06
 * @time 22:11
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 如果带有token，则对token进行检查，否则直接通过
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        System.out.println("---- jwt filter ----");
        // 判断请求头是否带上token
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                executeLogin(request, response);
                return true;
            } catch (Exception e) {
                logger.error(e.getMessage());
                responseUnauthorized(response, e.getMessage());
            }
        } else {
            responseUnauthorized(response, "您没有权限访问!");
        }
        return false;
    }

    /**
     * 判断用户是否想要登入。
     * 检测 header 里面是否包含 Authorization 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
        return token != null;
    }

    /**
     * 执行登录操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 拒绝访问
     * */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (req.getHeader("Origin") != null) {
            res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, req.getHeader("Origin"));
        }
        if (req.getHeader("Access-Control-Request-Headers") != null) {
            res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, req.getHeader("Access-Control-Request-Headers"));
        }
        res.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,OPTIONS,PUT,DELETE");

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            res.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

//    private void responseError(ServletResponse response, String message) {
//        try {
//            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//            //设置编码，否则中文字符在重定向时会变为空字符串
//            message = URLEncoder.encode(message, "UTF-8");
//            httpServletResponse.sendRedirect("/unauthorized?msg=" + message);
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }

//    private void responseJson(ServletResponse response, HttpStatus status, String message) {
//        try {
//            PrintWriter out = response.getWriter();
//            out.append(new ResultMap().code(status.value()).fail().message(message));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void responseUnauthorized(ServletResponse response, String message) {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");
        ResultMap result = new ResultMap().code(HttpStatus.UNAUTHORIZED.value())
                .fail()
                .message(message);
        try {
            res.getWriter().write(mapper.writeValueAsString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
