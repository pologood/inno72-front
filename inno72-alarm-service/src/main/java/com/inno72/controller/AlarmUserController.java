package com.inno72.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.ResultPages;
import com.inno72.model.AlarmUser;
import com.inno72.service.AlarmUserService;

/**
* Created by CodeGenerator on 2018/08/13.
*/
@RestController
@RequestMapping("/alarm/user")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AlarmUserController {
    @Resource
    private AlarmUserService alarmUserService;

    @RequestMapping(value = "/add", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result add(AlarmUser alarmUser) {
alarmUserService.save(alarmUser);
        return ResultGenerator.genSuccessResult();
    }
    @RequestMapping(value = "/delete", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result delete(@RequestParam String id) {
alarmUserService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/update", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result update(AlarmUser alarmUser) {
alarmUserService.update(alarmUser);
        return ResultGenerator.genSuccessResult();
    }
    
    @RequestMapping(value = "/detail", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result detail(@RequestParam String id) {
AlarmUser alarmUser = alarmUserService.findById(id);
        return ResultGenerator.genSuccessResult(alarmUser);
    }
    
    @RequestMapping(value = "/list", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<AlarmUser> list = alarmUserService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

	/**
	 * 同步用户
	 * @return 同步结果
	 */
	@RequestMapping(value = "/syncUser", method = { RequestMethod.POST,  RequestMethod.GET})
    public Result syncUser() {
		return  alarmUserService.syncUser();
    }


	@RequestMapping(value = "/getList", method = { RequestMethod.POST,  RequestMethod.GET})
	public ModelAndView getList(AlarmUser alarmUser) {
		List<AlarmUser> list = alarmUserService.queryForPage(alarmUser);
		return ResultPages.page(ResultGenerator.genSuccessResult(list));
	}

}
