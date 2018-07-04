package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Authentication;

public interface Inno72AuthenticationMapper extends Mapper<Inno72Authentication> {

	Inno72Authentication selectByUsername(String username);
}