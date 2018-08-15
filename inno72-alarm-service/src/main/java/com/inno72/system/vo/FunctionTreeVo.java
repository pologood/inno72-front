package com.inno72.system.vo;

import java.util.List;

public class FunctionTreeVo {
	private String title;
	private String id;
	private List<FunctionTreeVo> children;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<FunctionTreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<FunctionTreeVo> children) {
		this.children = children;
	}

}
