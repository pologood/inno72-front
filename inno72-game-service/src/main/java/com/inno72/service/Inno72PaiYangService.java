package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.vo.Inno72SamplingGoods;

import java.util.List;

/**
 * 派样服务接口
 */
public interface Inno72PaiYangService {
    /**
     * 获取首页展示图片
     * @param machineCode
     * @return
     */
    Result<List<Inno72SamplingGoods>> getSampling(String machineCode);
}
