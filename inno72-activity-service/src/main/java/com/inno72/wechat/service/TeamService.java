package com.inno72.wechat.service;

import com.inno72.vo.Result;
import com.inno72.wechat.vo.CampActivityTimes;
import com.inno72.wechat.vo.CampTask;
import com.inno72.wechat.vo.CampTeam;
import com.inno72.wechat.vo.CampUser;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

public interface TeamService {
    /**
     * 加入阵营
     * @param userId
     * @param teamCode
     * @return
     */
    Result<Object> joinTeam(String userId, Integer teamCode);

    /**
     * 获取用户数据
     * @param userId
     * @return
     */
    Result<Object> getUserInfo(String userId);

    /**
     * 获取我的阵营积分排行
     * @param userId
     * @return
     */
    Result<Object> getUserTeamTopN(String userId);

    /**
     * 获取全部阵营排行
     * @return
     */
    Result<Object> getAllTeamTopN();

    /**
     * 完成任务
     * @param userId
     * @param tashId
     * @return
     */
    Result<Object> finishTash(String userId, String tashId);

    /**
     *兑换快速通道票（工作人员点确定调用）
     * @param userId
     * @return
     */
    Result<Object> useQuickChannel(String userId);

    /**
     * 积分兑换抽奖次数
     * @param userId
     * @return
     */
    Result<Object> changeScore(String userId);

    /**
     * 兑奖（工作人员点）
     * @param userId
     * @return
     */
    Result<Object> useAwards(String userId);

    /**
     * 我的任务
     * @param userId
     * @return
     */
    Result<Object> userTask(String userId);

    /**
     * 积分兑换抽奖记录
     * @param userId
     * @return
     */
    Result<Object> getScoreChangeLog(String userId);

    /**
     * 保存微信用户
     * @param user
     * @return
     */
    String saveUserInfo(WxMpUser user);

    /**
     * 保存活动场次
     * @param times
     * @return
     */
    Result<Object> saveActivityTimes(CampActivityTimes times);

    /**
     * 保存阵营
     * @param team
     * @return
     */
    Result<Object> saveTeam(CampTeam team);

    /**
     * 保存任务
     * @param task
     * @return
     */
    Result<Object> saveTask(CampTask task);

    Result<Object> saveUser(CampUser user);
}
