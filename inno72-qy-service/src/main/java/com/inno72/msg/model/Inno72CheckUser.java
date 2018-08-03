package com.inno72.msg.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_check_user")
public class Inno72CheckUser {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码（预留）
     */
    private String password;

    /**
     * 身份证号
     */
    @Column(name = "card_no")
    private String cardNo;

    /**
     * 公司
     */
    private String enterprise;

    /**
     * 性别（1.男，2.女）
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    @Column(name = "head_image")
    private String headImage;

    /**
     * 企业号userID
     */
    @Column(name = "wechat_user_id")
    private String wechatUserId;

    /**
     * 企业号Openeid
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 创建人ID
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新人ID
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 是否删除（0.正常，1.删除，默认为0）
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 备注描述
     */
    private String remark;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取密码（预留）
     *
     * @return password - 密码（预留）
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码（预留）
     *
     * @param password 密码（预留）
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取身份证号
     *
     * @return card_no - 身份证号
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置身份证号
     *
     * @param cardNo 身份证号
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
     * 获取公司
     *
     * @return enterprise - 公司
     */
    public String getEnterprise() {
        return enterprise;
    }

    /**
     * 设置公司
     *
     * @param enterprise 公司
     */
    public void setEnterprise(String enterprise) {
        this.enterprise = enterprise;
    }

    /**
     * 获取性别（1.男，2.女）
     *
     * @return sex - 性别（1.男，2.女）
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别（1.男，2.女）
     *
     * @param sex 性别（1.男，2.女）
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取头像
     *
     * @return head_image - 头像
     */
    public String getHeadImage() {
        return headImage;
    }

    /**
     * 设置头像
     *
     * @param headImage 头像
     */
    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    /**
     * 获取企业号userID
     *
     * @return wechat_user_id - 企业号userID
     */
    public String getWechatUserId() {
        return wechatUserId;
    }

    /**
     * 设置企业号userID
     *
     * @param wechatUserId 企业号userID
     */
    public void setWechatUserId(String wechatUserId) {
        this.wechatUserId = wechatUserId;
    }

    /**
     * 获取企业号Openeid
     *
     * @return open_id - 企业号Openeid
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置企业号Openeid
     *
     * @param openId 企业号Openeid
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取创建人ID
     *
     * @return create_id - 创建人ID
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置创建人ID
     *
     * @param createId 创建人ID
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新人ID
     *
     * @return update_id - 更新人ID
     */
    public String getUpdateId() {
        return updateId;
    }

    /**
     * 设置更新人ID
     *
     * @param updateId 更新人ID
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取是否删除（0.正常，1.删除，默认为0）
     *
     * @return is_delete - 是否删除（0.正常，1.删除，默认为0）
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置是否删除（0.正常，1.删除，默认为0）
     *
     * @param isDelete 是否删除（0.正常，1.删除，默认为0）
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取备注描述
     *
     * @return remark - 备注描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注描述
     *
     * @param remark 备注描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}