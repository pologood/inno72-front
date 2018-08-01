package com.inno72.model;

import javax.persistence.*;

@Table(name = "inno72_game_result_goods")
public class Inno72GameResultGoods {
	@Id
	@Column(name = "Id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 机器id
	 */
	@Column(name = "machine_id")
	private String machineId;

	/**
	 * 游戏id
	 */
	@Column(name = "game_id")
	private String gameId;

	/**
	 * 有些结果code
	 */
	@Column(name = "result_code")
	private Integer resultCode;

	/**
	 * 商品id
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * @return Id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取机器id
	 *
	 * @return machine_id - 机器id
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * 设置机器id
	 *
	 * @param machineId 机器id
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * 获取游戏id
	 *
	 * @return game_id - 游戏id
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * 设置游戏id
	 *
	 * @param gameId 游戏id
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	/**
	 * 获取有些结果code
	 *
	 * @return result_code - 有些结果code
	 */
	public Integer getResultCode() {
		return resultCode;
	}

	/**
	 * 设置有些结果code
	 *
	 * @param resultCode 有些结果code
	 */
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 获取商品id
	 *
	 * @return goods_id - 商品id
	 */
	public String getGoodsId() {
		return goodsId;
	}

	/**
	 * 设置商品id
	 *
	 * @param goodsId 商品id
	 */
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
}