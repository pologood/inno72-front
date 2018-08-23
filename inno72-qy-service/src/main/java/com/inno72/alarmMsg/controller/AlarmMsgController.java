package com.inno72.alarmMsg.controller;

import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.alarmMsg.service.AlarmMsgService;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

@RequestMapping("/alarm/msg")
@CrossOrigin
@RestController
public class AlarmMsgController {


    @Resource
    private AlarmMsgService alarmMsgService;

    @RequestMapping(value="list",method = {RequestMethod.POST,RequestMethod.GET})
    public ModelAndView list(){
        Condition condition = new Condition(Inno72AlarmMsg.class);
        List<Inno72AlarmMsg> list= alarmMsgService.findByPage(condition);
        return ResultPages.page(ResultGenerator.genSuccessResult(list));
    }
}
