package com.inno72.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "inno72_game_user_login")
public class Inno72GameUserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 图片URL
     */
    private String url;

    /**
     * 性别 0：女， 1：男
     */
    private Integer sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 活动ID
     */
    @Column(name = "activity_id")
    private String activityId;

    /**
     * 机器ID
     */
    @Column(name = "machine_id")
    private String machineId;

    /**
     * 登录时间
     */
    @Column(name = "login_time")
    private Date loginTime;

    /**
     * 是否处理过 0：否， 1：是
     */
    private Integer processed;

    public static final Integer PROCESSED_NO = 0;
    public static final Integer PROCESSED_YES = 1;

    /**
     * 操作系统 0：IOS； 1：ANDROID
     */
    @Column(name = "operating_system")
    private Integer operatingSystem;

    /**
     * 手机型号
     */
    @Column(name = "phone_model")
    private String phoneModel;

    /**
     * 扫描软件
     */
    @Column(name = "scan_software")
    private String scanSoftware;

    /**
     * 点位
     */
    @Column(name = "locale_id")
    private String localeId;

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
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取图片URL
     *
     * @return url - 图片URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置图片URL
     *
     * @param url 图片URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取性别 0：女， 1：男
     *
     * @return sex - 性别 0：女， 1：男
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别 0：女， 1：男
     *
     * @param sex 性别 0：女， 1：男
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取年龄
     *
     * @return age - 年龄
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置年龄
     *
     * @param age 年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取活动ID
     *
     * @return activity_id - 活动ID
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * 设置活动ID
     *
     * @param activityId 活动ID
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    /**
     * 获取机器ID
     *
     * @return machine_id - 机器ID
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * 设置机器ID
     *
     * @param machineId 机器ID
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * 获取登录时间
     *
     * @return login_time - 登录时间
     */
    public Date getLoginTime() {
        return loginTime;
    }

    /**
     * 设置登录时间
     *
     * @param loginTime 登录时间
     */
    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * 获取是否处理过 0：否， 1：是
     *
     * @return processed - 是否处理过 0：否， 1：是
     */
    public Integer getProcessed() {
        return processed;
    }

    /**
     * 设置是否处理过 0：否， 1：是
     *
     * @param processed 是否处理过 0：否， 1：是
     */
    public void setProcessed(Integer processed) {
        this.processed = processed;
    }

    /**
     * 获取操作系统 0：IOS； 1：ANDROID
     *
     * @return operating_system - 操作系统 0：IOS； 1：ANDROID
     */
    public Integer getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * 设置操作系统 0：IOS； 1：ANDROID
     *
     * @param operatingSystem 操作系统 0：IOS； 1：ANDROID
     */
    public void setOperatingSystem(Integer operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * 获取手机型号
     *
     * @return phone_model - 手机型号
     */
    public String getPhoneModel() {
        return phoneModel;
    }

    /**
     * 设置手机型号
     *
     * @param phoneModel 手机型号
     */
    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    /**
     * 获取扫描软件
     *
     * @return scan_software - 扫描软件
     */
    public String getScanSoftware() {
        return scanSoftware;
    }

    /**
     * 设置扫描软件
     *
     * @param scanSoftware 扫描软件
     */
    public void setScanSoftware(String scanSoftware) {
        this.scanSoftware = scanSoftware;
    }

    /**
     * 获取点位
     *
     * @return locale_id - 点位
     */
    public String getLocaleId() {
        return localeId;
    }

    /**
     * 设置点位
     *
     * @param localeId 点位
     */
    public void setLocaleId(String localeId) {
        this.localeId = localeId;
    }
}