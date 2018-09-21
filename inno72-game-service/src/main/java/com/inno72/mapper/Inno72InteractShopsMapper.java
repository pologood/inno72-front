package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72InteractShops;
import org.apache.ibatis.annotations.Param;

public interface Inno72InteractShopsMapper extends Mapper<Inno72InteractShops> {

    Inno72InteractShops findByInteractIdAndShopId(@Param("interactId") String interactId, @Param("shopId") String shopId);
}