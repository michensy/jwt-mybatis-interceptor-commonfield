package com.zd.exception;

/**
 * @author Zidong
 * @date 2019/4/28 7:19 PM
 */
public class BusinessException extends RuntimeException {
    private String errCode;

    private String errMsg;

    public BusinessException(String errMsg) {
        this.setErrMsg(errMsg);
    }

    public BusinessException(String errCode, String errMsg) {
        this.setErrCode(errCode);
        this.setErrMsg(errMsg);
    }

    public BusinessException(String errCode, String errMsg, Throwable cause) {
        super(cause);
        this.setErrCode(errCode);
        this.setErrMsg(errMsg);
    }

    public BusinessException(String errMsg, Throwable cause) {
        super(cause);
        this.setErrMsg(errMsg);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}