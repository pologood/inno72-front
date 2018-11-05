package com.inno72.vo;

import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.alibaba.fastjson.JSON;

import lombok.Data;

@Data
public class Inno72TaoBaoCheckDataVo {


	/** sessionUuid */
	@NotNull(message = "登录信息不存在!")
	private String sessionUuid;
	/** traceId */
	private String traceId;

	/** 活动ID */
	private String activityId;

	/** 活动名称 */
	private String activityName;

	/** 机器ID */
	private String machineCode;
	public String getMachineCode(){
		return this.sessionUuid;
	}
	/** 省 */
	private String provence;

	/** 市 */
	private String city;

	/** 区 */
	private String district;

	/** 点位 */
	private String point;

	/** userID */
	private String userId;

	/** 用户昵称 */
	private String nickName;

	/** 时间2(到达服务器时间) */
	private String serviceTime;

	@NotNull(message = "消息类型不能为空!")
	@Length(max = 6, min = 6, message = "非法类型")
	private String type;


	public static enum ENUM_INNO72_TAOBAO_CHECK_DATA_VO_TYPE{
		LOGIN("001001","登录"),
		ORDER("002001","下单"),
		SHIPMENT("003001","出货"),
		CONCERN("004001","抽奖"),
		;
		private String type;
		private String desc;

		ENUM_INNO72_TAOBAO_CHECK_DATA_VO_TYPE(String type, String desc) {
			this.type = type;
			this.desc = desc;
		}

		public String getType() {
			return type;
		}

		public String getDesc() {
			return desc;
		}
	}

	/** 品牌ID(seller_id) */
	private String sellerId;

	/** 品牌名称 */
	private String sellerName;

	/** 商品ID(商品code) */
	private String goodsId;

	/** 商品名称 */
	private String goodsName;

	/** playCode */
	private String playCode;

	private String requestId;

	private String reqBody;
	private Map<String, Object> reqBodyObject;

	private String rspBody;
	private Map<String, Object> rspBodyObject;

	public Inno72TaoBaoCheckDataVo(String type, String sessionUuid) {
		this.machineCode = sessionUuid;
		this.type = type;
		this.reqBodyObject = JSON.parseObject(reqBody);
		this.rspBodyObject = JSON.parseObject(rspBody);
	}

	public Inno72TaoBaoCheckDataVo() {
	}

	public Inno72TaoBaoCheckDataVo buildBaseInformation(String traceId, String activityId, String activityName, String provence, String city,
			String district, String point, String userId, String nickName, String sellerId,
			String sellerName, String goodsId, String goodsName, String playCode, Inno72TaoBaoCheckDataVo vo) {

		vo.setTraceId(traceId);
		vo.setActivityId(activityId);
		vo.setActivityName(activityName);
		vo.setProvence(provence);
		vo.setCity(city);
		vo.setDistrict(district );
		vo.setPoint(point );
		vo.setUserId(userId );
		vo.setNickName(nickName );
		vo.setSellerId(sellerId );
		vo.setSellerName(sellerName );
		vo.setGoodsId(goodsId );
		vo.setGoodsName(goodsName );
		vo.setPlayCode(playCode );
		vo.setMachineCode(vo.getSessionUuid());
		vo.setReqBodyObject(JSON.parseObject(Optional.ofNullable(vo.getReqBody()).orElse("")));
		vo.setRspBodyObject(JSON.parseObject(Optional.ofNullable(vo.getRspBody()).orElse("")));

		return this;
	}
}
