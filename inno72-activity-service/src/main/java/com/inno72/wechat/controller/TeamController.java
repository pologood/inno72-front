package com.inno72.wechat.controller;

import com.google.gson.Gson;
import com.inno72.common.BizException;
import com.inno72.common.util.AESUtil;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import com.inno72.wechat.service.TeamService;
import com.inno72.wechat.vo.CampActivityTimes;
import com.inno72.wechat.vo.CampTask;
import com.inno72.wechat.vo.CampTeam;
import com.inno72.wechat.vo.CampUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 阵营活动接口
 */
@RestController
@RequestMapping("/team")
public class TeamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeamController.class);
    @Autowired
    private TeamService service;
    @Value("${goodfather.openprize.time}")
    private String date;

    /**
     *
     * @param userId 用户id
     * @param teamCode 阵营编码
     * @return
     */
    @RequestMapping(value = "/joinTeam",method = RequestMethod.POST)
    public Result<Object> joinTeam(@RequestParam String userId, @RequestParam Integer teamCode ) {
        try{
            return service.joinTeam(userId,teamCode);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 根据userid获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserInfo")
    public Result<Object> getUserInfo(@RequestParam String userId ) {
        try{
            return service.getUserInfo(userId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 获取我的阵营积分排行
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserTeamTopN")
    public Result<Object> getUserTeamTopN(@RequestParam String userId ) {
        try{
            return service.getUserTeamTopN(userId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 获取各个阵营排行榜
     * @return
     */
    @RequestMapping(value = "/getAllTeamTopN")
    public Result<Object> getAllTeamTopN() {
        try{
            return service.getAllTeamTopN();
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 完成任务
     * @return
     */
    @RequestMapping(value = "/finishTash",method = RequestMethod.POST)
    public Result<Object> finishTash(@RequestParam String userId ,@RequestParam String taskId) {
        try{
            return service.finishTash(userId,taskId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 兑换快速通道票（工作人员点确定调用）
     * @return
     */
    @RequestMapping(value = "/useQuickChannel",method = RequestMethod.POST)
    public Result<Object> useQuickChannel(@RequestParam String userId) {
        try{
            return service.useQuickChannel(userId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }


    /**
     * 积分兑换抽奖次数
     * @return
     */
    @RequestMapping(value = "/changeScore",method = RequestMethod.POST)
    public Result<Object> changeScore(@RequestParam String userId) {
        try{
            return service.changeScore(userId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 兑奖（工作人员点）
     * @return
     */
    @RequestMapping(value = "/useAwards",method = RequestMethod.POST)
    public Result<Object> useAwards(@RequestParam String userId) {
        try{
            return service.useAwards(userId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 我的任务
     * @return
     */
    @RequestMapping(value = "/userTask")
    public Result<Object> userTask(@RequestParam String userId) {
        try{
            return service.userTask(userId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 积分兑换抽奖记录
     * @return
     */
    @RequestMapping(value = "/getScoreChangeLog")
    public Result<Object> getScoreChangeLog(@RequestParam String userId) {
        try{
            return service.getScoreChangeLog(userId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 保存活动场次
     * @return
     */
    @RequestMapping(value = "/saveActivityTimes")
    public Result<Object> saveActivityTimes(CampActivityTimes times) {
        try{
            return service.saveActivityTimes(times);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    /**
     * 保存阵营
     * @return
     */
    @RequestMapping(value = "/saveTeam")
    public Result<Object> saveActivityTimes(CampTeam team) {
        try{
            return service.saveTeam(team);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }
    /**
     * 积分兑换抽奖记录
     * @return
     */
    @RequestMapping(value = "/saveTask")
    public Result<Object> saveTask(CampTask task) {
        try{
            return service.saveTask(task);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/getProperty")
    public Result<Object> saveTask() {
        return Results.success(date);
    }

    /**
     * 第三方获取用户积分信息 //test
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserScore")
    public String getUserScore(@RequestParam String userId ) {
        Result result = null;
        try{
            LOGGER.info("第三方获取用户积分信息获取加密userId={}",userId);
            //解密
            userId = AESUtil.decrypt(userId);
            LOGGER.info("第三方获取用户积分信息解密后userId={}",userId);
            result = service.getUserInfo(userId);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            result = Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            result = Results.failure("系统异常");
        }
        String json = new Gson().toJson(result);
        //加密json
        String retString = AESUtil.encrypt(json);
        return retString;
    }
    /**
     * 积分兑换抽奖记录
     * @return
     */
    @RequestMapping(value = "/initUser")
    public Result<Object> initUser(CampUser user) {
        try{
            return service.saveUser(user);
        }catch (BizException e){
            LOGGER.error(e.getMessage(),e);
            return Results.failure(e.getMessage());
        }catch (Exception e){
            LOGGER.error("系统异常",e);
            return Results.failure("系统异常");
        }
    }




}
