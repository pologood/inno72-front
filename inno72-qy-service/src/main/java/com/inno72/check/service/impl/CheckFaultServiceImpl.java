package com.inno72.check.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.inno72.alarmMsg.mapper.Inno72AlarmMsgMapper;
import com.inno72.alarmMsg.model.Inno72AlarmMsg;
import com.inno72.check.mapper.Inno72CheckFaultImageMapper;
import com.inno72.check.mapper.Inno72CheckFaultMapper;
import com.inno72.check.mapper.Inno72CheckFaultRemarkMapper;
import com.inno72.check.mapper.Inno72CheckFaultTypeMapper;
import com.inno72.check.model.Inno72CheckFault;
import com.inno72.check.model.Inno72CheckFaultImage;
import com.inno72.check.model.Inno72CheckFaultRemark;
import com.inno72.check.model.Inno72CheckFaultType;
import com.inno72.check.model.Inno72CheckUser;
import com.inno72.check.service.CheckFaultService;
import com.inno72.check.vo.CheckUserVo;
import com.inno72.common.AbstractService;
import com.inno72.common.CommonConstants;
import com.inno72.common.DateUtil;
import com.inno72.common.ImageUtil;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.StringUtil;
import com.inno72.common.UploadUtil;
import com.inno72.common.UserUtil;
import com.inno72.machine.mapper.Inno72MachineMapper;
import com.inno72.machine.model.Inno72Machine;
import com.inno72.msg.MsgUtil;

import tk.mybatis.mapper.entity.Condition;

@Service("checkFaultService")
public class CheckFaultServiceImpl extends AbstractService<Inno72CheckFault> implements CheckFaultService {

	@Resource
	private Inno72CheckFaultMapper inno72CheckFaultMapper;

	@Resource
	private Inno72CheckFaultImageMapper inno72CheckFaultImageMapper;

	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Resource
	private Inno72CheckFaultRemarkMapper inno72CheckFaultRemarkMapper;

	@Resource
	private Inno72CheckFaultTypeMapper inno72CheckFaultTypeMapper;

	@Resource
	private Inno72AlarmMsgMapper inno72AlarmMsgMapper;

	@Resource
	private MsgUtil msgUtil;

	@Resource
	private MongoOperations mongoTpl;

