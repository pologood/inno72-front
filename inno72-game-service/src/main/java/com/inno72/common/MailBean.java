package com.inno72.common;

import java.util.Vector;

/**
 * Created by zhouzengbao on 11/01/2018.
 */

public class MailBean {
    private String to; // 收件人
    private String from; // 发件人
    private String host; // SMTP主机
    private String username; // 发件人的用户名
    private String password; // 发件人的密码
    private String subject; // 邮件主题
    private String content; // 邮件正文
    Vector<String> file; // 多个附件
    private String filename; // 附件的文件名


    public MailBean() {
    }

    public MailBean(String from) {
        // 设置SMTP主机(163)，若用126，则设为：smtp.126.com
        this.host = "smtp.exmail.qq.com";
        // 设置发件人邮箱的用户名
        this.username = "diansjsj@pinwheelmedical.com";
        // 设置发件人邮箱的密码，需将*号改成正确的密码
        this.password = "Yyxk@2016";
        // 设置发件人的邮箱
        this.from = "diansjsj@pinwheelmedical.com";
        // 设置收件人的邮箱
        this.to = from;
        // 设置邮件的主题
        this.subject = "北京越洋新康科技发展有限公司";
        // 设置邮件的正文
        this.content = "邮件内容为小风车送检报告附件。请查收!";

        this.from = "diansjsj@pinwheelmedical.com";
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Vector<String> getFile() {
        return file;
    }

    public void attachFile(String fileName) {
        if (file == null){
            file = new Vector<String>();
        }
        file.addElement(fileName);
    }
}
