package com.zd.exception;

import com.alibaba.fastjson.JSONException;
import com.zd.constant.ErrorStatusEnum;
import com.sf.pg.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 *
 * @author lijie.zh
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     *
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Object businessExceptionHandler(BusinessException exception) {
        Throwable throwable = findRootExceptionThrowable(exception);

        if (throwable != null && !(throwable instanceof BusinessException)) {
            log.debug("业务异常: {}, {}", exception.getErrMsg(), exception);
        } else {
            log.debug("业务异常: {}, {}", exception.getErrMsg(), exception.getStackTrace().length > 0 ? exception.getStackTrace()[0] : "");
        }

        return R.error(exception.getErrMsg(), StringUtils.isBlank(exception.getErrCode()) ? ErrorStatusEnum.BUSINESS_ERROR.getValue() : exception.getErrCode());
    }

    private Throwable findRootExceptionThrowable(Exception e) {
        Object rootCause;
        for(rootCause = e; ((Throwable)rootCause).getCause() != null; rootCause = ((Throwable)rootCause).getCause()) {
            ;
        }
        return (Throwable)rootCause;
    }


    @ExceptionHandler(value = {JSONException.class, HttpMessageNotReadableException.class})
    public Object jsonError() {
        return R.error("请传递正确的JSON格式参数");
    }

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    public Object httpMediaTypeNotSupportedError(HttpMediaTypeNotSupportedException e) {
        return R.error("不支持当前content-type");
    }
}