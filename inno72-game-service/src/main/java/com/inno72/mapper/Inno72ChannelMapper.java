package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Channel;
import org.apache.ibatis.annotations.Param;

public interface Inno72ChannelMapper extends Mapper<Inno72Channel> {
    Inno72Channel findChannelBySellerId(@Param("sellerId")String sellerId);

    Inno72Channel findByCode(@Param("channelCode") String channelCode);
}