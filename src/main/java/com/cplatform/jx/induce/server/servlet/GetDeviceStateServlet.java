package com.cplatform.jx.induce.server.servlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.GetDeviceStateInfoResponse;
import com.cplatform.jx.induce.server.service.GetDeviceStateService;

import net.sf.json.JSONObject;

/**
 * 
 * 实时获取设备状态. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月3日 下午4:48:10
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/getDeviceState")
public class GetDeviceStateServlet extends AbstractService<BaseRequest, GetDeviceStateInfoResponse> {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private GetDeviceStateService deviceStateService;

	@Override
	protected boolean needCheckSignature() {
		return false;
	}

	@Override
	protected boolean needCheckTransaction() {
		return false;
	}

	@Override
	public GetDeviceStateInfoResponse processRequest(BaseRequest request) {
		try {
			GetDeviceStateInfoResponse response = deviceStateService.process(request);			
			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
			GetDeviceStateInfoResponse response = new GetDeviceStateInfoResponse();
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
