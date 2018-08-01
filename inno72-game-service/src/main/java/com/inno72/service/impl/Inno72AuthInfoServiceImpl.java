package com.inno72.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.AesUtils;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.common.util.QrCodeUtil;
import com.inno72.common.util.UuidUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72MachineMapper;
import com.inno72.model.Inno72Machine;
import com.inno72.oss.OSSUtil;
import com.inno72.service.Inno72AuthInfoService;
import com.inno72.vo.UserSessionVo;

import net.coobird.thumbnailator.Thumbnails;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
@Service
@Transactional
public class Inno72AuthInfoServiceImpl implements Inno72AuthInfoService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72AuthInfoServiceImpl.class);
	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;
	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;
	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Override
	public Result<Object> createQrCode(String machineId) {
		LOGGER.info("根据机器id生成二维码 machineCode is {} ", machineId);

		Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineId);
		Map<String, Object> map = new HashMap<String, Object>();
		// 在machine库查询bluetooth地址 "6893a2ada9dd4f7eb8dc33adfc6eda73"
		String bluetoothAdd = "";
		String bluetoothAddAes = "";
		if (inno72Machine != null) {
			bluetoothAdd = inno72Machine.getBluetoothAddress();
			if (!StringUtil.isEmpty(bluetoothAdd)) {
				bluetoothAddAes = AesUtils.encrypt(bluetoothAdd);
			}
		}
		String machineCode = "";
		if (!StringUtil.isEmpty(machineId)) {
			machineCode = AesUtils.encrypt(machineId);
		}
		String _machineId = inno72Machine.getId();

		LOGGER.info("Mac蓝牙地址 {} ", bluetoothAddAes);
		
		// 生成sessionUuid
		String sessionUuid = UuidUtil.getUUID32();
		//获取运行环境
		String env = inno72GameServiceProperties.get("env");
		// 调用天猫的地址
		String url = inno72GameServiceProperties.get("tmallUrl") + _machineId + "/" + sessionUuid + "/?env="+env+"&bluetoothAddAes="
				+ bluetoothAddAes+"&machineCode="+machineCode;
		
		LOGGER.info("二维码字符串 {} ", url);
		// 二维码存储在本地的路径
		String localUrl = _machineId + sessionUuid + ".png";

		// 存储在阿里云上的文件名
		String objectName = "qrcode/" + localUrl;
		// 提供给前端用来调用二维码的地址
		String returnUrl = inno72GameServiceProperties.get("returnUrl") + objectName;

		try {
			boolean result = QrCodeUtil.createQrCode(localUrl, url, 1800, "png");
			if (result) {
				File f = new File(localUrl);
				if (f.exists()) {
					// 压缩图片
					Thumbnails.of(localUrl).scale(0.5f).outputQuality(0f).toFile(localUrl);
					// 上传阿里云
					OSSUtil.uploadLocalFile(localUrl, objectName);
					// 删除本地文件
					f.delete();
				}

				// 设置二维码过期时间
				gameSessionRedisUtil.setSessionEx(sessionUuid + "_qrCode", "", 1800);

				map.put("qrCodeUrl", returnUrl);
				map.put("sessionUuid", sessionUuid);
				// LOGGER.info("二维码生成成功 - result -> {}", JSON.toJSONString(map).replace("\"",
				// "'"));
				LOGGER.info("二维码生成成功 - result -> {}", JsonUtil.toJson(map));
			} else {
				LOGGER.info("二维码生成失败");
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return Results.success(map);
	}

	@Override
	public Result<Object> sessionPolling(String sessionUuid) {
		if (StringUtils.isEmpty(sessionUuid)) {
			return Results.failure("参数缺失！");
		}

		UserSessionVo sessionStr = gameSessionRedisUtil.getSessionKey(sessionUuid);

		if (sessionStr == null) {
			return Results.failure("未登录！");
		}

		return Results.success(sessionStr);
	}

}
