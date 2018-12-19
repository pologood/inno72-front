package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72SupplyChannel;
import com.inno72.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;

public interface Inno72SupplyChannelMapper extends Mapper<Inno72SupplyChannel> {

	int subCount(Inno72SupplyChannel supplyChannel);

	List<Inno72SupplyChannel> selectListByParam(Map<String, Object> map);

	Inno72SupplyChannel selectChannel(Inno72SupplyChannel inno72SupplyChannel);

	List<Inno72SupplyChannel> selectByGoodsId(Map<String, String> param);

    String findGoodsIdByChannelId(@Param(value="channelId") String channelId);

    List<GoodsVo> findGoodsInfoByMerchantIdAndMachineId(@Param(value="merchantId") String merchantId,@Param(value="machineId")  String machineId);
}