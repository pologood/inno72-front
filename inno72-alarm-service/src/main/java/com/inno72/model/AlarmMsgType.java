package com.inno72.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Table(name = "alarm_msg_type")
public class AlarmMsgType {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    /**
     * 发送方式名称
     */
	@Column(name = "`name`")
	@NotNull(message = "名称不能为空")
    private String name;

    /**
     * 发送方式key（根据这个key取联系方式的value）
     */
	@Column(name = "`key`")
	@NotNull(message = "发送方式不能为空")
    private String key;

	@Column(name = "`code`")
	private String code;

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
     * 获取发送方式名称
     *
     * @return name - 发送方式名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置发送方式名称
     *
     * @param name 发送方式名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取发送方式key（根据这个key取联系方式的value）
     *
     * @return key - 发送方式key（根据这个key取联系方式的value）
     */
    public String getKey() {
        return key;
    }

    /**
     * 设置发送方式key（根据这个key取联系方式的value）
     *
     * @param key 发送方式key（根据这个key取联系方式的value）
     */
    public void setKey(String key) {
        this.key = key;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}