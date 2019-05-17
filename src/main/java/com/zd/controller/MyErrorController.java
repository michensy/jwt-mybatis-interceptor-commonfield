package com.zd.controller;

import com.zd.exception.BusinessException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Zidong
 * @date 2019/4/28 7:12 PM
 */
@Controller
public class MyErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    @ResponseBody
    public void error(HttpServletRequest request){
        // 先处理404异常
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == HttpServletResponse.SC_NOT_FOUND){
            throw new BusinessException("资源未找到");
        }
        Throwable servletException = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (servletException != null) {
            if (servletException instanceof MalformedJwtException) {
                throw new BusinessException("jwt格式不正确");
            }
            servletException.printStackTrace();
            throw new BusinessException(servletException.getMessage());
        }
        Throwable dispatcherServletException = (Throwable) request.getAttribute("org.springframework.web.servlet.DispatcherServlet.EXCEPTION");
        if (dispatcherServletException != null) {
            dispatcherServletException.printStackTrace();
            throw new BusinessException(dispatcherServletException.getMessage());
        }
    }
}
