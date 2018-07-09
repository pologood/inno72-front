package com.inno72.vo;

import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72Machine;

public class Inno72MachineVo extends Inno72Machine {

	private boolean isReload;

	private Inno72Game inno72Games;

	private String brandName;


	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public boolean isReload() {
		return isReload;
	}

	public void setReload(boolean isReload) {
		this.isReload = isReload;
	}

	public Inno72Game getInno72Games() {
		return inno72Games;
	}

	public void setInno72Games(Inno72Game inno72Games) {
		this.inno72Games = inno72Games;
	}


}
