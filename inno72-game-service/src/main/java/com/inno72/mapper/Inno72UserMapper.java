package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MachineGame;
import com.inno72.model.Inno72User;

public interface Inno72UserMapper extends Mapper<Inno72User> {

	Inno72User selectByUsername(String username);
}