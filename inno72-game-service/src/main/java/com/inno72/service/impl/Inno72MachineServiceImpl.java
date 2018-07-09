package com.inno72.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.Inno72GameServiceProperties;
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

import net.coobird.thumbnailator.Thumbnails;

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
	private IRedisUtil redisUtil;
	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;

	@Override
	public Result<Inno72MachineVo> findGame(String machineId, String gameId, String version, String versionInno72) {
		LOGGER.info("查询售卖机游戏详情 - machineId -> {}", machineId);
		Inno72Machine inno72Machine = inno72MachineMapper.selectByPrimaryKey(machineId);
		if (inno72Machine == null) {
			return Results.failure("机器ID错误!");
		}
		List<Inno72MachineGame> selectByCondition = inno72MachineGameMapper.selectByMachineId(machineId);
		if (selectByCondition.size() == 0) {
			return Results.failure("机器ID没有绑定游戏!");
		}

		String gameIds = "";

		for (Inno72MachineGame inno72MachineGame : selectByCondition) {
			gameIds += inno72MachineGame.getGameId() + ",";
		}
		if (gameIds.substring(gameIds.length() - 1).equals(",")) {
			gameIds = gameIds.substring(0, gameIds.length() - 1);
		}
		Inno72Game inno72Games = inno72GameMapper.selectByPrimaryKey(gameIds);

		Inno72MachineVo inno72MachineVo = new Inno72MachineVo();
		BeanUtils.copyProperties(inno72Machine, inno72MachineVo);
		inno72MachineVo.setInno72Games(inno72Games);

		String brandName = inno72GameMapper.selectBoundName(inno72Games.getSellerId());
		inno72MachineVo.setBrandName(brandName);

		if (!gameId.equals("0") && (!inno72Games.getId().equals(gameId) || !inno72Games.getVersion().equals(version)
				|| !inno72Games.getVersionInno72().equals(versionInno72))) {
			inno72MachineVo.setReload(true);
		}

		LOGGER.info("查询完成 - result -> {}", JSON.toJSONString(inno72MachineVo));

		return Results.success(inno72MachineVo);
	}

	@Override
	public Result<Object> createQrCode(String machineId) {
		LOGGER.info("根据机器id生成二维码", machineId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 生成sessionUuid
		String sessionUuid = UuidUtil.getUUID32();
		// 调用天猫的地址
		String url =inno72GameServiceProperties.get("tmallUrl")
						+ machineId + "/" + sessionUuid;
		// 二维码存储在本地的路径
		String localUrl = machineId + com.inno72.common.util.StringUtil.uuid() + ".png";
		// 存储在阿里云上的文件名
		String objectName = "qrcode/" + localUrl;
		// 提供给前端用来调用二维码的地址
		String returnUrl = inno72GameServiceProperties.get("returnUrl") + objectName;

		try {
			boolean result = QrCodeUtil.createQrCode(localUrl, url, 1800, "png");
			if (result) {
				File f = new File(localUrl);
				if (f.exists()) {
					//压缩图片
					Thumbnails.of(localUrl) 
			           .scale(0.5f) 
			           .outputQuality(0f) 
			           .toFile(localUrl);
				     //上传阿里云
				     OSSUtil.uploadLocalFile(localUrl, objectName);
				     // 删除本地文件
				     f.delete();
				}
				map.put("qrCodeUrl", returnUrl);
				map.put("sessionUuid", sessionUuid);
				LOGGER.info("二维码生成成功 - result -> {}", JSON.toJSONString(map).replace("\"", "'"));
			} else {
				LOGGER.info("二维码生成失败");
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
		return Results.success(map);
	}

	@Override
	public Result<Object> sessionPolling(String sessionUuid) {
		if (StringUtils.isEmpty(sessionUuid)) {
			return Results.failure("参数缺失！");
		}

		String sessionStr = redisUtil.get(sessionUuid);

		if (StringUtils.isEmpty(sessionStr)) {
			return Results.failure("未登录！");
		}

		return Results.success(JSON.parseObject(sessionStr));
	}

}
