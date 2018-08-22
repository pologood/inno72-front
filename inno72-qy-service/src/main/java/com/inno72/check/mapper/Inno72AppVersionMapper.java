package com.inno72.check.mapper;

import com.inno72.check.model.Inno72AppVersion;
import com.inno72.common.Mapper;

import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72AppVersionMapper extends Mapper<Inno72AppVersion> {


    Inno72AppVersion selectNowVersion(Map<String, Object> map);
}
