package com.inno72.check.service.impl;

import com.inno72.check.mapper.Inno72CheckSignInMapper;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.service.CheckSignInService;
import com.inno72.check.vo.MachineSignInVo;
import com.inno72.common.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CheckSignInServiceImpl extends AbstractService<Inno72CheckSignIn> implements CheckSignInService {

    @Resource
    private Inno72CheckSignInMapper checkSignInMapper;
    @Override
    public Result<String> add(Inno72CheckSignIn signIn) {
        signIn.setCreateTime(LocalDateTime.now());
        signIn.setId(StringUtil.getUUID());
        signIn.setCheckUserId(UserUtil.getUser().getId());
        int count = checkSignInMapper.insertSelective(signIn);
        if(count != 1){
            Results.failure("打卡失败");
        }
        return ResultGenerator.genSuccessResult();
    }

    @Override
    public Result<List<Inno72CheckSignIn>> findThisMonth(String machineId) {
        List<Inno72CheckSignIn> list = checkSignInMapper.selectTishMonth(UserUtil.getUser().getId(),machineId);
        return ResultGenerator.genSuccessResult(list);
    }

    @Override
    public Result<List<MachineSignInVo>> findMachineSignList() {
        List<MachineSignInVo> list = checkSignInMapper.selectMachineSignList(UserUtil.getUser().getId());
        if(list != null && list.size()>0){
            for(MachineSignInVo vo:list){
                List<Inno72CheckSignIn> signInList = vo.getSignInList();
                if(signInList != null && signInList.size()>0){
                    vo.setSignInStatus(1);
                }else{
                    vo.setSignInStatus(-1);
                }
                vo.setSignInList(null);
            }
        }

        return ResultGenerator.genSuccessResult(list);
    }
}
