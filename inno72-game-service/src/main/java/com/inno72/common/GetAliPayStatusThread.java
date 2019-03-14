package com.inno72.common;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.UuidUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72MachineConnectionMsgMapper;
import com.inno72.model.Inno72MachineConnectionMsg;
import com.inno72.model.Inno72OrderAlipay;
import com.inno72.service.ConnectionMsgService;
import com.inno72.service.Inno72ConnectionService;
import com.inno72.service.Inno72GameApiService;
import com.inno72.vo.Inno72ConnectionPayVo;
import com.inno72.vo.MachineApiVo;
import com.inno72.vo.PushRequestVo;

@Component
public class GetAliPayStatusThread {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetAliPayStatusThread.class);

	@Value("${env}")
	private String env;

	@Resource
	private Inno72GameApiService inno72GameApiService;
	private static Inno72GameApiService _inno72GameApiService;

	@Resource
	private ConnectionMsgService connectionMsgService;
	private static ConnectionMsgService _connectionMsgService;

	@Resource
	private Inno72MachineConnectionMsgMapper inno72MachineConnectionMsgMapper;
	private static Inno72MachineConnectionMsgMapper _inno72MachineConnectionMsgMapper;

	@Resource
	private Inno72ConnectionService inno72ConnectionService;
	private static Inno72ConnectionService _inno72ConnectionService;

	@PostConstruct
	public void init(){
		_inno72GameApiService = inno72GameApiService;
		_connectionMsgService = connectionMsgService;
		_inno72MachineConnectionMsgMapper = inno72MachineConnectionMsgMapper;
		_inno72ConnectionService = inno72ConnectionService;
	}

	public static ExecutorService service = Executors.newFixedThreadPool(4);

	public static void run(Inno72OrderAlipay inno72OrderAlipay){

		GetAliPayStatusThreadWork getAliPayStatusThreadWork = new GetAliPayStatusThreadWork(inno72OrderAlipay);

		service.execute(new Thread(getAliPayStatusThreadWork));

	}

	static class GetAliPayStatusThreadWork implements Runnable{

		private Inno72OrderAlipay inno72OrderAlipay;

		GetAliPayStatusThreadWork(Inno72OrderAlipay inno72OrderAlipay) {
			this.inno72OrderAlipay = inno72OrderAlipay;
		}

		@Override
		public void run() {
			Boolean aliOrderPayStatus = getAliOrderPayStatus(inno72OrderAlipay);

		}

		private Boolean getAliOrderPayStatus(Inno72OrderAlipay order) {
			LOGGER.info("getAliOrderPayStatus order = {} ", JSON.toJSONString(order));
			try {
				MachineApiVo vo = new MachineApiVo();
				vo.setSessionUuid(order.getMachineCode());
				Result<Object> objectResult = _inno72GameApiService.orderPolling(vo);

				LocalDateTime now = LocalDateTime.now();

				if (objectResult.getCode() == Result.SUCCESS){
					String s = Optional.ofNullable(objectResult.getData()).map(Object::toString).orElse("");
					String model = Optional.ofNullable(JSON.parseObject(s).get("model")).map(Object::toString).orElse("");
					if (StringUtil.notEmpty(model) && model.equals("false")){

						Thread.sleep(1000);
						LocalDateTime now1 = LocalDateTime.now();
						Duration between = Duration.between(now, now1);
						if (between.toMinutes() < 5){
							getAliOrderPayStatus(inno72OrderAlipay);
						}

					}
					return Boolean.parseBoolean(model);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}

		private void sendMsg(Inno72OrderAlipay order) throws IOException {
			Inno72ConnectionPayVo payvo = new Inno72ConnectionPayVo();
			long l = System.currentTimeMillis();
			payvo.setActivityId(order.getActivityId());
			payvo.setMachineCode(order.getMachineCode());
			payvo.setVersion(l);
			payvo.setType(Inno72MachineConnectionMsg.TYPE_ENUM.PAY.getKey());
			PushRequestVo vo = new PushRequestVo();
			vo.setData(JsonUtil.toJson(payvo));
			vo.setTargetCode(order.getMachineCode());
			String request = JsonUtil.toJson(vo);
			Inno72MachineConnectionMsg msg = new Inno72MachineConnectionMsg();
			msg.setMachineCode(order.getMachineCode());
			msg.setStatus(Inno72MachineConnectionMsg.STATUS_ENUM.COMMIT.getKey());
			List<Inno72MachineConnectionMsg> list = _inno72MachineConnectionMsgMapper.select(msg);
			if(list.size()>0){
				for(Inno72MachineConnectionMsg inno72MachineConnectionMsg:list){
					_inno72MachineConnectionMsgMapper.updateStatusById(inno72MachineConnectionMsg.getId(),Inno72MachineConnectionMsg.STATUS_ENUM.EXPIRE.getKey());
				}
			}
			msg.setMachineCode(order.getMachineCode());
			msg.setStatus(Inno72MachineConnectionMsg.STATUS_ENUM.COMMIT.getKey());
			msg.setId(UuidUtil.getUUID32());
			msg.setActivityId(order.getActivityId());
			msg.setCreateTime(new Date());
			msg.setUpdateTime(msg.getCreateTime());
			msg.setMsg(request);
			msg.setTimes(1);
			msg.setType(Inno72MachineConnectionMsg.TYPE_ENUM.PAY.getKey());
			msg.setVersion(l);
			LOGGER.info("插入消息 {}", JSON.toJSONString(msg));
			_inno72MachineConnectionMsgMapper.insert(msg);
			_inno72ConnectionService.sendMsg(msg);
		}
	}



}
