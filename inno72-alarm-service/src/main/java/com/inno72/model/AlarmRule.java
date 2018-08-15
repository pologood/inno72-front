package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;
import com.inno72.common.LocalDateTimeConverter;

@Table(name = "alarm_rule")
public class AlarmRule {
    /**
     * uuid
     */
    @Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    /**
     * 规则名称
     */
    @NotNull(message = "规则名称不能为空!")
	@Length(max = 50, message = "规则名称超过最大长度")
	@Column(name = "name")
    private String name;

    /**
     * 规则描述
     */
	@NotNull(message = "规则描述不能为空!")
	@Length(max = 250, message = "规则描述超过最大长度")
	@Column(name = "description")
    private String description;

    /**
     * 规则类型	1 关键字 2 正则表达式
     */
    @Column(name = "rule_type")
	@Size(min = 1, max = 2, message = "规则类型超限")
	@NotNull(message = "规则类型不能为空!")
    private Integer ruleType;

    /**
     * 规则片段	
     */
    @Column(name = "rule_fragment")
	@NotNull(message = "规规则片段\t不能为空!")
    private String ruleFragment;

    /**
     * 规则所属项目名称	 务必跟项目名保持一致，不然无法报警
     */
    @Column(name = "app_name")
	@NotNull(message = "规则所属项目不能为空!")
    private String appName;

    /**
     * 负责人	
     */
    @Column(name = "director")
	@NotNull(message = "负责人不能为空!")
    private String director;

    /**
     * 发送时间段的开始时间	
     */
    @Column(name = "start_time")
	@NotNull(message = "发送时间段的开始时间不能为空!")
    private String startTime;

    /**
     * 发送时间段的结束时间	
     */
    @Column(name = "end_time")
	@NotNull(message = "发送时间段的开始时间不能为空!")
    private String endTime;

    @Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_user")
    private String updateUser;

    /**
     * 获取uuid
     *
     * @return id - uuid
     */
    public String getId() {
        return id;
    }

    /**
     * 设置uuid
     *
     * @param id uuid
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取规则名称
     *
     * @return name - 规则名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置规则名称
     *
     * @param name 规则名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取规则描述
     *
     * @return description - 规则描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置规则描述
     *
     * @param description 规则描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取规则类型	1 关键字 2 正则表达式
     *
     * @return rule_type - 规则类型	1 关键字 2 正则表达式
     */
    public Integer getRuleType() {
        return ruleType;
    }

    /**
     * 设置规则类型	1 关键字 2 正则表达式
     *
     * @param ruleType 规则类型	1 关键字 2 正则表达式
     */
    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }

    /**
     * 获取规则片段	
     *
     * @return rule_fragment - 规则片段	
     */
    public String getRuleFragment() {
        return ruleFragment;
    }

    /**
     * 设置规则片段	
     *
     * @param ruleFragment 规则片段	
     */
    public void setRuleFragment(String ruleFragment) {
        this.ruleFragment = ruleFragment;
    }

    /**
     * 获取规则所属项目名称	 务必跟项目名保持一致，不然无法报警

     *
     * @return app_name - 规则所属项目名称	 务必跟项目名保持一致，不然无法报警

     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置规则所属项目名称	 务必跟项目名保持一致，不然无法报警

     *
     * @param appName 规则所属项目名称	 务必跟项目名保持一致，不然无法报警

     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取负责人	
     *
     * @return director - 负责人	
     */
    public String getDirector() {
        return director;
    }

    /**
     * 设置负责人	
     *
     * @param director 负责人	
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * 获取发送时间段的开始时间	
     *
     * @return start_time - 发送时间段的开始时间	
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 设置发送时间段的开始时间	
     *
     * @param startTime 发送时间段的开始时间	
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取发送时间段的结束时间	
     *
     * @return end_time - 发送时间段的结束时间	
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置发送时间段的结束时间	
     *
     * @param endTime 发送时间段的结束时间	
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
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