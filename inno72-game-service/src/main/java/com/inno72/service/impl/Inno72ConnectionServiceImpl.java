package com.inno72.service.impl;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.json.JsonUtil;
import com.inno72.mapper.Inno72MachineActivityImageMapper;
import com.inno72.mapper.Inno72MachineConnectionMsgMapper;
import com.inno72.model.Inno72MachineActivityImage;
import com.inno72.model.Inno72MachineConnectionMsg;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72ConnectionService;
import com.inno72.service.Inno72MachineService;
import com.inno72.vo.Inno72ConnectionFindActivityResultVo;
import com.inno72.vo.Inno72MachineVo;
import com.inno72.vo.PushRequestVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class Inno72ConnectionServiceImpl implements Inno72ConnectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72ConnectionServiceImpl.class);

    @Autowired
    private Inno72GameServiceProperties inno72GameServiceProperties;
    @Autowired
    private Inno72MachineService inno72MachineService;
    @Autowired
    private Inno72MachineActivityImageMapper inno72MachineActivityImageMapper;
    @Autowired
    private Inno72MachineConnectionMsgMapper inno72MachineConnectionMsgMapper;

    @Override
    public void changeActivity(String machineCode, String activityId, Integer activityType) {
        Inno72ConnectionFindActivityResultVo dataVo = new Inno72ConnectionFindActivityResultVo();
        dataVo.setActivityId(activityId);
        dataVo.setMachineCode(machineCode);
        dataVo.setVersion(System.currentTimeMillis());
        dataVo.setType(Inno72ConnectionFindActivityResultVo.TYPE_ENUM.FINDACTIVITY.getKey());
        if(!"0".equals(activityId)){
            Result r = inno72MachineService.findGame(machineCode,"-1","","");
            Inno72MachineVo vo = (Inno72MachineVo)r.getData();
            if(vo!=null){
                dataVo.setGameVersion(vo.getInno72Games().getVersion());
                dataVo.setGameVersionInno72(vo.getInno72Games().getVersionInno72());
                dataVo.setPlanCode(vo.getPlanCode());
            }else{
                dataVo.setGameVersion("0");
                dataVo.setGameVersionInno72("0");
                dataVo.setPlanCode("0");
            }
        }else{
            //加载默认活动
            dataVo.setGameVersion("0");
            dataVo.setGameVersionInno72("0");
            dataVo.setPlanCode("0");
        }
        String data = JsonUtil.toJson(dataVo);
        send(machineCode,activityId,dataVo.getVersion(),dataVo.getType(),data);
    }
    @Override
    public void send(String machineCode, String activityId, Long version, Integer type, String data) {
        PushRequestVo vo = new PushRequestVo();
        vo.setData(data);
        vo.setTargetCode(machineCode);
        String request = JsonUtil.toJson(vo);

        Inno72MachineConnectionMsg msg = new Inno72MachineConnectionMsg();
        msg.setActivityId(activityId);
        msg.setCreateTime(new Date());
        msg.setUpdateTime(msg.getCreateTime());
        msg.setMachineCode(machineCode);
        msg.setMsg(request);
        msg.setStatus(Inno72MachineConnectionMsg.STATUS_ENUM.COMMIT.getKey());
        msg.setTimes(1);
        msg.setType(type);
        msg.setVersion(version);
        inno72MachineConnectionMsgMapper.insert(msg);
        //发送长连接
        String pushServerUrl = inno72GameServiceProperties.get("pushServerUrl")+"/pusher/push/one";
        LOGGER.info("send msg url = {},data={}",pushServerUrl,request);
        String response = HttpClient.post(pushServerUrl,request);
        LOGGER.info("send msg response={}",response);
    }

    @Override
    public void callBack(String machineCode, String activityId, Integer type, Long version) {
        updateMsgStatus(machineCode,activityId,type,version);
        switch (type){
            case 0:
                findActivityCallBack(machineCode,activityId,type,version);
                break;
        }
        invalidBeforeMsg(machineCode,activityId,type,version);
    }

    private void invalidBeforeMsg(String machineCode, String activityId, Integer type, Long version) {
        inno72MachineConnectionMsgMapper.invalidBeforeMsg(machineCode,activityId,type,version);
    }

    private void findActivityCallBack(String machineCode, String activityId, Integer type, Long version) {
        Inno72MachineActivityImage inno72MachineActivityImage = new Inno72MachineActivityImage();
        inno72MachineActivityImage.setMachineCode(machineCode);
        inno72MachineActivityImage =inno72MachineActivityImageMapper.selectOne(inno72MachineActivityImage);
        if(inno72MachineActivityImage==null){
            inno72MachineActivityImage = new Inno72MachineActivityImage();
            inno72MachineActivityImage.setMachineCode(machineCode);
            inno72MachineActivityImage.setActivityId(activityId);
            inno72MachineActivityImage.setUpdateTime(new Date());
            inno72MachineActivityImage.setVersion(version);
            inno72MachineActivityImageMapper.insert(inno72MachineActivityImage);
        }else{
            if(inno72MachineActivityImage.getVersion().longValue()<version.longValue()){
                inno72MachineActivityImage.setActivityId(activityId);
                inno72MachineActivityImage.setUpdateTime(new Date());
                inno72MachineActivityImageMapper.updateByPrimaryKey(inno72MachineActivityImage);
            }
        }
    }

    private void updateMsgStatus(String machineCode, String activityId, Integer type, Long version) {
        inno72MachineConnectionMsgMapper.updateMsgStatus(machineCode,activityId,type,version);
    }

}
