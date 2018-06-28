package com.inno72.vo;

import java.util.List;

import com.inno72.model.Inno72Game;
import com.inno72.model.Inno72Machine;

import lombok.Data;

@Data
public class Inno72MachineVo extends Inno72Machine{
	
	List<Inno72Game> inno72Games;

	
}
