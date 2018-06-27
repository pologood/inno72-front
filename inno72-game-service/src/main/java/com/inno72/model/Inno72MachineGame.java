package com.inno72.model;

import javax.persistence.*;

@Table(name = "inno72_machine_game")
public class Inno72MachineGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 机器id
     */
    @Column(name = "machine_id")
    private Long machineId;

    /**
     * 游戏id
     */
    @Column(name = "game_id")
    private Long gameId;

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
     * 获取机器id
     *
     * @return machine_id - 机器id
     */
    public Long getMachineId() {
        return machineId;
    }

    /**
     * 设置机器id
     *
     * @param machineId 机器id
     */
    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    /**
     * 获取游戏id
     *
     * @return game_id - 游戏id
     */
    public Long getGameId() {
        return gameId;
    }

    /**
     * 设置游戏id
     *
     * @param gameId 游戏id
     */
    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}