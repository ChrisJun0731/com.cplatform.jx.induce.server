package com.cplatform.jx.induce.server.servlet;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.PushRealTimeContentRequest;
import com.cplatform.jx.induce.server.service.PushDeviceContentService;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
@RequestMapping("/pushRealTimeContent")
public class PushRealTimePlayListServlet extends AbstractService<PushRealTimeContentRequest, BaseResponse> {

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

	@PostConstruct
	private void init() throws Exception {
		jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(PushRealTimeContentRequest.class);
//		@SuppressWarnings("rawtypes")
//		Map classMap = new HashMap();
//		classMap.put("show", PlayShow.class);
//		classMap.put("playTime", PlayTime.class);
//		jsonConfig.setClassMap(classMap);

	}
	
	@Override
	public BaseResponse processRequest(PushRealTimeContentRequest request) {
		try {
			if(request == null){
				BaseResponse response = new BaseResponse();
				response.setStatus(-3);
				response.setStatusText("请求参数异常");
			}
			BaseResponse response = pushDeviceContentService.process(request,1,request.getIp());			
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
	protected PushRealTimeContentRequest requestJsonToObject(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (PushRealTimeContentRequest) JSONObject.toBean(jsonObject, jsonConfig);

	}
}
