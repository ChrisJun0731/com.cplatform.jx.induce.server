package com.cplatform.jx.induce.server.servlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.GetDeviceWaringParamResponse;
import com.cplatform.jx.induce.server.service.GetDeviceWaringService;
import net.sf.json.JSONObject;

/**
 * 
 * 获取设备告警信息. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月6日 下午4:28:13
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/getDeviceWaring")
public class GetDeviceWaringServlet extends AbstractService<BaseRequest, GetDeviceWaringParamResponse> {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private GetDeviceWaringService deviceWarngService;

	@Override
	protected boolean needCheckSignature() {
		return false;
	}

	@Override
	protected boolean needCheckTransaction() {
		return false;
	}

	@Override
	public GetDeviceWaringParamResponse processRequest(BaseRequest request) {
		try {
			GetDeviceWaringParamResponse response = deviceWarngService.process(request);			
			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
			GetDeviceWaringParamResponse response = new GetDeviceWaringParamResponse();
			response.setStatus(-2);
			response.setStatusText("Exception");
			return response;
		}
	}

	@Override
	protected BaseRequest requestJsonToObject(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (BaseRequest) JSONObject.toBean(jsonObject, BaseRequest.class);
	}
	
}
