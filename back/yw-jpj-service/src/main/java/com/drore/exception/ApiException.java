/*
 * Copyright (C) 2016 Zhejiang DRORE Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.drore.com.
 * Developer Web Site: http://open.drore.com.
 */
package com.drore.exception;

/***
 * @since:cloud-gis 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/7/26 13:20
 * api异常
 */
public class ApiException extends RuntimeException{
    public ApiException(){
        super();
    }
    public ApiException(String msg){
        super(msg);
    }
    public ApiException(String msg, Throwable e){
        super(msg,e);
    }
}
