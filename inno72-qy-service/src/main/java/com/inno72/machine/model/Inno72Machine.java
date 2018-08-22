package com.inno72.machine.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.vo.CheckUserVo;
import com.inno72.check.vo.FaultVo;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import com.inno72.machine.vo.SupplyChannelVo;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "inno72_machine")
public class Inno72Machine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 机器id
	 */
	@Column(name = "machine_code")
	private String machineCode;

	/**
	 * 机器名称
	 */
	@Column(name = "machine_name")
	private String machineName;

	/**
	 * 所属点位
	 */
	@Column(name = "locale_id")
	private String localeId;

	/**
	 * 机器所属标签
	 */
	@Column(name = "tag")
	private String tag;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 机器状态 1：未出厂（厂内安装测试）2:厂内测试通过；3:H5录入状态；4:正常
	 */
	@Column(name = "machine_status")
	private Integer machineStatus;

	/**
	 * 网络灯状态 0:关闭，1:开启
	 */
	@Column(name = "net_status")
	private Integer netStatus;

	/**
	 * 设备id
	 */
	@Column(name = "device_id")
	private String deviceId;

	@Transient
	private String address;

	@Transient
	private Integer lackGoodsStatus;

	@Transient
	private int lackGoodsCount;

	@Transient
	private int totalGoodsCount;

	@Transient
	private String localeStr;

	@Transient
	private Integer faultStatus;

	@Transient
	private int signInStatus;

	@Transient
	private List<SupplyChannelVo> supplyChannelVoList;

	@Transient
	private List<CheckUserVo> checkUserVoList;

	@Transient
	private List<FaultVo> faultVoList;

	@Transient
	private List<Inno72CheckSignIn> signInList;

	public enum Machine_Status {
		// 在厂测试
		INFACTORY(1),
		// 通过测试
		PASSTEST(2),
		// 设置在点位
		INLOCAL(3),
		// 正常
		NORMAL(4);

		private int v;

		private Machine_Status(int v) {
			this.v = v;
		}

		public int v() {
			return this.v;
		}

		public static Machine_Status get(int v) {
			for (Machine_Status c : Machine_Status.values()) {
				if (c.v == v) {
					return c;
				}
			}
			return null;
		}
	}

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取机器id
	 *
	 * @return machine_code - 机器id
	 */
	public String getMachineCode() {
		return machineCode;
	}

	/**
	 * 设置机器id
	 *
	 * @param machineCode
	 *            机器id
	 */
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	/**
	 * 获取机器名称
	 *
	 * @return machine_name - 机器名称
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * 设置机器名称
	 *
	 * @param machineName
	 *            机器名称
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	/**
	 * 获取所属点位
	 *
	 * @return locale_id - 所属点位
	 */
	public String getLocaleId() {
		return localeId;
	}

	/**
	 * 设置所属点位
	 *
	 * @param localeId
	 *            所属点位
	 */
	public void setLocaleId(String localeId) {
		this.localeId = localeId;
	}

	/**
	 * 获取机器所属标签
	 *
	 * @return tag - 机器所属标签
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * 设置机器所属标签
	 *
	 * @param tag
	 *            机器所属标签
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * 获取创建人
	 *
	 * @return create_id - 创建人
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置创建人
	 *
	 * @param createId
	 *            创建人
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId
	 *            更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取机器状态 1：未出厂（厂内安装测试）2:厂内测试通过；3:H5录入状态；4:正常
	 *
	 * @return machine_status - 机器状态 1：未出厂（厂内安装测试）2:厂内测试通过；3:H5录入状态；4:正常
	 */
	public Integer getMachineStatus() {
		return machineStatus;
	}

	/**
	 * 设置机器状态 1：未出厂（厂内安装测试）2:厂内测试通过；3:H5录入状态；4:正常
	 *
	 * @param machineStatus
	 *            机器状态 1：未出厂（厂内安装测试）2:厂内测试通过；3:H5录入状态；4:正常
	 */
	public void setMachineStatus(Integer machineStatus) {
		this.machineStatus = machineStatus;
	}

	/**
	 * 获取网络灯状态 0:关闭，1:开启
	 *
	 * @return net_status - 网络灯状态 0:关闭，1:开启
	 */
	public Integer getNetStatus() {
		return netStatus;
	}

	/**
	 * 设置网络灯状态 0:关闭，1:开启
	 *
	 * @param netStatus
	 *            网络灯状态 0:关闭，1:开启
	 */
	public void setNetStatus(Integer netStatus) {
		this.netStatus = netStatus;
	}

	/**
	 * 获取设备id
	 *
	 * @return device_id - 设备id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * 设置设备id
	 *
	 * @param deviceId
	 *            设备id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public Integer getLackGoodsStatus() {
		return lackGoodsStatus;
	}

	public void setLackGoodsStatus(Integer lackGoodsStatus) {
		this.lackGoodsStatus = lackGoodsStatus;
	}

	public List<SupplyChannelVo> getSupplyChannelVoList() {
		return supplyChannelVoList;
	}

	public void setSupplyChannelVoList(List<SupplyChannelVo> supplyChannelVoList) {
		this.supplyChannelVoList = supplyChannelVoList;
	}

	public int getLackGoodsCount() {
		return lackGoodsCount;
	}

	public void setLackGoodsCount(int lackGoodsCount) {
		this.lackGoodsCount = lackGoodsCount;
	}

	public String getLocaleStr() {
		return localeStr;
	}

	public void setLocaleStr(String localeStr) {
		this.localeStr = localeStr;
	}

	public List<CheckUserVo> getCheckUserVoList() {
		return checkUserVoList;
	}

	public void setCheckUserVoList(List<CheckUserVo> checkUserVoList) {
		this.checkUserVoList = checkUserVoList;
	}

	public List<FaultVo> getFaultVoList() {
		return faultVoList;
	}

	public void setFaultVoList(List<FaultVo> faultVoList) {
		this.faultVoList = faultVoList;
	}

	public Integer getFaultStatus() {
		return faultStatus;
	}

	public void setFaultStatus(Integer faultStatus) {
		this.faultStatus = faultStatus;
	}

	public List<Inno72CheckSignIn> getSignInList() {
		return signInList;
	}

	public void setSignInList(List<Inno72CheckSignIn> signInList) {
		this.signInList = signInList;
	}

	public int getSignInStatus() {
		return signInStatus;
	}

	public void setSignInStatus(int signInStatus) {
		this.signInStatus = signInStatus;
	}

	public int getTotalGoodsCount() {
		return totalGoodsCount;
	}

	public void setTotalGoodsCount(int totalGoodsCount) {
		this.totalGoodsCount = totalGoodsCount;
	}
}