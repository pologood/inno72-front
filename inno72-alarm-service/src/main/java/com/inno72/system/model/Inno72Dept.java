package com.inno72.system.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "inno72_dept")
public class Inno72Dept {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	private String name;

	private Integer seq;

	@Column(name = "parent_id")
	private String parentId;

	@Transient
	private String parentName;

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
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return seq
	 */
	public Integer getSeq() {
		return seq;
	}

	/**
	 * @param seq
	 */
	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	/**
	 * @return parent_id
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}