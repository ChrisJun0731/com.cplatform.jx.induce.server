package com.cplatform.jx.induce.server.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.PlayShow;
import com.cplatform.jx.induce.PlayTime;
import com.cplatform.jx.induce.PushContentRequest;
import com.cplatform.jx.induce.server.service.PushDeviceContentService;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * 节目发布. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月3日 下午4:48:35
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/pushPlayContent")
public class PushPlayListServlet  extends AbstractService<PushContentRequest, BaseResponse> {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	/** jsonConfig */
	private JsonConfig jsonConfig;
	
	@Autowired
	private PushDeviceContentService pushDeviceContentService;

	@Override
	protected boolean needCheckSignature() {
		return false;
	}

	@Override
	protected boolean needCheckTransaction() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() throws Exception {
		jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(PushContentRequest.class);
		@SuppressWarnings("rawtypes")
		Map classMap = new HashMap();
		classMap.put("show", PlayShow.class);
		classMap.put("playTime", PlayTime.class);
		jsonConfig.setClassMap(classMap);

	}
	
	@Override
	public BaseResponse processRequest(PushContentRequest request) {
		try {
			if(request == null){
				BaseResponse response = new BaseResponse();
				response.setStatus(-3);
				response.setStatusText("请求参数异常");
			}
			BaseResponse response = pushDeviceContentService.process(request,0,request.getIp());			
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
	protected PushContentRequest requestJsonToObject(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (PushContentRequest) JSONObject.toBean(jsonObject, jsonConfig);

	}
}
