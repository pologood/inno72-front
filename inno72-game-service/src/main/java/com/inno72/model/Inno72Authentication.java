package com.inno72.model;

import javax.persistence.*;

@Table(name = "inno72_authentication")
public class Inno72Authentication {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 用户名
	 */
	@Column(name = "uName")
	private String uName;

	/**
	 * 用户密码
	 */
	@Column(name = "uPassword")
	private String uPassword;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public String getuPassword() {
		return uPassword;
	}

	public void setuPassword(String uPassword) {
		this.uPassword = uPassword;
	}

}