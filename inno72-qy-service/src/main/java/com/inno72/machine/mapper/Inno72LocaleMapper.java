package com.inno72.machine.mapper;


import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Locale;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Inno72LocaleMapper extends Mapper<Inno72Locale> {
    List<Inno72Locale> selectLocaleByAreaCode(String areaCode);
}