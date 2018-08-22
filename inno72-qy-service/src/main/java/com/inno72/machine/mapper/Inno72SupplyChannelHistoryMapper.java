package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72SupplyChannelHistory;
import com.inno72.machine.vo.WorkOrderVo;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72SupplyChannelHistoryMapper extends Mapper<Inno72SupplyChannelHistory> {

	List<WorkOrderVo> getWorkOrderVoList(Map<String, Object> map);

    List<Inno72SupplyChannelHistory> getWorkOrderGoods(Map<String, Object> map);
}