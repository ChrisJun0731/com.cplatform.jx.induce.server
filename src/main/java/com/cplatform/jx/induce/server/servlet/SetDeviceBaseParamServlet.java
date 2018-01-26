package com.cplatform.jx.induce.server.servlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.SetDeviceBaseParamRequest;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.service.SetDeviceParamService;

import net.sf.json.JSONObject;

/**
 * 
 * �����豸��������. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��3�� ����4:55:47
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setDeviceBaseParam")
public class SetDeviceBaseParamServlet  extends AbstractService<SetDeviceBaseParamRequest, BaseResponse> {

	/** ��־��¼�� */
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
	public BaseResponse processRequest(SetDeviceBaseParamRequest request) {
		try {
			if(request == null){
				BaseResponse response = new BaseResponse();
				response.setStatus(-3);
				response.setStatusText("��������쳣");
			}
			BaseResponse response = setDeviceParamService.process(request,CommandID.SET_BASE_PARAM);			
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
	protected SetDeviceBaseParamRequest requestJsonToObject(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (SetDeviceBaseParamRequest) JSONObject.toBean(jsonObject, SetDeviceBaseParamRequest.class);
	}
}
