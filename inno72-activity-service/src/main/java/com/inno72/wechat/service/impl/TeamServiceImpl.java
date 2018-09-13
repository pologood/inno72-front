package com.inno72.wechat.service.impl;

import com.google.gson.Gson;
import com.inno72.common.BizException;
import com.inno72.mongo.MongoUtil;
import com.inno72.service.GoodFatherService;
import com.inno72.vo.Result;
import com.inno72.vo.Results;
import com.inno72.wechat.service.TeamService;
import com.inno72.wechat.vo.*;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodFatherService.class);
    @Autowired
    private MongoUtil mongoUtil;

    /**
     * 力量种族锁
     */
    private Object powerLock = new Object();
    /**
     * 智慧种族锁
     */
    private Object wisdomLock = new Object();
    /**
     * 灵力种族锁
     */
    private Object manaLock = new Object();

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String TEAM_KEY_PRE = "team:";
    @Value("${team.hitnum}")
    private Integer hitNum;

    private Integer findTimesCode() {
        CampActivityTimes times = findCampActivityTimes();
        if(times!=null) return times.getTimesCode();
        LOGGER.info("查询游戏场次为空，date={}", System.currentTimeMillis());
        return null;
    }

    private Integer findTimesCodeWithException() {
        Integer timesCode = findTimesCode();
        if(timesCode==null) {
            throw new BizException("游戏未开始");
        }
        return timesCode;
    }

    @Override
    public Result<Object> joinTeam(String userId, Integer teamCode) {
        //查询目前场次
        Integer timesCode = findTimesCodeWithException();

        //查看是否已经加入阵营
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId))
                .addCriteria(Criteria.where("teamCode").is(teamCode))
                .addCriteria(Criteria.where("timesCode").is(timesCode));
        CampUserTeam cut = mongoUtil.findOne(query,CampUserTeam.class);

        //没加入阵营，插入数据库加入阵营
        if(cut==null){
            LOGGER.info("用户未加入此阵营，执行保存。userId={},teamCode={}",userId,teamCode);
            cut = new CampUserTeam();
            cut.setAwardsSize(0);
            cut.setCreateTime(new Date());
            cut.setScore(0);
            cut.setTeamCode(teamCode);
            cut.setTimesCode(timesCode);
            cut.setUserId(userId);
            //获取昵称
            String nickName = findNickName(userId);
            cut.setNickName(nickName);
            mongoUtil.insert(cut);
        }else{
            LOGGER.info("用户已经加入过此阵营，不执行保存。userId={},teamCode={}",userId,teamCode);
        }
        return Results.success();
    }

    private String findNickName(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        CampUser cu = mongoUtil.findOne(query,CampUser.class);
        if(cu==null){
            LOGGER.info("查询nickName为空，userId={}",userId);
            return null;
        }
        return cu.getNickName();
    }

    @Override
    public Result<Object> getUserInfo(String userId) {
        LOGGER.info("获取用户数据，userId={}",userId);
        //查询目前场次
        Integer timesCode = findTimesCode();
        UserInfoVo userInfo = new UserInfoVo();
        //获取昵称
        CampUser user = findUser(userId);
        if(user == null) throw new BizException("用户不存在");
        userInfo.setNickName(user.getNickName());
        if(timesCode == null){
            userInfo.setGameFlag(UserInfoVo.GAMEFLAG_UNSTARTED);
            userInfo.setScore(0);
            userInfo.setTeamCode(0);
        }else{
            userInfo.setGameFlag(UserInfoVo.GAMEFLAG_STARTED);
            //查找用户信息
            CampUserTeam userTeam = findTeamUser(userId,timesCode);
            if(userTeam!=null){
                userInfo.setScore(userTeam.getScore());
                userInfo.setTeamCode(userTeam.getTeamCode());
            }else{
                userInfo.setScore(0);
                userInfo.setTeamCode(0);
            }
        }
        /**
         * 查找快速通道票个数
         */
        Integer quickChannelSize = findQuickChannel(userId);
        userInfo.setQuickChannelSize(quickChannelSize);
        return Results.success(userInfo);
    }

    private CampUser findUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userId));
        return mongoUtil.findOne(query,CampUser.class);
    }

    @Override
    public Result<Object> getUserTeamTopN(String userId) {
        LOGGER.info("获取我的阵营积分排行userId={}",userId);
        UserTeamTopNVo userTeamTopNVo = new UserTeamTopNVo();

        //获取我的阵营信息
        CampUserTeam campUserTeam = findTeamUser(userId);
        if(campUserTeam == null){
            throw new BizException("请加入阵营");
        }

        //获取我的排名
        Integer ranking = getUserRanking(userId,campUserTeam);
        userTeamTopNVo.setRanking(ranking);

        //获取前五名
        List<TopNVo> list = getTeamTopN(campUserTeam.getTeamCode());
        userTeamTopNVo.setList(list);
        return Results.success(userTeamTopNVo);
    }

    @Override
    public Result<Object> getAllTeamTopN() {
        LOGGER.info("获取各个阵营排行榜");
        CampActivityTimes times = findCampActivityTimes();
        if(times == null) throw new BizException("游戏未开始");

        Integer timesCode = times.getTimesCode();
        Map<String,Object> map = new HashMap<>();
        map.put("nowTime",System.currentTimeMillis());
        map.put("finishTime",times.getEndTime());
        //获取本场次排名
        List<TopNVo> list = getAllTeamTopN(timesCode);
        map.put("list",list);
        return Results.success(map);
    }

    private CampActivityTimes findCampActivityTimes() {
        Date now = new Date();
        Query query = new Query();
        query.addCriteria(Criteria.where("startTime").lt(now.getTime())).addCriteria(Criteria.where("endTime").gt(now.getTime()));
        CampActivityTimes times = mongoUtil.findOne(query,CampActivityTimes.class);
        return times;
    }

    @Override
    public Result<Object> finishTash(String userId, String tashId) {
        LOGGER.info("完成任务userId={},taskId={}",userId,tashId);
        //查询此task
        CampTask task = findTask(tashId);
        if(task == null){
            throw new BizException("参数错误");
        }
        CampUserTeam userTeam = findTeamUser(userId);
        if(userTeam == null) throw new BizException("请加入阵营");
        //查看是否做过此任务
        Integer timesCode = findTimesCodeWithException();
        CampUserTask userTask = findUserTask(userId,tashId,timesCode);
        if(userTask != null){
            return Results.warn("您已经积过这个分了",3);
        }
        //没做过此任务插入任务记录表
        userTask = new CampUserTask();
        if(userTeam.getTeamCode() == task.getTeamCode() && (task.getType()== CampTask.TYPE_BIG || task.getType()== CampTask.TYPE_MID) ){
            userTask.setMainFlag(CampUserTask.MAINFLAG_MAIN);
        }else{
            userTask.setMainFlag(CampUserTask.MAINFLAG_NOT_MAIN);
        }
        userTask.setCreateTime(new Date());
        userTask.setTaskId(tashId);
        userTask.setTimesCode(timesCode);
        userTask.setUserId(userId);
        userTask.setTeamCode(userTeam.getTeamCode());
        saveUserTask(userTask);

        //计算分数
        Integer score = task.getScore();
        Integer teamCode = task.getTeamCode();
        Integer type = task.getType();
        Integer multScore = 0;
        boolean ifCanQuick = false;//是否可以抽快速通道票
//        Integer mainTaskFlag = getMainTaskFlag(task,userTeam.getTeamCode());
        if(teamCode == userTeam.getTeamCode()){
            //同种族任务
            if(type == CampTask.TYPE_BIG){
                //同种族的大展位可以抽奖
                ifCanQuick = true;
            }
            multScore = score*2;
        }else{
            multScore = score;
        }

        //更新个人分数
        Integer userScore = userTeam.getScore();
        if(userScore == null) userScore = 0;
        userScore+= multScore;
        updateUserScore(userScore,userTeam.getId());

        //更新种族分数
        updateTeamScore(userTeam.getTeamCode(),multScore,timesCode);
        Integer date = getTodayDate();
        //抽快速通道票
        if(ifCanQuick){
            boolean ifHit = drawQuickChannel(userTeam.getId(),userTeam.getTimesCode(),userTeam.getTeamCode(),date);
            if(ifHit){
                //如果中奖
                //此处不用枷锁
                //更新redis缓存
                redisTemplate.opsForValue().set(TEAM_KEY_PRE+date+":"+userTeam.getTeamCode(),"1");

                //插入数据
                CampQuickChannel quickChannel = new CampQuickChannel();
                quickChannel.setDate(date);
                quickChannel.setQuickChannelSize(1);
                quickChannel.setUserId(userId);
                saveQuickChannel(quickChannel);
                return Results.warn("获得快速通道票",2,multScore);
            }
        }
        return Results.success(multScore);
    }

    @Override
    public Result<Object> useQuickChannel(String userId) {
        //查询快速通道票
        CampQuickChannel quickChannel = findQuickChannelObj(userId);
        if(quickChannel == null || quickChannel.getQuickChannelSize()==null || quickChannel.getQuickChannelSize()==0){
            return Results.warn("没有快速通道票",2);
        }
        //更新快速通道票个数
        CampQuickChannel param = new CampQuickChannel();
        param.setId(quickChannel.getId());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("quickChannelSize",0);
        mongoUtil.update(param,map,CampQuickChannel.class);
        //插入兑换日志
        CampQuickChannelChangeLog log = new CampQuickChannelChangeLog();
        log.setCreateTime(new Date());
        log.setQuickChannelId(quickChannel.getId());
        log.setUserId(userId);
        mongoUtil.save(log);
        return Results.success();
    }

    @Override
    public Result<Object> changeScore(String userId) {
        LOGGER.info("积分兑换抽奖次数userId={}",userId);
        Integer timesCode = findTimesCodeWithException();
        CampUserTeam userTeam = findTeamUser(userId,timesCode);
        if(userTeam == null) return Results.failure("请先加入阵营");
        Integer teamCode = userTeam.getTeamCode();

        //校验是否做过主线任务
        checkDownMainTask(userId,timesCode,teamCode);

        Integer score = userTeam.getScore();
        if(score == null) score=0;
        Integer size = userTeam.getAwardsSize();
        if(size == null) size=0;
        int awardsSize = score/155;
        int leftScore = score%155;
        int leftAwardSize = size + awardsSize;
        if(awardsSize == 0){
            return Results.warn("积分不够无法兑换",2);
        }

        //更新数据
        CampUserTeam param = new CampUserTeam();
        param.setId(userTeam.getId());
        Map<String,Object> map = new HashMap<String,Object>(2);
        map.put("score",leftScore);
        map.put("awardsSize",leftAwardSize);
        mongoUtil.update(param,map,CampUserTeam.class);

        //插入兑换日志
        CampScoreChangeLog log = new CampScoreChangeLog();
        log.setTimesCode(timesCode);
        log.setCreateTime(new Date());
        log.setStartScore(userTeam.getScore());
        log.setStartAwardsSize(userTeam.getAwardsSize());
        log.setEndScore(leftScore);
        log.setEndAwardsSize(leftAwardSize);
        log.setUserId(userId);
        mongoUtil.save(log);
        return Results.success(awardsSize);
    }

    private void checkDownMainTask(String userId, Integer timesCode, Integer teamCode) {
        //查询主线任务个数
        long mainTaskCount = findMainTask(teamCode);
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId))
                .addCriteria(Criteria.where("timesCode").is(timesCode))
                .addCriteria(Criteria.where("teamCode").is(teamCode))
                .addCriteria(Criteria.where("mainFlag").is(CampUserTask.MAINFLAG_MAIN));
        long count = mongoUtil.count(query,CampUserTask.class);
        if(count < mainTaskCount) throw new BizException("完成主线任务才能兑换积分");
    }

    /**
     * 查找阵营主线任务个数
     * @param teamCode
     * @return
     */
    private long findMainTask(Integer teamCode) {
        Query query = new Query();
        Criteria  c = new Criteria();
        c.orOperator(Criteria.where("type").is(CampTask.TYPE_BIG),Criteria.where("type").is(CampTask.TYPE_MID));
        query.addCriteria(Criteria.where("teamCode").is(teamCode))
                .addCriteria(c);
        long count = mongoUtil.count(query,CampTask.class);
        return count;
    }

    @Override
    public Result<Object> useAwards(String userId) {
        CampUserTeam userTeam = findTeamUser(userId);
        if(userTeam == null||userTeam.getAwardsSize()==null||userTeam.getAwardsSize()==0){
            return Results.failure("没有兑奖次数");
        }
        //更新数据库
        CampUserTeam param = new CampUserTeam();
        param.setId(userTeam.getId());
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("awardsSize",0);
        mongoUtil.update(param,map,CampUserTeam.class);

        //插入日志
        CampAwardsChangeLog log = new CampAwardsChangeLog();
        log.setAwardsSize(userTeam.getAwardsSize());
        log.setCreateTime(new Date());
        log.setTimesCode(findTimesCode());
        log.setUserId(userId);
        mongoUtil.save(log);
        return Results.success();
    }

    @Override
    public Result<Object> userTask(String userId) {
        LOGGER.info("获取我的任务userId={}",userId);
        Integer timesCode = findTimesCodeWithException();
        CampUserTeam userTeam = findTeamUser(userId,timesCode);
        if(userTeam == null) throw new BizException("请选择阵营");

        //查询本阵营任务
        Query query = new Query();
        query.addCriteria(Criteria.where("teamCode")
                .is(userTeam.getTeamCode()))
                .with(new Sort(new Sort.Order(Sort.Direction.ASC,"type"),new Sort.Order(Sort.Direction.DESC,"score")));
        List<CampTask> list = mongoUtil.find(query,CampTask.class);
        if(list == null || list.size()==0){
            LOGGER.error("无法找到任务teamCode={}",userTeam.getTeamCode());
            throw new BizException("数据错误");
        }
        for(CampTask task : list){
            //本阵营任务双倍积分
            task.setScore(task.getScore()*2);
            if(task.getType()==CampTask.TYPE_BIG||task.getType()==CampTask.TYPE_MID){
                task.setMainFlag(CampTask.MAINFLAG_MAIN);
            }else{
                task.setMainFlag(CampTask.MAINFLAG_NOT_MAIN);
            }
        }

        //查询其他阵营任务
        query = new Query();
        query.addCriteria(Criteria.where("teamCode")
                .ne(userTeam.getTeamCode()))
                .with(new Sort(new Sort.Order(Sort.Direction.ASC,"teamCode"),new Sort.Order(Sort.Direction.ASC,"type"),new Sort.Order(Sort.Direction.DESC,"score")));
        List<CampTask> otherlist = mongoUtil.find(query,CampTask.class);
        if(list == null || list.size()==0){
            LOGGER.error("无法找到其他阵营任务teamCode={}",userTeam.getTeamCode());
            throw new BizException("数据错误");
        }
        list.addAll(otherlist);

        //剔除已经做过的任务
        //查询做过的任务
        query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId))
                .addCriteria(Criteria.where("timesCode").is(timesCode));
        List<CampUserTask> userTaskList = mongoUtil.find(query,CampUserTask.class);
        if(userTaskList!=null&&userTaskList.size()>0){
            Set<String> taskIds = new HashSet<String>(userTaskList.size());
            for(CampUserTask task : userTaskList){
                taskIds.add(task.getTaskId());
            }

            List<CampTask> downTask = new ArrayList<CampTask>(userTaskList.size());
            Iterator<CampTask> it = list.iterator();
            while(it.hasNext()){
                CampTask task = it.next();
                task.setFinishFlag(CampTask.FINISHFLAG_UNFINISH);
                if(taskIds.contains(task.getId())){
                    it.remove();
                    task.setFinishFlag(CampTask.FINISHFLAG_FINISH);
                    downTask.add(task);
                }
            }
            list.addAll(downTask);
        }
        return Results.success(list);
    }

    @Override
    public Result<Object> getScoreChangeLog(String userId) {

        Integer timesCode = findTimesCodeWithException();
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId)).addCriteria(Criteria.where("timesCode").is(timesCode))
                .with(new Sort(new Sort.Order(Sort.Direction.DESC,"createTime")));
        List<CampScoreChangeLog> list = mongoUtil.find(query,CampScoreChangeLog.class);
        return Results.success(list);
    }

    @Override
    public String saveUserInfo(WxMpUser user) {
        LOGGER.info("保存用户信息：userjson:{}",new Gson().toJson(user));
        String openid = user.getOpenId();
        //查询看是否保存过；
        Query query = new Query();
        query.addCriteria(Criteria.where("openid").is(openid));
        CampUser campUser = mongoUtil.findOne(query,CampUser.class);
        if(campUser==null){
            campUser = new CampUser();
            campUser.setNickName(user.getNickname());
            campUser.setOpenid(user.getOpenId());
            campUser.setCity(user.getCity());
            campUser.setProvince(user.getProvince());
            campUser.setRemark(user.getRemark());
            campUser.setSex(user.getSex());
            campUser.setCountry(user.getCountry());
            campUser.setGroupId(user.getGroupId());
            campUser.setHeadImgUrl(user.getHeadImgUrl());
            campUser.setLanguage(user.getLanguage());
            campUser.setSexId(user.getSexId());
            campUser.setSubscribe(user.getSubscribe());
            campUser.setSubscribeTime(user.getSubscribeTime());
            campUser.setTagIds(user.getTagIds());
            campUser.setUnionId(user.getUnionId());
            mongoUtil.save(campUser);
        }
        return campUser.getId();
    }

    @Override
    public Result<Object> saveActivityTimes(CampActivityTimes times) {
        mongoUtil.save(times);
        return Results.success();
    }

    @Override
    public Result<Object> saveTeam(CampTeam team) {
        mongoUtil.save(team);
        return Results.success();
    }

    @Override
    public Result<Object> saveTask(CampTask task) {
        mongoUtil.save(task);
        return Results.success();
    }

    /**
     * 获取今天是本月几号
     * @return
     */
    private Integer getTodayDate() {
        Calendar c = Calendar.getInstance();
        Integer date = c.get(Calendar.DAY_OF_MONTH);
        return date;
    }

    /**
     * 查询用户任务
     * @param userId
     * @param tashId
     * @param timesCode
     * @return
     */
    private CampUserTask findUserTask(String userId, String tashId, Integer timesCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId))
                .addCriteria(Criteria.where("taskId").is(tashId))
                .addCriteria(Criteria.where("timesCode").is(timesCode));
        return mongoUtil.findOne(query,CampUserTask.class);
    }

    /**
     * 抽奖部分代码
     * @param userTeamId
     * @param timesCode
     * @param teamCode
     * @param date
     * @return
     */
    private boolean drawQuickChannel(String userTeamId, Integer timesCode, Integer teamCode, Integer date) {
        LOGGER.info("抽快速通道票userTeamId={},timesCode={},teamCode,date={}",userTeamId,timesCode,teamCode,date);
        Boolean haskey = redisTemplate.hasKey(TEAM_KEY_PRE+date+":"+teamCode);
        if(!haskey){
            //没人得奖
            //查询第五千个人和自己的id对比
            Query query = new Query();
            query.addCriteria(Criteria.where("timesCode").is(timesCode)).addCriteria(Criteria.where("teamCode").is(teamCode))
            .with(new Sort(new Sort.Order(Sort.Direction.ASC,"createTime")))
            .skip(hitNum-1).limit(1);
            CampUserTeam userTeam = mongoUtil.findOne(query,CampUserTeam.class);
            if(userTeam!=null&&userTeam.getId().equalsIgnoreCase(userTeamId)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param quickChannel
     */
    private void saveQuickChannel(CampQuickChannel quickChannel) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(quickChannel.getUserId()))
                .addCriteria(Criteria.where("date").is(quickChannel.getDate()));
        CampQuickChannel channel = mongoUtil.findOne(query,CampQuickChannel.class);
        if(channel==null){
            mongoUtil.save(quickChannel);
        }
    }

    /**
     * 更新种族总分数
     * @param teamCode
     * @param multScore
     */
    private void updateTeamScore(Integer teamCode, Integer multScore,Integer timesCode) {
        LOGGER.info("更新种族总分数teamCode={},multScore={},timesCode={}",teamCode,multScore,timesCode);
        Object lock = null;
        if(teamCode == CampTeam.TEAMCODE_POWER){
            lock = powerLock;
        }else if(teamCode == CampTeam.TEAMCODE_WISDOM){
            lock = wisdomLock;
        }else if(teamCode == CampTeam.TEAMCODE_MANA){
            lock = manaLock;
        }
        synchronized (lock){
            Query query = new Query();
            query.addCriteria(Criteria.where("timesCode").is(timesCode))
                    .addCriteria(Criteria.where("teamCode").is(teamCode));
            CampTeam team = mongoUtil.findOne(query,CampTeam.class);
            if(team == null) throw new BizException("数据异常");
            Integer teamScore = team.getScore();
            if(teamScore == null) teamScore = 0;
            teamScore += multScore;
            //更新数据库
            CampTeam param = new CampTeam();
            param.setId(team.getId());
            Map<String,Object> map = new HashMap<String,Object>(1);
            map.put("score",teamScore);
            mongoUtil.update(param,map,CampTeam.class);
        }
    }

    /**
     * 更新个人分数
     * @param userScore
     * @param userTeamId
     */
    private void updateUserScore(Integer userScore, String userTeamId) {
        LOGGER.info("更新个人分数userScore={},userTeamId={}",userScore,userTeamId);
        CampUserTeam param = new CampUserTeam();
        param.setId(userTeamId);
        Map<String,Object> map = new HashMap<String,Object>(1);
        map.put("score",userScore);
        mongoUtil.update(param,map,CampUserTeam.class);
    }

    /**
     * 保存usertask
     * @param userTask
     */
    private void saveUserTask(CampUserTask userTask) {
        mongoUtil.save(userTask);
    }

    /**
     * 查询task信息
     * @param tashId
     * @return
     */
    private CampTask findTask(String tashId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(tashId));
        return mongoUtil.findOne(query,CampTask.class);
    }

    private List<TopNVo> getAllTeamTopN(Integer timesCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("timesCode").is(timesCode))
                .with(new Sort(new Sort.Order(Sort.Direction.DESC,"score")));
        List<CampTeam> ct = mongoUtil.find(query,CampTeam.class);
        if(ct ==null || ct.size()!=3){
            throw new BizException("阵营数据错误");
        }
        List<TopNVo> list = new ArrayList<TopNVo>(3);
        for(int i=0;i<ct.size();i++){
            TopNVo vo = new TopNVo();
            vo.setScore(ct.get(i).getScore());
            vo.setRanking(i+1);
            vo.setTeamCode(ct.get(i).getTeamCode());
            list.add(vo);
        }
        if(list.get(1).getScore() == list.get(0).getScore()){
            //如果第二名和第一名分数一样
            list.get(1).setRanking(list.get(0).getRanking());
        }
        if(list.get(2).getScore() == list.get(1).getScore()){
            //如果第三名和第二名分数一样
            list.get(2).setRanking(list.get(1).getRanking());
        }
        return list;
    }

    /**
     * 获取阵营排名
     * @param teamCode
     * @return
     */
    private List<TopNVo> getTeamTopN(Integer teamCode) {
        LOGGER.info("获取阵营排名teamCode={}",teamCode);
        Integer timesCode = findTimesCodeWithException();
        Query query = new Query();
        query.addCriteria(Criteria.where("teamCode").is(teamCode))
                .addCriteria(Criteria.where("timesCode").is(timesCode))
                .with(new Sort(new Sort.Order(Sort.Direction.DESC,"score"),new Sort.Order(Sort.Direction.ASC,"createTime"))).limit(5);
        List<CampUserTeam> list = mongoUtil.find(query,CampUserTeam.class);

        List<TopNVo> retList = null;
        if(list!=null && list.size()>0){
            retList = new ArrayList<TopNVo>(list.size());
            for(int i=0; i<list.size();i++){
                TopNVo vo = new TopNVo();
                vo.setNickName(list.get(i).getNickName());
                vo.setRanking(i+1);
                vo.setScore(list.get(i).getScore());
                vo.setTeamCode(teamCode);
                retList.add(vo);
            }
        }
        return retList;
    }

    /**
     * 获取我的排名
     * @param userId
     * @param campUserTeam
     * @return
     */
    private Integer getUserRanking(String userId, CampUserTeam campUserTeam) {
        LOGGER.info("获取我的阵营积分排行userId={}",userId);

        Integer teamCode = campUserTeam.getTeamCode();
        Integer score = campUserTeam.getScore();
        if(score == null ) score = 0;

        //查询比我比分高的个数
        Query query = new Query();
        query.addCriteria(Criteria.where("teamCode").is(teamCode))
                .addCriteria(Criteria.where("timesCode").is(campUserTeam.getTimesCode()))
                .addCriteria(Criteria.where("score").gt(score));
        Long size = mongoUtil.count(query,CampUserTeam.class);
        if(size == null) size = 0L;

        //获取同分数比我先参加的人
        query = new Query();
        query.addCriteria(Criteria.where("teamCode").is(teamCode))
                .addCriteria(Criteria.where("timesCode").is(campUserTeam.getTimesCode()))
                .addCriteria(Criteria.where("score").is(score))
                .addCriteria(Criteria.where("createTime").lt(campUserTeam.getCreateTime()));
        Long size2 = mongoUtil.count(query,CampUserTeam.class);
        if(size2 == null) size2=0L;
        size+=size2;

        ++size;
        return size.intValue();
    }

    /**
     * 查找快速通道票个数
     * @param userId
     * @return
     */
    private Integer findQuickChannel(String userId) {
        Integer date = getTodayDate();
        LOGGER.info("查找快速通道票个数userId={},date={}",userId,date);
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId)).addCriteria(Criteria.where("date").is(date));
        CampQuickChannel quickChannel = mongoUtil.findOne(query,CampQuickChannel.class);
        if(quickChannel == null || quickChannel.getQuickChannelSize() == null) return 0;
        return quickChannel.getQuickChannelSize();
    }

    /**
     * 查找快速通道票对象
     * @param userId
     * @return
     */
    private CampQuickChannel findQuickChannelObj(String userId) {
        Integer date = getTodayDate();
        LOGGER.info("查找快速通道票对象userId={},date={}",userId,date);
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId)).addCriteria(Criteria.where("date").is(date));
        CampQuickChannel quickChannel = mongoUtil.findOne(query,CampQuickChannel.class);
        return quickChannel;
    }

    /**
     * 查找用户阵营数据
     * @param userId
     * @return
     */
    private CampUserTeam findTeamUser(String userId) {
        Integer timesCode = findTimesCode();
        if(timesCode == null) throw new BizException("游戏未开始");
        LOGGER.info("查找用户阵营数据userId={},timesCode={}",userId,timesCode);
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId)).addCriteria(Criteria.where("timesCode").is(timesCode));
        CampUserTeam userTeam = mongoUtil.findOne(query,CampUserTeam.class);
        return userTeam;
    }

    /**
     * 查找用户阵营数据
     * @param userId
     * @param timesCode
     * @return
     */
    private CampUserTeam findTeamUser(String userId, Integer timesCode) {
        LOGGER.info("查找用户阵营数据userId={},timesCode={}",userId,timesCode);
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId)).addCriteria(Criteria.where("timesCode").is(timesCode));
        CampUserTeam userTeam = mongoUtil.findOne(query,CampUserTeam.class);
        return userTeam;
    }

}
