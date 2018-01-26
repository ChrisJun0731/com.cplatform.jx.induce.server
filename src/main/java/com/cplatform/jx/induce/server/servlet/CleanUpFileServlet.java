package com.cplatform.jx.induce.server.servlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.CleanFileRequest;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.service.SetDeviceParamService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/cleanfile")
public class CleanUpFileServlet extends AbstractService<CleanFileRequest, BaseResponse> {

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
	public BaseResponse processRequest(CleanFileRequest request) {
		try {
			if(request == null){
				BaseResponse response = new BaseResponse();
				response.setStatus(-3);
				response.setStatusText("请求参数异常");
			}
			BaseResponse response = setDeviceParamService.process(request,CommandID.CLEAR_FILE);			
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
	protected CleanFileRequest requestJsonToObject(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (CleanFileRequest) JSONObject.toBean(jsonObject, CleanFileRequest.class);
	}

}
