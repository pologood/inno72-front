package com.inno72.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;
import com.inno72.common.LocalDateConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "inno72_game_user")
public class Inno72GameUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
	private String id;

	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
	private LocalDateTime createTime;

	public Inno72GameUser() {
		this.createTime = LocalDateTime.now();
	}

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

}