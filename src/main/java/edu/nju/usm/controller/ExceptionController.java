package edu.nju.usm.controller;

import edu.nju.usm.model.ResultMap;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理
 *
 * @author HermC yzy627@126.com
 * @version 1.0
 * @date 2019/01/06
 * @time 22:11
 * */
@RestController
@RequestMapping(value = "/unauthorized")
@RestControllerAdvice
public class ExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    /**
     * unauthorized的路由
     * */
    @RequestMapping(value = "/**")
    public ResultMap handleUrl(HttpServletRequest request) {
        return new ResultMap()
                .code(HttpStatus.OK.value())
                .fail()
                .message(request.getParameter("msg") != null ? request.getParameter("msg") : "您没有权限访问!");
    }

    /**
     * 捕获shiro的异常
     * */
    @ExceptionHandler(ShiroException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultMap handle401(Throwable ex) {
        return new ResultMap()
                .code(HttpStatus.UNAUTHORIZED.value())
                .fail()
                .message(ex.getMessage());
    }

    /**
     * 捕捉其他所有的异常
     * */
    @ExceptionHandler(Exception.class)
    public ResultMap handleOthers(HttpServletRequest req, HttpServletResponse res, Throwable ex) {
        logger.error(ex.getMessage());
        res.setStatus(getStatus(req).value());
        return new ResultMap()
                .code(getStatus(req).value())
                .fail()
                .message(ex.getMessage());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
