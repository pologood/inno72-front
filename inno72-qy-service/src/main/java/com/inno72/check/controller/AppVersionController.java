package com.inno72.check.controller;

import com.inno72.check.model.Inno72AppVersion;
import com.inno72.check.service.AppVersionService;
import com.inno72.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/app/version")
@CrossOrigin
@RestController
public class AppVersionController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private AppVersionService appVersionService;

    /**
     * 获取最新版本号
     * @param inno72AppVersion
     * @return
     */
    @RequestMapping(value = "find",method = RequestMethod.POST)
    public Result<Inno72AppVersion> find(@RequestBody Inno72AppVersion inno72AppVersion){
        return appVersionService.findVersion(inno72AppVersion);
    }
}
