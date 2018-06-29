package com.inno72.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.QrCodeUtil;
import com.inno72.common.util.UuidUtil;
import com.inno72.mapper.Inno72GameMapper;
import com.inno72.mapper.Inno72MachineGameMapper;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72MachineGame;
import com.inno72.oss.OSSUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72MachineService;
import com.inno72.vo.Inno72MachineVo;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72MachineServiceImpl extends AbstractService<Inno72Machine> implements Inno72MachineService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72MachineServiceImpl.class);
    @Resource
    private Inno72MachineMapper inno72MachineMapper;
    @Resource
    private Inno72MachineGameMapper inno72MachineGameMapper;
    @Resource
    private Inno72GameMapper inno72GameMapper;
    @Autowired
    private IRedisUtil redistUtil;

	@Override
	public  Result<Inno72MachineVo> findGame(String machineId, String gameId) {
		LOGGER.info("查询售卖机游戏详情 - machineId -> {}", machineId);
		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器ID错误!");
		}
		List<Inno72MachineGame> selectByCondition = inno72MachineGameMapper.selectByMachineId(Integer.parseInt(machineId));
		if (selectByCondition.size() == 0) {
			return Results.failure("机器ID没有绑定游戏!");
		}
		
		String gameIds = "";
		
		for (Inno72MachineGame inno72MachineGame : selectByCondition) {
			gameIds+=inno72MachineGame.getGameId()+",";
		}
		if (gameIds.substring(gameIds.length()-1) .equals(",")) {
			gameIds = gameIds.substring(0, gameIds.length()-1);
		}
		Inno72Game inno72Games = inno72GameMapper.selectByPrimaryKey(gameIds);
		
		Inno72MachineVo inno72MachineVo = new Inno72MachineVo();
		BeanUtils.copyProperties(inno72Machine, inno72MachineVo);
		inno72MachineVo.setInno72Games(inno72Games);
		
		if (!gameId.equals("0") && !inno72Games.getId().equals(gameId)) {
			inno72MachineVo.setReload(true);
		}
		
		LOGGER.info("查询完成 - result -> {}", JSON.toJSONString(inno72MachineVo));
		
		return Results.success(inno72MachineVo);
	}

	@Override
	public Result<String> createQrCode(Integer machineId) {
		LOGGER.info("根据机器id生成二维码", machineId);
		Map<String, Object> map = new HashMap<String, Object>();
		String url = "https://oauth.taobao.com/authorize?response_type=code&client_id=24791535&machineId="+machineId+"&redirect_uri=https://inno72test.ews.m.jaeapp.com/";
        String localUrl = new Date().getTime()+""+machineId+"qrcode.jpg";
        String targetPath = "https://oss.console.aliyun.com/bucket/oss-cn-beijing/inno72-qrcode/object/"+localUrl;
		try {
			boolean result = QrCodeUtil.createQrCode(localUrl,url,10000,"JPEG");
			String sessionUuid = UuidUtil.getUUID32();
			if(result) {
				OSSUtil.uploadLocalFile(localUrl, localUrl);
				map.put("qrCodeUrl", targetPath);
				map.put("sessionUuid", sessionUuid);
				LOGGER.info("二维码生成成功 - result -> {}", JSON.toJSONString(map));
			}else {
				LOGGER.info("二维码生成失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("二维码生成失败", e);
		}
		return Results.success(JSON.toJSONString(map));
	}

}
