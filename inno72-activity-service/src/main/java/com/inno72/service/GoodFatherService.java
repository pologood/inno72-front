package com.inno72.service;

import com.inno72.vo.GoodFather;
import com.inno72.vo.Result;

public interface GoodFatherService {
    /**
     * 保存
     * @param goodFather
     * @return
     */
    Result<Object> save(GoodFather goodFather);

    /**
     * 参与抽奖接口
     * @param phone
     * @return
     */
    Result<Object> attend(String phone);

    /**
     * 获取抽奖结果接口
     * @param phone
     * @return
     */
    Result<Object> getlotteryDrawResult(String phone);
}
