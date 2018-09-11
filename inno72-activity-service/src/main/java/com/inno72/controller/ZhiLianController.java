package com.inno72.controller;

import com.inno72.common.BizException;
import com.inno72.service.ZhiLianService;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zhilian")
public class ZhiLianController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ZhiLianService service;

    @ResponseBody
    @RequestMapping(value = "/getCode", method = {RequestMethod.POST})
    public Result<Object> getCode(String userId) {
        Result result = null;
        try {
            return service.getVerificationCode(userId);
        } catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            result = Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            result = Results.failure("系统异常");
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(value = "/getUserId", method = {RequestMethod.POST})
    public Result<Object> getUserId(String code) {
        Result result = null;
        try {
            return service.getUserId(code);
        } catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            result = Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            result = Results.failure("系统异常");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/finish", method = {RequestMethod.POST})
    public Result<Object> finish(String userId) {
        Result result = null;
        try {
            return service.finish(userId);
        } catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            result = Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            result = Results.failure("系统异常");
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/init", method = {RequestMethod.POST})
    public Result<Object> init() {
        Result result = null;
        try {
            return service.init();
        } catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            result = Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            result = Results.failure("系统异常");
        }
        return result;
    }

}
