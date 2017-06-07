package com.neonex.mc.model;

/**
 * Created by dennis on 2017-05-23.
 */
public class AjaxResponseBody {
    String msg;
    MccParameter param;
    ResponseVo result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MccParameter getParam() {
        return param;
    }

    public void setParam(MccParameter param) {
        this.param = param;
    }

    public ResponseVo getResult() {
        return result;
    }

    public void setResult(ResponseVo result) {
        this.result = result;
    }
}
