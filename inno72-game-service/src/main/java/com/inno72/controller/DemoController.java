package com.inno72.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.log.LogContext;
import com.inno72.log.vo.LogType;
import com.inno72.model.Inno72Game;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72GameService;
import com.inno72.vo.UserSessionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
public class DemoController {

	private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

	@Resource
	private Inno72GameService inno72GameService;

	@Resource
	private IRedisUtil redisUtil;


	@Value("${spring.datasource.url}")
	private String url;

	public void setUrl(String url) {
		this.url = url;
	}

	@ResponseBody
	@RequestMapping("test")
	public String test() {

		// UserSessionVo userSessionVo = new UserSessionVo("123456");
//		System.out.println(userSessionVo);
//		Set<String> keys = redisUtil.hkeys("SESSION:game-service:SESSION:123456");
//		System.out.println(keys);

		redisUtil.deleteByPrex("SESSION:game-service:SESSION:*");

		return "test";

	}

	@ResponseBody
	@RequestMapping("testJump")
	public String testJump(String userId, HttpServletResponse response) {
		logger.info("userId is {}", userId);
		try {
			response.sendRedirect("https://m.tb.cn/.T5NQRt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "test";
	}

	@ResponseBody
	@RequestMapping("update")
	public String update() {
		//
		//		new LogContext(LogType.SYS).tag("系统日志tag").bulid();
		//		logger.info("hello {}", "系统日志内容");

		//		Inno72Game inno72Game = inno72GameService.findById(1);
		//		inno72Game.setName(inno72Game.getName());
		//		inno72GameService.update(inno72Game);

		//		Random rand =new Random(25);
		//		int i = rand.nextInt(100);
		//		Inno72Game _inno72Game = new Inno72Game();
		//		_inno72Game.setId(String.valueOf(i));
		//		_inno72Game.setCreateId("test");
		//		_inno72Game.setUpdateId("test");
		//		_inno72Game.setRemark("test");
		//		_inno72Game.setVersion("test");
		//		_inno72Game.setVersionInno72("test");
		//		inno72GameService.save(_inno72Game);

		Inno72Game byId = inno72GameService.findById(81);
		inno72GameService.deleteByIds(byId.getId());
		return "update ok";
	}

	@RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
	public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {

		new LogContext(LogType.SYS).tag("系统日志tag").bulid();
		logger.info("hello {}", "系统日志内容");

		PageHelper.startPage(page, size);
		List<Inno72Game> list = inno72GameService.findAll();
		PageInfo pageInfo = new PageInfo(list);
		return ResultGenerator.genSuccessResult(pageInfo);
	}

	@ResponseBody
	@RequestMapping("test1")
	public String test1() {
		return "123";
	}


}
