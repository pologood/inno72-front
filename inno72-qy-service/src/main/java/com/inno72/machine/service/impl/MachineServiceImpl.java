package com.inno72.machine.service.impl;

import com.inno72.check.mapper.Inno72CheckUserMachineMapper;
import com.inno72.check.mapper.Inno72CheckUserMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.model.Inno72CheckUserMachine;
import com.inno72.check.vo.FaultVo;
import com.inno72.common.*;
import com.inno72.machine.mapper.Inno72AdminAreaMapper;
import com.inno72.machine.mapper.Inno72LocaleMapper;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72AdminArea;
import com.inno72.machine.model.Inno72Locale;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.machine.service.MachineService;
import com.inno72.machine.vo.SupplyRequestVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MachineServiceImpl extends AbstractService<Inno72Machine> implements MachineService {

    @Resource
    private Inno72MachineMapper inno72MachineMapper;

    @Resource
    private Inno72CheckUserMachineMapper inno72CheckUserMachineMapper;

    @Resource
    private Inno72AdminAreaMapper inno72AdminAreaMapper;

    @Resource
    private Inno72LocaleMapper inno72LocaleMapper;

    @Resource
    private Inno72CheckUserMapper inno72CheckUserMapper;
    @Override
    public Result<String> setMachine(SupplyRequestVo vo) {
        String localeId = vo.getLocaleId();
        String machineCode = vo.getMachineCode();
        if(StringUtil.isEmpty(localeId) || StringUtil.isEmpty(machineCode)){
            return Results.failure("参数不能为空");
        }
        Inno72Machine machine = inno72MachineMapper.getMachineByCode(machineCode);
        if(machine == null){
            return Results.failure("机器不存在");
        }
        String machineId = machine.getId();
        Integer machineStatus = machine.getMachineStatus();
        if(machineStatus.equals(3) || machineStatus.equals(4)){
            Inno72CheckUser checkUser = UserUtil.getUser();
            machine.setLocaleId(localeId);
            machine.setMachineStatus(4);
            machine.setUpdateId(checkUser.getId());
            machine.setUpdateTime(LocalDateTime.now());
            inno72MachineMapper.updateByPrimaryKeySelective(machine);
            Condition condition = new Condition(Inno72CheckUserMachine.class);
            condition.createCriteria().andEqualTo("checkUserId",checkUser.getId()).andEqualTo("machineId",machineId);
            List<Inno72CheckUserMachine> userMachines = inno72CheckUserMachineMapper.selectByCondition(condition);
            if(userMachines == null || userMachines.size()==0){
                Inno72CheckUserMachine userMachine = new Inno72CheckUserMachine();
                userMachine.setId(StringUtil.getUUID());
                userMachine.setCheckUserId(checkUser.getId());
                userMachine.setMachineId(machineId);
                inno72CheckUserMachineMapper.insertSelective(userMachine);
            }
        }else{
            return Results.failure("机器状态有误");
        }
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<List<Inno72Machine>> getMachineList() {
        String chekUserId = UserUtil.getUser().getId();
        List<Inno72Machine> list = inno72MachineMapper.machineList(chekUserId);
        if(list != null && list.size()>0){
            list.forEach(inno72Machine -> {
                List<FaultVo> faultVoList = inno72Machine.getFaultVoList();
                if(faultVoList != null && faultVoList.size()>0){
                    inno72Machine.setFaultStatus(-1);
                }else{
                    inno72Machine.setFaultStatus(1);
                }
                inno72Machine.setFaultVoList(null);
                List<Inno72CheckSignIn> signInList = inno72Machine.getSignInList();
                if(signInList != null && signInList.size()>0){
                    inno72Machine.setSignInStatus(1);
                }else{
                    inno72Machine.setSignInStatus(-1);
                }
                inno72Machine.setSignInList(null);
            });
        }
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 查询一级地址
     */
    @Override
    public Result<List<Inno72AdminArea>> findFirstLevelArea() {
//        String userId = UserUtil.getUser().getId();
//        Inno72CheckUser user = inno72CheckUserMapper.selectByPrimaryKey(userId);
//        String area = user.getArea();
        Map<String,Object> map = new HashMap<>();
//        if(StringUtil.isNotEmpty(area)){
//            Inno72AdminArea adminArea = inno72AdminAreaMapper.selectByPrimaryKey(area);
//            map.put("level",adminArea.getLevel());
//            map.put("code",adminArea.getCode());
//        }
        List<Inno72AdminArea> list = inno72AdminAreaMapper.selectFistLevelArea(map);
        return ResultGenerator.genSuccessResult(list);
    }

    /**
     * 查询单个一级地址及以下地址
     */
    @Override
    public Result<Inno72AdminArea> cityLevelArea(Inno72AdminArea adminArea) {
        String area = "";
//        if(StringUtil.isEmpty(area)){
//            Inno72CheckUser user = inno72CheckUserMapper.selectByPrimaryKey(UserUtil.getUser().getId());
//            area = user.getArea();
//            if(StringUtil.isEmpty(area)){
//                Results.failure("用户未设置区域");
//            }
//        }
        Map<String,Object> map = new HashMap<>();
        map.put("code",adminArea.getCode());
        Inno72AdminArea inno72AdminArea = inno72AdminAreaMapper.cityLevelArea(map);
        return ResultGenerator.genSuccessResult(inno72AdminArea);
    }

    @Override
    public Result<List<Inno72Locale>> selectLocaleByAreaCode(String areaCode) {
        List<Inno72Locale> list = inno72LocaleMapper.selectLocaleByAreaCode(areaCode);
        return ResultGenerator.genSuccessResult(list);
    }

    @Override
    public Result<Map<String,Object>> selectMachineLocale(Inno72Machine inno72Machine) {
        String machineCode = inno72Machine.getMachineCode();
        if(StringUtil.isEmpty(machineCode)){
            return Results.failure("参数不能为空");
        }
        Inno72Machine machineLocale = inno72MachineMapper.getMachineByCode(machineCode);
        if(machineLocale != null){
            Map<String,Object> map = new HashMap<>();
            map.put("localeStr",machineLocale.getLocaleStr());
            map.put("localeId",machineLocale.getLocaleId());
            Condition condition = new Condition(Inno72CheckUserMachine.class);
            condition.createCriteria().andEqualTo("checkUserId",UserUtil.getUser().getId())
                    .andEqualTo("machineId",machineLocale.getId());

            List<Inno72CheckUserMachine> list = inno72CheckUserMachineMapper.selectByCondition(condition);
            int machineFlag = 0;
            if(list != null && list.size()>0){
                machineFlag = 1;
            }
            map.put("machineFlag",machineFlag);
            return ResultGenerator.genSuccessResult(map);
        }else{
            return Results.failure("机器有误");
        }
    }

}
