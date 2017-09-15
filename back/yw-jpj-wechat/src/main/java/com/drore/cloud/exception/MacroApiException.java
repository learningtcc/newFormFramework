
package com.drore.cloud.exception;

/***
 * macro接口异常类
 * @since:cloud-ims 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2016/9/14 15:50
 */
public class MacroApiException extends RuntimeException {
    public MacroApiException(){
        super();
    }
    public MacroApiException(String msg){
        super(msg);
    }
    public MacroApiException(String msg, Throwable e){
        super(msg,e);
    }
}
