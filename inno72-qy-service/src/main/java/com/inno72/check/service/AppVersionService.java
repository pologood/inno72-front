package com.inno72.check.service;

import com.inno72.check.model.Inno72AppVersion;
import com.inno72.common.Result;
import com.inno72.common.Service;

public interface AppVersionService extends Service<Inno72AppVersion> {

    public Result<Inno72AppVersion> findVersion(Inno72AppVersion inno72AppVersion);
}