	@Override
	public Result<String> addCheckFault(Inno72CheckFault checkFault) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		String remark = checkFault.getRemark();
		if (StringUtil.isEmpty(remark)) {
			Results.failure("故障描述不能为空");
		}
		String[] machineIds = checkFault.getMachineIds();
		if (machineIds != null && machineIds.length > 0) {
			for (String machineId : machineIds) {
				String id = StringUtil.getUUID();
				checkFault.setId(id);
				String time = DateUtil.toTimeStr(LocalDateTime.now(), DateUtil.DF_FULL_S2);
				checkFault.setCode("F" + StringUtil.createRandomCode(6) + time);
				checkFault.setSubmitTime(LocalDateTime.now());
				checkFault.setSubmitUser(checkUser.getName());
				checkFault.setMachineId(machineId);
				checkFault.setWorkType(1);//
				checkFault.setSource(1);// 巡检
				checkFault.setUrgentStatus(1);// 日常
				checkFault.setSubmitId(checkUser.getId());
				checkFault.setSubmitUserType(1);// 巡检人员
				checkFault.setStatus(2);// 处理中
				checkFault.setReceiveUser(checkUser.getName());
				checkFault.setReceiveId(checkUser.getId());
				checkFault.setUpdateTime(LocalDateTime.now());
				String type = checkFault.getType();
				Inno72CheckFaultType inno72CheckFaultType = inno72CheckFaultTypeMapper.selectByPrimaryKey(type);
				String typeName = "";
				if (inno72CheckFaultType != null) {
					typeName = inno72CheckFaultType.getName();
				}
				Inno72Machine machine = inno72MachineMapper.selectByPrimaryKey(machineId);
				String machineCode = machine.getMachineCode();
				checkFault.setTitle(machineCode + "出现普通" + typeName + "的故障，请尽快处理");
				inno72CheckFaultMapper.insertSelective(checkFault);
				Inno72CheckFaultRemark faultRemark = new Inno72CheckFaultRemark();
				faultRemark.setRemark(remark);
				faultRemark.setUserId(checkUser.getId());
				faultRemark.setName(checkUser.getName());
				faultRemark.setType(1);
				faultRemark.setCreateTime(LocalDateTime.now());
				faultRemark.setFaultId(id);
				String remarkId = StringUtil.getUUID();
				faultRemark.setId(remarkId);
				inno72CheckFaultRemarkMapper.insertSelective(faultRemark);
				String[] images = checkFault.getImages();
				if (images != null && images.length > 0) {
					for (int i = 0; i < images.length; i++) {
						Inno72CheckFaultImage image = new Inno72CheckFaultImage();
						image.setId(StringUtil.getUUID());
						image.setFaultId(id);
						image.setRemarkId(remarkId);
						image.setSort(i + 1);
						image.setImage(ImageUtil.getLackImageUrl(images[i]));
						image.setCreateTime(LocalDateTime.now());
						inno72CheckFaultImageMapper.insertSelective(image);
					}
				}

			}
			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("checkUserId", checkUser.getId());
			queryMap.put("machineIds", machineIds);
			List<Inno72Machine> machines = inno72MachineMapper.selectByParam(queryMap);
			Inno72CheckFaultType checkFaultType = inno72CheckFaultTypeMapper.selectByPrimaryKey(checkFault.getType());
			if (checkFaultType == null) {
				return Results.failure("故障类型有误");
			}
			if (machines != null && machines.size() > 0) {
				String title = "您负责的机器出现故障";
				String appName = "machine_check_app_backend";
				String pushCode = "push_check_app_fault";
				String smsCode = "sms_check_app_fault";
				String faultType = checkFaultType.getName();

				for (Inno72Machine machine : machines) {
					String machineCode = machine.getMachineCode();
					String localeStr = machine.getLocaleStr();
					Map<String, String> params = new HashMap<>();
					String messgeInfo = "【互动管家】您负责的机器，" + localeStr + "，" + machineCode + "出现故障，故障类型：" + faultType
							+ "，故障描述：" + remark + "，请及时处理。";
					params.put("machineCode", machineCode);
					params.put("machineId", machine.getId());
					params.put("localeStr", localeStr);
					params.put("faultType", faultType);
					params.put("remark", remark);
					params.put("messgeInfo", messgeInfo);
					List<CheckUserVo> checkUserList = machine.getCheckUserVoList();
					List<String> userIdList = new ArrayList<>();
					if (checkUserList != null && checkUserList.size() > 0) {
						for (CheckUserVo user : checkUserList) {
							String phone = user.getPhone();
							if (StringUtil.isNotEmpty(phone)) {
								userIdList.add(user.getId());
								msgUtil.sendPush(pushCode, params, phone, appName, title, messgeInfo);
								msgUtil.sendSMS(smsCode, params, phone, appName);
							}
						}

					}
					Inno72AlarmMsg alarmMsg = new Inno72AlarmMsg();
					alarmMsg.setId(StringUtil.getUUID());
					alarmMsg.setCreateTime(LocalDateTime.now());
					alarmMsg.setMachineCode(machineCode);
					alarmMsg.setSystem("machineChannel");
					alarmMsg.setTitle(title);
					alarmMsg.setType(1);
					alarmMsg.setDetail(messgeInfo);
					inno72AlarmMsgMapper.insertSelective(alarmMsg);

					Map<String, Object> paramsMap = new HashMap<>();
					paramsMap.put("machineCode", machineCode);
					paramsMap.put("localStr", localeStr);
					paramsMap.put("text", "出现故障，故障类型：" + faultType + "，故障描述：" + remark + "，请及时处理。");
					String userIdString = StringUtils.join(userIdList.toArray(), "|");
					Map<String, String> m = new HashMap<>();
					m.put("touser", userIdString);
					m.put("agentid", "1000002");
					msgUtil.sendQyWechatMsg("qywechat_msg", params, m, userIdString, appName);
					StringUtil.logger(CommonConstants.LOG_TYPE_SET_WORK,machineCode,"操作工单：巡检人员"+checkUser.getName()+"对"+localeStr+"点位处的机器提交了故障单");
				}

			}

		}

		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> finish(Inno72CheckFault checkFault) {
		Inno72CheckFault upCheckFault = new Inno72CheckFault();
		upCheckFault.setId(checkFault.getId());
		upCheckFault.setFinishRemark(checkFault.getFinishRemark());
		upCheckFault.setFinishTime(LocalDateTime.now());
		upCheckFault.setStatus(3);// 已完成
		upCheckFault.setFinishId(UserUtil.getUser().getId());
		upCheckFault.setFinishUser(UserUtil.getUser().getName());
		upCheckFault.setUpdateTime(LocalDateTime.now());
		inno72CheckFaultMapper.updateByPrimaryKeySelective(upCheckFault);
		Inno72CheckFaultRemark faultRemark = new Inno72CheckFaultRemark();
		String remarkId = StringUtil.getUUID();
		faultRemark.setId(remarkId);
		faultRemark.setUserId(UserUtil.getUser().getId());
		faultRemark.setName(UserUtil.getUser().getName());
		faultRemark.setFaultId(checkFault.getId());
		faultRemark.setCreateTime(LocalDateTime.now());
		faultRemark.setType(1);
		faultRemark.setRemark(checkFault.getFinishRemark());
		inno72CheckFaultRemarkMapper.insertSelective(faultRemark);
		String[] images = checkFault.getImages();
		if (images != null && images.length > 0) {
			for (int i = 0; i < images.length; i++) {
				Inno72CheckFaultImage faultImage = new Inno72CheckFaultImage();
				faultImage.setId(StringUtil.getUUID());
				faultImage.setRemarkId(remarkId);
				faultImage.setSort(i + 1);
				faultImage.setImage(ImageUtil.getLackImageUrl(images[i]));
				faultImage.setCreateTime(LocalDateTime.now());
				faultImage.setFaultId(checkFault.getId());
				inno72CheckFaultImageMapper.insertSelective(faultImage);
			}
		}
		Inno72CheckFault fault = inno72CheckFaultMapper.selectDetail(checkFault.getId());
		if(fault != null){
			Inno72Machine machine = inno72MachineMapper.getMachineById(fault.getMachineId());
			StringUtil.logger(CommonConstants.LOG_TYPE_SET_WORK,machine.getMachineCode(),"解决工单:巡检人员解决了"+machine.getLocaleStr()+"点位所在机器的工单");
		}

		return ResultGenerator.genSuccessResult();
	}

