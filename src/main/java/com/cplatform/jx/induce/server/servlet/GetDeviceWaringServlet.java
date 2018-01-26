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
 * ��ȡ�豸�澯��Ϣ. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��6�� ����4:28:13
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/getDeviceWaring")
public class GetDeviceWaringServlet extends AbstractService<BaseRequest, GetDeviceWaringParamResponse> {

	/** ��־��¼�� */
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
