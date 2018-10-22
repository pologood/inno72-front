package com.inno72.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.util.QrCodeUtil;
import com.inno72.oss.OSSUtil;
import com.inno72.service.SuperOpenService;

import net.coobird.thumbnailator.Thumbnails;

/**
 * inno72 统一开放接口，负责所有客户端交互代理转发。
 * 增加接口时候 增加{@see com.inno72.service.impl.ADPTE_METHOD}
 *
 * @author zb.zhou
 *
 */
@RestController
@RequestMapping("/inno72")
public class Inno72SuperOpenController {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72SuperOpenController.class);

	@Resource
	private SuperOpenService superOpenService;

	/**
	 * @param request reques
	 * @param response response
	 * @param requestJson 请求参数
	 * 	ex:
	 *	{
	 *
	 *	   "serviceName":findGame,
	 *	   "params":{
	 *	        "machineId":"machineId",
	 *	        "gameId":"gameId"
	 *	    },
	 *	    "version":"1.0.0"
	 *	}
	 */
	@RequestMapping(value = "/service/open", method = {RequestMethod.GET, RequestMethod.POST})
	public Result<String> open(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String requestJson) {

		try {
			request.getRequestDispatcher(superOpenService.adpter(requestJson)).forward(request, response);
		} catch (Exception e) {
			LOGGER.error("公共开放接口异常 ===> {} ", e.getMessage(), e);
		}
		return Results.failure("调用服务失败！");
	}

	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;

	@RequestMapping(value = "/service/queryPicCode", method = {RequestMethod.GET, RequestMethod.POST})
	public Result<Object> queryPicCode(@RequestParam MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return Results.failure("非法请求!");
		}
		long time = new Date().getTime();
		int random = (int) (Math.random() * 6);
		String name = file.getOriginalFilename();
		if (!name.contains(".")) {
			return Results.failure("格式错误!");
		}
		String photoName = (random % 2 == 0 ? (time - random) + "" : (time + random) + "") + time + name
				.substring(name.lastIndexOf("."));
		String ossPhotoPath = "game/user/photo/" + photoName;
		String ossPhotoCodePath = "game/user/code/" + photoName;

		try {
			OSSUtil.uploadByStream(file.getInputStream(), ossPhotoPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String host = inno72GameServiceProperties.get("returnUrl");
		String fullOssUrl = host + ossPhotoPath;

		try {
			boolean result = QrCodeUtil.createQrCode(photoName, fullOssUrl, 1800, "png");
			if (!result) {
				return Results.failure("生成二维码失败!");
			}
			File localCodeFile = new File(photoName);
			if (!localCodeFile.exists()) {
				Thread.sleep(1200);
				localCodeFile = new File(photoName);
				if (!localCodeFile.exists()) {
					return Results.failure("二维码不翼而飞，一会再试一次呗!");
				}
			}
			// 压缩图片
			Thumbnails.of(photoName).scale(0.5f).outputQuality(0f).toFile(photoName);
			// 上传阿里云
			OSSUtil.uploadLocalFile(photoName, ossPhotoCodePath);
			// 删除本地文件
			boolean delete = localCodeFile.delete();
			LOGGER.debug("删除文件 {}", delete);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, String> result = new HashMap<>(2);
		result.put("ossPhotoCodePath", host + ossPhotoCodePath);
		result.put("ossPhotoPath", host + ossPhotoPath);
		return Results.success(result);
	}

}
