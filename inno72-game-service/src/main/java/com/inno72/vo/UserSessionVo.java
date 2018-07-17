package com.inno72.vo;

import lombok.Data;

@Data
public class UserSessionVo {

	private String mid;
	private String userNick;
	private String userId;
	private String accessToken;
	private String gameId;
	private String sessionUuid;

	/**
	 * 查询游戏结果对应的货道时存入
	 */
	private String gameReport;
	/**
	 * 下单存入
	 */
	private String inno72OrderId;

	public UserSessionVo(String mid, String userNick, String userId, String access_token, String gameId,
			String sessionUuid) {
		super();
		this.mid = mid;
		this.userNick = userNick;
		this.userId = userId;
		this.accessToken = access_token;
		this.gameId = gameId;
		this.sessionUuid = sessionUuid;
	}

	public UserSessionVo() {
		super();
	}


}
