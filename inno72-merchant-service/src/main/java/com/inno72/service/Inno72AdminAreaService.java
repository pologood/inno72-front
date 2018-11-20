package com.inno72.service;

import java.util.List;
import java.util.Map;

import com.inno72.model.Inno72AdminArea;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/11/13.
 */
public interface Inno72AdminAreaService extends Service<Inno72AdminArea> {

	List<Inno72AdminArea> findCity();

}
