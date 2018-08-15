package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;
import com.inno72.common.LocalDateTimeConverter;

@Table(name = "alarm_user")
public class AlarmUser {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    /**
     * 用户名
     */
	@Column(name = "name")
    private String name;
    /**
     * 用户名
     */
	@Column(name = "login_name")
    private String loginName;

    /**
     * 联系方式 JSON
     */
	@Column(name = "contact")
    private String contact;

    /**
     * 1有效 0删除
     */
    @Column(name = "del_flag")
    private Integer delFlag;

    @Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    @Column(name = "update_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime updateTime;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_user")
    private String updateUser;

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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
     * 获取用户名
     *
     * @return name - 用户名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置用户名
     *
     * @param name 用户名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取联系方式 JSON
     *
     * @return contact - 联系方式 JSON
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置联系方式 JSON
     *
     * @param contact 联系方式 JSON
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 获取1有效 0删除
     *
     * @return del_flag - 1有效 0删除
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * 设置1有效 0删除
     *
     * @param delFlag 1有效 0删除
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * @return create_time
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return create_user
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return update_user
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }


}