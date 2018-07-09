package com.inno72.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_machine_game")
public class Inno72MachineGame {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select uuid()")
    private String id;

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
     * @return Id
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
}