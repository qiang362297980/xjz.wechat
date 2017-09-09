package com.tool;

import java.util.Date;

/**
 * Created by XCA on 2017/4/6.
 */
public class JsonResult<T> {

    public static int NEED_RETRY = 2;
    private int code;
    private String message;
    public final static int FAILD = 2;
    public final static int SUCCESS = 1;

//    private String timestamp = ConcurrentDateUtil.format(new Date());

    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public String getTimestamp() {
//        return timestamp;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
