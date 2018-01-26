package com.cplatform.jx.induce.server.servlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.SetDeviceDateTimeRequest;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.service.SetDeviceParamService;

import net.sf.json.JSONObject;

/**
 * 
 * 设置设备时间. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月7日 上午11:26:35
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setDeviceDateTime")
public class SetDeviceDateTimeServlet extends AbstractService<SetDeviceDateTimeRequest, BaseResponse> {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SetDeviceParamService setDeviceParamService;

	@Override
	protected boolean needCheckSignature() {
		return false;
	}

	@Override
	protected boolean needCheckTransaction() {
		return false;
	}

	@Override
	public BaseResponse processRequest(SetDeviceDateTimeRequest request) {
		try {
			if(request == null){
				BaseResponse response = new BaseResponse();
				response.setStatus(-3);
				response.setStatusText("请求参数异常");
			}
			BaseResponse response = setDeviceParamService.process(request,CommandID.SET_DATETIME);			
			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
			BaseResponse response = new BaseResponse();
			response.setStatus(-2);
			response.setStatusText("Exception");
			return response;
		}
	}

	@Override
	protected SetDeviceDateTimeRequest requestJsonToObject(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (SetDeviceDateTimeRequest) JSONObject.toBean(jsonObject, SetDeviceDateTimeRequest.class);
	}
}
