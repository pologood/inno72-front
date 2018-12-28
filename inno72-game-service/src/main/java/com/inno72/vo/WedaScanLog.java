package com.inno72.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
@Document
public class WedaScanLog implements Serializable {
    @Id
    private String id;

    private String phone;//手机号



}
