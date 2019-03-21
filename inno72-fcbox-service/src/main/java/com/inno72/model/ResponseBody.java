package com.inno72.model;

import java.util.List;

public class ResponseBody {

	private String id;

	private String requestId;

	private int code;
	private String engDesc;
	private String chnDesc;
	private String detail;
	private String totalCount;
	private String pageSize;
	private String pageNo;

	private List<Content> content;

	public ResponseBody(String id, String requestId, int code, String engDesc, String chnDesc, String detail,
			String totalCount, String pageSize, String pageNo, List<Content> content) {
		this.id = id;
		this.requestId = requestId;
		this.code = code;
		this.engDesc = engDesc;
		this.chnDesc = chnDesc;
		this.detail = detail;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public ResponseBody() {
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getEngDesc() {
		return engDesc;
	}

	public void setEngDesc(String engDesc) {
		this.engDesc = engDesc;
	}

	public String getChnDesc() {
		return chnDesc;
	}

	public void setChnDesc(String chnDesc) {
		this.chnDesc = chnDesc;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public List<Content> getContent() {
		return content;
	}

	public void setContent(List<Content> content) {
		this.content = content;
	}
}
