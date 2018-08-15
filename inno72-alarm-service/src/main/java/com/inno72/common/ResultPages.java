package com.inno72.common;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class ResultPages {

	/**
	 * Title:改版后的分页返回处理 Description:
	 * 
	 * @Create_by:L.cm
	 * @Create_date:2017年7月18日
	 * @Last_Edit_By:
	 * @Edit_Description
	 * @Create_Version:ShareWithUs 1.0
	 */
	public static ModelAndView page(Result<?> result) {
		View view = getInstance();
		return new ModelAndView(view, result.map());
	}

	private static View getInstance() {
		return JsonViewHolder.INSTANCE;
	}

	private static class JsonViewHolder {
		private static View INSTANCE = new MappingJackson2JsonView();
	}

}
