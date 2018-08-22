package com.inno72.check.service.impl;


import com.inno72.check.mapper.Inno72AppVersionMapper;
import com.inno72.check.model.Inno72AppVersion;
import com.inno72.check.service.AppVersionService;
import com.inno72.common.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("appVersionService")
public class AppVersionServiceImpl extends AbstractService<Inno72AppVersion> implements AppVersionService {

    @Resource
    private Inno72AppVersionMapper inno72AppVersionMapper;
    @Override
    public Result<Inno72AppVersion> findVersion(Inno72AppVersion inno72AppVersion) {
        if(inno72AppVersion.getAppType() == 0){
            return Results.failure("参数有误");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("appType",inno72AppVersion.getAppType());
        Inno72AppVersion version = inno72AppVersionMapper.selectNowVersion(map);
        if(version != null){
            version.setUrl(CommonConstants.DOWNLOAD_APP_PREF+version.getUrl());
        }
        return ResultGenerator.genSuccessResult(version);
    }
}
