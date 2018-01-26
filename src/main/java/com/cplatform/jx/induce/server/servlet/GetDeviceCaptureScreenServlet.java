package com.cplatform.jx.induce.server.servlet;

import java.nio.channels.SocketChannel;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.GetDeviceCaptureScreenResponse;
import com.cplatform.jx.induce.GetDeviceStateInfoResponse;
import com.cplatform.jx.induce.server.protocol.message.GetScreenImgMessage;
import com.cplatform.jx.induce.server.protocol.net.SocketClient;
import com.cplatform.jx.induce.server.protocol.net.SocketConnectionPool;

import net.sf.json.JSONObject;

/**
 * 
 * ��ȡ��Ļ��ǰ��ͼ. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��6�� ����5:17:49
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/getDeviceCapture")
public class GetDeviceCaptureScreenServlet extends AbstractService<BaseRequest, GetDeviceCaptureScreenResponse> {

	private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");
	/** ��־��¼�� */
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
	public GetDeviceCaptureScreenResponse processRequest(BaseRequest request) {
		GetDeviceCaptureScreenResponse response = null;
		SocketChannel socket = null;
		try {
			
			socket = socketConnPool.getConnection(request.getIp(), sysConfig.getInt("device.port", 5000));//socketClietnt.connect(request.getIp(), sysConfig.getInt("device_port", 5000));
			if (socket != null) {
				String path =GetFilePath();
				String nowTime = dateFormat.format(new Date()); // ��ȡ��ǰʱ��
				String fileName = nowTime+".bmp";
				int ret = socketClietnt.captureScreen(GetScreenImgMessage.MAX_BLOCK_SIZE, socket,path,fileName);				
				response = new GetDeviceCaptureScreenResponse();
				response.setStatus(-1);
				switch (ret) {
				case 0:
					response.setStatus(GetDeviceStateInfoResponse.STATUS_OK);
					response.setBmpPath(path+fileName);
					break;
				case -1:
					response.setStatusText("�豸����Ӧ");
					break;
				case -2:
					response.setStatusText("��ȡͼƬʧ��");
					break;
				default:
					response.setStatusText("fail");
					break;
				}
			} else {
				response = new GetDeviceCaptureScreenResponse();
				response.setStatus(-1);
				response.setStatusText("�豸������");			
			}
			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
			response = new GetDeviceCaptureScreenResponse();
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
	
	private String GetFilePath(){
		String nowTime = dateFormat.format(new Date()); // ��ȡ��ǰʱ��
		StringBuilder outputPath = new StringBuilder(100);
		String path = sysConfig.getString("capture.bmp.path","f://T");
		if (!path.endsWith("/")) {
			outputPath.append(path).append("/");
		} else {
			outputPath.append(path);
		}
		outputPath.append(nowTime.substring(0, 6)).append("/");
		outputPath.append(nowTime.substring(6, 8)).append("/");		
		
		return outputPath.toString();
	}
	
	@PostConstruct
	private void init() throws Exception {
		socketConnPool.createPool();
	}
}
