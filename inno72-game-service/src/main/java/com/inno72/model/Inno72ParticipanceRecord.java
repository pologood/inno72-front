package com.inno72.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "inno72_participance_record")
public class Inno72ParticipanceRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 参与者ID
	 */
	@Column(name = "participant_id")
	private String participantId;

	/**
	 * 机器ID
	 */
	@Column(name = "machine_id")
	private Long machineId;

	/**
	 * 游戏ID
	 */
	@Column(name = "game_id")
	private Long gameId;

	/**
	 * 参与时间
	 */
	@Column(name = "participance_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime participanceTime;

	/**
	 * @return id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取参与者ID
	 *
	 * @return participant_id - 参与者ID
	 */
	public String getParticipantId() {
		return participantId;
	}

	/**
	 * 设置参与者ID
	 *
	 * @param participantId 参与者ID
	 */
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	/**
	 * 获取机器ID
	 *
	 * @return machine_id - 机器ID
	 */
	public Long getMachineId() {
		return machineId;
	}

	/**
	 * 设置机器ID
	 *
	 * @param machineId 机器ID
	 */
	public void setMachineId(Long machineId) {
		this.machineId = machineId;
	}

	/**
	 * 获取游戏ID
	 *
	 * @return game_id - 游戏ID
	 */
	public Long getGameId() {
		return gameId;
	}

	/**
	 * 设置游戏ID
	 *
	 * @param gameId 游戏ID
	 */
	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	/**
	 * 获取参与时间
	 *
	 * @return participance_time - 参与时间
	 */
	public LocalDateTime getParticipanceTime() {
		return participanceTime;
	}

	/**
	 * 设置参与时间
	 *
	 * @param participanceTime 参与时间
	 */
	public void setParticipanceTime(LocalDateTime participanceTime) {
		this.participanceTime = participanceTime;
	}
}