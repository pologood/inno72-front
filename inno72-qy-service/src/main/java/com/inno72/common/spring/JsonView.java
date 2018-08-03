package com.inno72.common.spring;

import java.util.Iterator;
import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.inno72.common.Result;


public class JsonView extends MappingJackson2JsonView {
	public JsonView() {
	}

	@SuppressWarnings("rawtypes")
	protected Object filterModel(Map<String, Object> model) {
		Iterator var2 = model.entrySet().iterator();

		Map.Entry entry;
		do {
			if(!var2.hasNext()) {
				return super.filterModel(model);
			}

			entry = (Map.Entry)var2.next();
		} while(!entry.getValue().getClass().getName().equals(Result.class.getName()));

		return entry.getValue();
	}

}
