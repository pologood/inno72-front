package com.inno72.vo;

import java.io.Serializable;

public class PayParamVo implements Serializable {

    private static final long serialVersionUID = 7818682169207761016L;
    private String spId;
    private String subject;//支付标题 写死 肚肚机
    private String outTradeNo; //业务的订单Id
    private Long totalFee;//总费用 单位:分
    private String notifyUrl;//回调地址
    private Integer type;//1:支付宝 2:微信
    private Integer terminalType;//1:qrCode 2:wap 3:app 4:小程序 5:公众号
    private Integer transTimeout;//可支付时长 单位:分钟
    private Integer qrTimeout;//qrCode有效期时长 单位:分钟
    private String unitPrice;//单价 单位:分
    private Integer quantity;//数量
    private String clientIp;//支付端ip QRCODE的情况下必填
    private String sign;//签名

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(Integer terminalType) {
        this.terminalType = terminalType;
    }

    public Integer getTransTimeout() {
        return transTimeout;
    }

    public void setTransTimeout(Integer transTimeout) {
        this.transTimeout = transTimeout;
    }

    public Integer getQrTimeout() {
        return qrTimeout;
    }

    public void setQrTimeout(Integer qrTimeout) {
        this.qrTimeout = qrTimeout;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
