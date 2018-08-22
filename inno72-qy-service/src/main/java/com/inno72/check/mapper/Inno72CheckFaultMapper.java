package com.inno72.check.mapper;

import com.inno72.check.model.Inno72CheckFault;
import com.inno72.common.Mapper;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckFaultMapper extends Mapper<Inno72CheckFault> {
    List<Inno72CheckFault> selectForPage(Map<String, Object> map);

    Inno72CheckFault selectDetail(String faultId);
}
