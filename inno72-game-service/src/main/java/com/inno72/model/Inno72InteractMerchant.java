package com.inno72.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "inno72_interact_merchant")
public class Inno72InteractMerchant implements Serializable {
    private static final long serialVersionUID = 6235996603233107574L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 互动ID
     */
    @Column(name = "interact_id")
    private String interactId;

    /**
     * 商户ID
     */
    @Column(name = "merchant_id")
    private String merchantId;

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
     * 获取互动ID
     *
     * @return interact_id - 互动ID
     */
    public String getInteractId() {
        return interactId;
    }

    /**
     * 设置互动ID
     *
     * @param interactId 互动ID
     */
    public void setInteractId(String interactId) {
        this.interactId = interactId;
    }

    /**
     * 获取商户ID
     *
     * @return merchant_id - 商户ID
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * 设置商户ID
     *
     * @param merchantId 商户ID
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}