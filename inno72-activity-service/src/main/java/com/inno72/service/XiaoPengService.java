package com.inno72.service;

import com.inno72.common.BizException;
import com.inno72.vo.Result;
import com.inno72.vo.XiaoPeng;

public interface XiaoPengService {

	public void save(XiaoPeng xiaoPeng) throws BizException;

	public Result<Object> feedBackPolling(String sessionUuid);

}
