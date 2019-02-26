package com.inno72.vo;

import java.io.Serializable;

/**
 *
 */
public class Inno72ConnectionFindActivityResultVo extends Inno72ConnectionBaseResultVo implements Serializable {
    private String gameVersion;
    private String gameVersionInno72;
    private String planCode;

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public String getGameVersionInno72() {
        return gameVersionInno72;
    }

    public void setGameVersionInno72(String gameVersionInno72) {
        this.gameVersionInno72 = gameVersionInno72;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

}
