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
 * 获取设备基本参数. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月3日 下午4:54:54
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/getDeviceBaseParam")
public class GetDeviceBaseParamServlet extends AbstractService<BaseRequest, GetDeviceBaseParamResponse> {

	/** 日志记录器 */
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
					response.setStatusText("设备无响应");
				}
			} else {
				response = new GetDeviceBaseParamResponse();
				response.setStatus(-1);
				response.setStatusText("设备不在线");
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
