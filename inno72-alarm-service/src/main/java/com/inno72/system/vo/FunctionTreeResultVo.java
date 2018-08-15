package com.inno72.system.vo;

import java.util.List;

public class FunctionTreeResultVo {
	private List<String> functions;
	private FunctionTreeVo tree;

	public List<String> getFunctions() {
		return functions;
	}

	public void setFunctions(List<String> functions) {
		this.functions = functions;
	}

	public FunctionTreeVo getTree() {
		return tree;
	}

	public void setTree(FunctionTreeVo tree) {
		this.tree = tree;
	}

	public static class FunctionTreeVo {
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

}
