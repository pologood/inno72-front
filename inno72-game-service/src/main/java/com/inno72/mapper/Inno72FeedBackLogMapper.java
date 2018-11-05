package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72FeedBackLog;
import org.apache.ibatis.annotations.Param;

public interface Inno72FeedBackLogMapper extends Mapper<Inno72FeedBackLog> {
    void deleteFeedBackErrorLogByOrderId(@Param("orderId") String tradeNo);
}