package com.inno72.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "inno72_machine")
public class Inno72Machine {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
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
	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	@Column(name = "machine_status")
	private Integer machineStatus;

	@Column(name = "net_status")
	private Integer netStatus;

	@Column(name = "device_id")
	private String deviceId;

	@Column(name = "address")
	private String address;
	
	@Column(name = "bluetooth_address")
	private String bluetoothAddress;
	
	@Column(name = "open_status")
	private Integer openStatus;

	/**
	 * @return Id
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
	 * @return machine_id - 机器id
	 */
	public String getMachineCode() {
		return machineCode;
	}

	/**
	 * 设置机器id
	 *
	 * @param machineCode 机器id
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
	 * @param machineName 机器名称
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
	 * @param localeId 所属点位
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
	 * @param tag 机器所属标签
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
	 * @param createId 创建人
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
	 * @param updateId 更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime 创建时间
	 */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime 更新时间
	 */
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getMachineStatus() {
		return machineStatus;
	}

	public void setMachineStatus(Integer machineStatus) {
		this.machineStatus = machineStatus;
	}

	public Integer getNetStatus() {
		return netStatus;
	}

	public void setNetStatus(Integer netStatus) {
		this.netStatus = netStatus;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBluetoothAddress() {
		return bluetoothAddress;
	}

	public void setBluetoothAddress(String bluetoothAddress) {
		this.bluetoothAddress = bluetoothAddress;
	}

	public Integer getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(Integer openStatus) {
		this.openStatus = openStatus;
	}
}