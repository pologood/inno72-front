package com.inno72.common.interceptor;

import com.framelib.exception.SystemException;
import com.inno72.common.Result;
import com.inno72.common.StatusConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Result handleServiceException(SystemException ex) {
        logger.error(ex.getMessage(), ex);
        int retCode = StatusConstants.RETURN_CODE_FAIL;
        String msg = "";
        if (ex instanceof SystemException) {
            msg = (((SystemException) ex).getMsgId());
        } else {
            msg = "系统错误";
        }
        Result result = new Result();
        result.setCode(retCode);
        result.setMsg(msg);
        return result;
    }
}
