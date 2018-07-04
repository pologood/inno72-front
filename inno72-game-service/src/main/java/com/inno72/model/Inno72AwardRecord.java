package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_award_record")
public class Inno72AwardRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
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
     * 获奖时间
     */
    @Column(name = "award_time")
    private LocalDateTime awardTime;

    /**
     * 分数(不需要分数体现的游戏-1)
     */
    private Integer score;

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
     * 获取获奖时间
     *
     * @return award_time - 获奖时间
     */
    public LocalDateTime getAwardTime() {
        return awardTime;
    }

    /**
     * 设置获奖时间
     *
     * @param awardTime 获奖时间
     */
    public void setAwardTime(LocalDateTime awardTime) {
        this.awardTime = awardTime;
    }

    /**
     * 获取分数(不需要分数体现的游戏-1)
     *
     * @return score - 分数(不需要分数体现的游戏-1)
     */
    public Integer getScore() {
        return score;
    }

    /**
     * 设置分数(不需要分数体现的游戏-1)
     *
     * @param score 分数(不需要分数体现的游戏-1)
     */
    public void setScore(Integer score) {
        this.score = score;
    }
}