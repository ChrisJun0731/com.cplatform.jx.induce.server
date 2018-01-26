package com.cplatform.jx.induce.server.servlet;

import java.nio.channels.SocketChannel;

import javax.annotation.PostConstruct;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.GetDeviceBaseParamResponse;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.message.Header;
import com.cplatform.jx.induce.server.protocol.message.QueryBaseParamMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryBaseParamRespMessage;
import com.cplatform.jx.induce.server.protocol.net.SocketClient;
import com.cplatform.jx.induce.server.protocol.net.SocketConnectionPool;

import net.sf.json.JSONObject;

/**
 * 
 * ��ȡ�豸��������. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��3�� ����4:54:54
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/getDeviceBaseParam")
public class GetDeviceBaseParamServlet extends AbstractService<BaseRequest, GetDeviceBaseParamResponse> {

	/** ��־��¼�� */
	private Logger logger = Logger.getLogger(getClass());

	@Override
	protected boolean needCheckSignature() {
		return false;
	}

	@Autowired
	private SocketClient socketClietnt;
	
	@Autowired
	private SocketConnectionPool socketConnPool;

	@Override
	protected boolean needCheckTransaction() {
		return false;
	}

	@Override
	public GetDeviceBaseParamResponse processRequest(BaseRequest request) {
		GetDeviceBaseParamResponse response = null;
		SocketChannel socket = null;
		try {
			
			socket = socketConnPool.getConnection(request.getIp(), sysConfig.getInt("device.port", 5000));//socketClietnt.connect(request.getIp(), sysConfig.getInt("device_port", 5000));
			if (socket != null) {
				QueryBaseParamMessage info = new QueryBaseParamMessage(new Header());

				info.setAddress(BaseRequest.CLIENT_ADDRESS);

				QueryBaseParamRespMessage obj = (QueryBaseParamRespMessage) socketClietnt.sendCommond(info, socket,CommandID.QUERY_BASE_PARAM_RESP);
				if (obj != null) {
					response = new GetDeviceBaseParamResponse();
					response.setStatus(GetDeviceBaseParamResponse.STATUS_OK);
					response.setCmsip(obj.getCmsip());
					response.setGateway(obj.getGateway());
					response.setIp(obj.getIp());
					response.setMask(obj.getMask());
					response.setPort(obj.getPort());
					response.setScreenNum(obj.getScreenNum());
				} else {
					response = new GetDeviceBaseParamResponse();
					response.setStatus(-1);
					response.setStatusText("�豸����Ӧ");
				}
			} else {
				response = new GetDeviceBaseParamResponse();
				response.setStatus(-1);
				response.setStatusText("�豸������");
			}
			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
		    response = new GetDeviceBaseParamResponse();
			response.setStatus(-2);
			response.setStatusText("Exception");
			return response;
		}
		finally{
//			socketClietnt.stopSocket(socket);
			socketConnPool.returnConnection(request.getIp(), sysConfig.getInt("device.port", 5000), socket);
		}
	}

	@Override
	protected BaseRequest requestJsonToObject(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (BaseRequest) JSONObject.toBean(jsonObject, BaseRequest.class);
	}
	
	@PostConstruct
	private void init() throws Exception {
		socketConnPool.createPool();
	}
}
