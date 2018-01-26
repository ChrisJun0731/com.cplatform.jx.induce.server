package com.cplatform.jx.induce.server.service;

import java.nio.channels.SocketChannel;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.GetDeviceStateInfoResponse;
import com.cplatform.jx.induce.GetDeviceWaringParamResponse;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.message.DetectionPointMessage;
import com.cplatform.jx.induce.server.protocol.message.DetectionPointRespMessage;
import com.cplatform.jx.induce.server.protocol.message.Header;
import com.cplatform.jx.induce.server.protocol.message.QueryScreenStateMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryScreenStateRespMessage;
import com.cplatform.jx.induce.server.protocol.net.SocketClient;
import com.cplatform.jx.induce.server.protocol.net.SocketConnectionPool;
import com.cplatform.spring.sync.SyncLock;

/**
 * 
 * 获取设备告警参数. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月6日 下午4:59:30
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Component
public class GetDeviceWaringService {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SocketClient socketClietnt;
	
	@Autowired
	private SocketConnectionPool socketConnPool;
	
	/** sysConfig */
	@Resource(name = "sysConfig")
	private PropertiesConfiguration sysConfig;

//	@SyncLock(key = "#request.ip")
	public GetDeviceWaringParamResponse process(BaseRequest request) throws Exception {
		//
		SocketChannel socket = null;
		try {
			GetDeviceWaringParamResponse response = null;
			socket = socketConnPool.getConnection(request.getIp(), sysConfig.getInt("device.port", 5000));//socketClietnt.connect(request.getIp(), sysConfig.getInt("device_port", 5000));
			if (socket != null) {
				QueryScreenStateMessage info = new QueryScreenStateMessage(new Header());

				info.setAddress(BaseRequest.CLIENT_ADDRESS);

				QueryScreenStateRespMessage obj = (QueryScreenStateRespMessage) socketClietnt.sendCommond(info, socket,CommandID.QUERY_SCREEN_STATE_RESP);
				if (obj != null) {
					response = new GetDeviceWaringParamResponse();
					response.setStatus(GetDeviceStateInfoResponse.STATUS_OK);
					int temp = obj.getValue();
					if (obj.getSymbol() == 2)
						temp = obj.getValue() * -1;
					response.setTemperature(temp);
					//获取 设备坏点信息
					DetectionPointRespMessage bad =  GetDetectionPoint(socket);
					 if(bad != null&&bad.getResult()==1)
						 response.setBadPoints(bad.getBadNum());
					 else
						 response.setBadPoints(0);
					
				} else {
					response = new GetDeviceWaringParamResponse();
					response.setStatus(-1);
					response.setStatusText("设备无响应");
				}
			} else {
				response = new GetDeviceWaringParamResponse();
				response.setStatus(-1);
				response.setStatusText("设备不在线");
			}

			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
			GetDeviceWaringParamResponse response = new GetDeviceWaringParamResponse();
			response.setStatus(-2);
			response.setStatusText("Exception");
			return response;
		}
		finally{
//			socketClietnt.stopSocket(socket);
			socketConnPool.returnConnection(request.getIp(), sysConfig.getInt("device.port", 5000), socket);
		}
	}
	
	private DetectionPointRespMessage GetDetectionPoint(SocketChannel socket){
		DetectionPointMessage info = new DetectionPointMessage(new Header());
		info.setAddress(BaseRequest.CLIENT_ADDRESS);
		DetectionPointRespMessage obj = (DetectionPointRespMessage) socketClietnt.sendCommond(info, socket,CommandID.DETECTION_POINT_RESP);
		return obj;
	}
	
	@PostConstruct
	private void init() throws Exception {
		socketConnPool.createPool();
	}
}