	@Override
	public List<Inno72CheckFault> findForPage(Inno72CheckFault inno72CheckFault) {
		Integer status = inno72CheckFault.getStatus();
		Inno72CheckUser checkUser = UserUtil.getUser();
		Map<String, Object> map = new HashMap<>();
		map.put("checkUserId", checkUser.getId());
		if (status != null && status != -1) {
			map.put("status", status);
		}
		Integer workType = inno72CheckFault.getWorkType();
		if (workType != null && workType != -1) {
			map.put("workType", workType);
		}

		List<Inno72CheckFault> list = inno72CheckFaultMapper.selectForPage(map);
		return list;
	}

	@Override
	public Result<String> upload(MultipartFile file) {
		return UploadUtil.uploadImage(file, "check");
	}

	@Override
	public Result<String> editRemark(Inno72CheckFault inno72CheckFault) {
		String faultId = inno72CheckFault.getId();
		String remark = inno72CheckFault.getRemark();
		String[] images = inno72CheckFault.getImages();
		Inno72CheckFaultRemark faultRemark = new Inno72CheckFaultRemark();
		String id = StringUtil.getUUID();
		faultRemark.setId(id);
		faultRemark.setFaultId(faultId);
		faultRemark.setUserId(UserUtil.getUser().getId());
		faultRemark.setType(1);
		faultRemark.setName(UserUtil.getUser().getName());
		faultRemark.setRemark(remark);
		faultRemark.setCreateTime(LocalDateTime.now());
		inno72CheckFaultRemarkMapper.insertSelective(faultRemark);
		if (images != null && images.length > 0) {
			for (int i = 0; i < images.length; i++) {
				Inno72CheckFaultImage image = new Inno72CheckFaultImage();
				image.setId(StringUtil.getUUID());
				image.setFaultId(faultId);
				image.setSort(i + 1);
				image.setRemarkId(id);
				image.setImage(ImageUtil.getLackImageUrl(images[i]));
				image.setCreateTime(LocalDateTime.now());
				inno72CheckFaultImageMapper.insertSelective(image);
			}
		}
		Inno72CheckFault fault = inno72CheckFaultMapper.selectDetail(faultId);
		if(fault != null){
			Inno72Machine machine = inno72MachineMapper.getMachineById(fault.getMachineId());
			StringUtil.logger(CommonConstants.LOG_TYPE_SET_WORK,machine.getMachineCode(),"操作工单:巡检人员"+UserUtil.getUser().getName()+"对"+machine.getLocaleStr()+"点位处机器的工单进行了回复");
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<Inno72CheckFault> getDetail(String faultId) {
		Inno72CheckFault fault = inno72CheckFaultMapper.selectDetail(faultId);
		List<Inno72CheckFaultRemark> remarkList = fault.getRemarkList();
		if (fault != null) {
			if (remarkList != null && remarkList.size() > 0) {
				for (Inno72CheckFaultRemark remark : remarkList) {
					int type = remark.getType();
					if (type == 2) {
						remark.setName("运营人员");
					}
					List<Inno72CheckFaultImage> imageList = remark.getImageList();
					if (imageList != null && imageList.size() > 0) {
						for (Inno72CheckFaultImage image : imageList) {
							String imageUrl = image.getImage();
							image.setImage(ImageUtil.getLongImageUrl(imageUrl));
						}
					}
				}
			}
			int status = fault.getStatus();
			String receiveId = fault.getReceiveId();
			if (status == 2 && StringUtil.isNotEmpty(receiveId) && receiveId.equals(UserUtil.getUser().getId())) {
				fault.setFinishShow(1);
			} else {
				fault.setFinishShow(-1);
			}

		}
		return ResultGenerator.genSuccessResult(fault);
	}

	@Override
	public Result<List<Inno72CheckFaultType>> getTypeList(String parentCode) {
		Condition condition = new Condition(Inno72CheckFaultType.class);
		if (StringUtil.isEmpty(parentCode)) {
			condition.createCriteria().andEqualTo("level", 1);
		} else {
			condition.createCriteria().andEqualTo("parentCode", parentCode);
		}
		List<Inno72CheckFaultType> list = inno72CheckFaultTypeMapper.selectByCondition(condition);
		return ResultGenerator.genSuccessResult(list);
	}

	@Override
	public Result<String> receive(Inno72CheckFault inno72CheckFault) {
		Inno72CheckUser checkUser = UserUtil.getUser();
		inno72CheckFault = inno72CheckFaultMapper.selectByPrimaryKey(inno72CheckFault.getId());
		if (inno72CheckFault == null) {
			return Results.failure("参数有误");
		}
		int status = inno72CheckFault.getStatus();
		if (status != 1) {
			return Results.failure("当前状态不可接单");
		}
		inno72CheckFault.setReceiveId(checkUser.getId());
		inno72CheckFault.setReceiveUser(checkUser.getName());
		inno72CheckFault.setTalkingTime(LocalDateTime.now());
		inno72CheckFault.setUpdateTime(LocalDateTime.now());
		inno72CheckFault.setStatus(2);// 处理中
		inno72CheckFaultMapper.updateByPrimaryKeySelective(inno72CheckFault);
		inno72CheckFault = inno72CheckFaultMapper.selectDetail(inno72CheckFault.getId());
		if(inno72CheckFault != null){
			Inno72Machine machine = inno72MachineMapper.getMachineById(inno72CheckFault.getMachineId());
			StringUtil.logger(CommonConstants.LOG_TYPE_SET_WORK,machine.getMachineCode(),"操作工单：巡检人员"+checkUser.getName()+"对"+machine.getLocaleStr()+"点位处的机器接收了工单");
		}
		return ResultGenerator.genSuccessResult();
	}

}
