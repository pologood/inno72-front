package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Points;

public interface PointsMapper  extends Mapper<Points> {
	void insertS(List<Points> pointss);
}
