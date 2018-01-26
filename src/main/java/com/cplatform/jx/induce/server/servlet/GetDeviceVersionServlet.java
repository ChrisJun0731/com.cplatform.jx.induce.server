package com.cplatform.jx.induce.server.servlet;

import java.nio.channels.SocketChannel;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.GetDeviceStateInfoResponse;
import com.cplatform.jx.induce.GetDeviceVersionResponse;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.message.Header;
import com.cplatform.jx.induce.server.protocol.message.QueryVersionMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryVersionRespMessage;
import com.cplatform.jx.induce.server.protocol.net.SocketClient;
import com.cplatform.jx.induce.server.protocol.net.SocketConnectionPool;

import net.sf.json.JSONObject;

/**
 * 
 * 获取设备版本信息. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月3日 下午4:53:00
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/getDeviceVersion")
public class GetDeviceVersionServlet extends AbstractService<BaseRequest, GetDeviceVersionResponse> {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SocketClient socketClietnt;
	
	@Autowired
	private SocketConnectionPool socketConnPool;

	@Override
	protected boolean needCheckSignature() {
		return false;
	}

	@Override
	protected boolean needCheckTransaction() {
		return false;
	}

	@Override
	public GetDeviceVersionResponse processRequest(BaseRequest request) {
		GetDeviceVersionResponse response = null;
		SocketChannel socket = null;
		
		try {
			socket = socketConnPool.getConnection(request.getIp(), sysConfig.getInt("device.port", 5000));//socketClietnt.connect(request.getIp(), sysConfig.getInt("device_port", 5000));
			if (socket != null) {
				QueryVersionMessage info = new QueryVersionMessage(new Header());

				info.setAddress(BaseRequest.CLIENT_ADDRESS);

				QueryVersionRespMessage obj = (QueryVersionRespMessage) socketClietnt.sendCommond(info, socket,CommandID.QUERY_VERSION_RESP);
				if (obj != null) {
					response = new GetDeviceVersionResponse();
					response.setStatus(GetDeviceStateInfoResponse.STATUS_OK);
					response.setVersion(obj.getVersion());
				} else {
					response = new GetDeviceVersionResponse();
					response.setStatus(-1);
					response.setStatusText("设备无响应");
				}
			} else {
				response = new GetDeviceVersionResponse();
				response.setStatus(-1);
				response.setStatusText("设备不在线");
			}
			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
			response = new GetDeviceVersionResponse();
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
