package com.cplatform.jx.induce.server.service;

import java.nio.channels.SocketChannel;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.SetDeviceBaseParamRequest;
import com.cplatform.jx.induce.SetDeviceDateTimeRequest;
import com.cplatform.jx.induce.SetDevicePowerWaringRequest;
import com.cplatform.jx.induce.SetDeviceTempWaringRequest;
import com.cplatform.jx.induce.SetDeviceVirtualConnRequest;
import com.cplatform.jx.induce.SetDeviceEnvWaringRequest;
import com.cplatform.jx.induce.SetDeviceLightRequest;
import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.CleanFileRequest;
import com.cplatform.jx.induce.ControlDeviceScreenRequest;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.message.CleanFileMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlLightMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlSwitchMessage;
import com.cplatform.jx.induce.server.protocol.message.Header;
import com.cplatform.jx.induce.server.protocol.message.ResetMessage;
import com.cplatform.jx.induce.server.protocol.message.SetBaseParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetBaseRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetControlParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetDateTimeMessage;
import com.cplatform.jx.induce.server.protocol.message.SetHighTemperatureParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetPowerExceptionParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetRestFactorySettingMessage;
import com.cplatform.jx.induce.server.protocol.message.SetVirtualConnTestMessage;
import com.cplatform.jx.induce.server.protocol.net.SocketClient;
import com.cplatform.jx.induce.server.protocol.net.SocketConnectionPool;
import com.cplatform.spring.sync.SyncLock;

/**
 * 
 * 设置设备参数. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月7日 上午10:45:32
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Component
public class SetDeviceParamService {

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SocketClient socketClietnt;
	
	@Autowired
	private SocketConnectionPool socketConnPool;
	
	/** sysConfig */
	@Resource(name = "sysConfig")
	private PropertiesConfiguration sysConfig;

	@SyncLock(key = "#request.ip")
	public BaseResponse process(BaseRequest request,int commandId) throws Exception {
		//
		SocketChannel socket = null;
		try {
			BaseResponse response = null;
			SetBaseRespMessage obj = null;
			socket = socketConnPool.getConnection(request.getIp(), sysConfig.getInt("device.port", 5000));//socketClietnt.connect(request.getIp(), sysConfig.getInt("device_port", 5000));
			
			if (socket != null) {
				switch(commandId){
				case CommandID.SET_BASE_PARAM:
					SetDeviceBaseParamRequest req = (SetDeviceBaseParamRequest)request;
					SetBaseParamMessage info = new SetBaseParamMessage(new Header());
					
					info.setAddress(BaseRequest.CLIENT_ADDRESS);
					info.setCmsip(req.getCmsip());
					info.setGateway(req.getGateway());
					info.setIp(req.getCardip());
					info.setMask(req.getMask());
					info.setPort(req.getPort());
					info.setReport(req.getReport());
					info.setScreenNum(req.getScreenNum());
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(info, socket,CommandID.SET_BASE_PARAM_RESP);					
					break;
				case CommandID.CONTROL_SCREEN:
					ControlDeviceScreenRequest reqscreen = (ControlDeviceScreenRequest)request;
					ControlSwitchMessage switchinfo = new ControlSwitchMessage(new Header());
					switchinfo.setAddress(BaseRequest.CLIENT_ADDRESS);
					switchinfo.setSwitchs(reqscreen.getIsOpen());
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(switchinfo, socket,CommandID.CONTROL_SCREEN_RESP);
					break;
				case CommandID.SET_DATETIME:
					SetDeviceDateTimeRequest dateTime = (SetDeviceDateTimeRequest)request;
					SetDateTimeMessage reqdatetime = new SetDateTimeMessage(new Header());

					reqdatetime.setAddress(BaseRequest.CLIENT_ADDRESS);

					String date = dateTime.getDateTime();  
					
					reqdatetime.setDay(Integer.valueOf(date.substring(6, 8)));
					reqdatetime.setHours(Integer.valueOf(date.substring(8, 10)));
					reqdatetime.setMinutes(Integer.valueOf(date.substring(10, 12)));
					reqdatetime.setMonth(Integer.valueOf(date.substring(4, 6)));
					reqdatetime.setSeconds(Integer.valueOf(date.substring(12, 14)));
					reqdatetime.setYear(Integer.valueOf(date.substring(0, 4)));
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(reqdatetime, socket,CommandID.SET_DATETIME_RESP);
					break;
				case CommandID.SET_CONTROL_PARAM:
					SetDeviceEnvWaringRequest temperature = (SetDeviceEnvWaringRequest)request;
					SetControlParamMessage temp =new SetControlParamMessage(new Header());
					temp.setAddress(BaseRequest.CLIENT_ADDRESS);
					temp.setCloseScreen(temperature.getThreshold());
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(temp, socket,CommandID.SET_CONTROL_PARAM_RESP);
					break;
				case CommandID.SET_POWER_EXCEPTION_PARAM:
					SetDevicePowerWaringRequest power = (SetDevicePowerWaringRequest)request;
					SetPowerExceptionParamMessage powerexp =new SetPowerExceptionParamMessage(new Header());
					powerexp.setAddress(BaseRequest.CLIENT_ADDRESS);
					powerexp.setIsClose(power.getIsClose());
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(powerexp, socket,CommandID.SET_POWER_EXCEPTION_PARAM_RESP);
					break;
				case CommandID.SET_HIGH_TEMPERATURE_PARAM:
					SetDeviceTempWaringRequest temps =(SetDeviceTempWaringRequest)request;
					SetHighTemperatureParamMessage temper =new SetHighTemperatureParamMessage(new Header());
					temper.setAddress(BaseRequest.CLIENT_ADDRESS);
					temper.setFanLocation(temps.getFanLocation());
					temper.setFanLocationCms(temps.getFanLocationCms());
					temper.setIsClosePower(temps.getIsClosePower());
					temper.setIsOpen(temps.getIsOpen());
					temper.setLimit(temps.getLimit());
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(temper, socket,CommandID.SET_HIGH_TEMPERATURE_PARAM_RESP);		
					break;
				case CommandID.SET_VIRTUAL_CONN_TEST:
					SetDeviceVirtualConnRequest vir = (SetDeviceVirtualConnRequest)request;
					SetVirtualConnTestMessage virtest =new SetVirtualConnTestMessage(new Header());
					virtest.setAddress(BaseRequest.CLIENT_ADDRESS);
					virtest.setBroadcast(vir.getBroadcast());
					virtest.setIsOpen(vir.getIsOpen());
					virtest.setTime(vir.getTime());
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(virtest, socket,CommandID.SET_VIRTUAL_CONN_TEST_RESP);				
					break;
				case CommandID.CLEAR_FILE:
					CleanFileRequest clean =(CleanFileRequest)request;
					CleanFileMessage cleanfile = new CleanFileMessage(new Header());
					cleanfile.setAddress(BaseRequest.CLIENT_ADDRESS);
					cleanfile.setType(clean.getType());
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(cleanfile, socket,CommandID.CLEAR_FILE_RESP);							
					break;
				case CommandID.CONTROL_LIGHT:
					SetDeviceLightRequest light =(SetDeviceLightRequest)request;
					ControlLightMessage lightmessage =new ControlLightMessage(new Header());
					lightmessage.setAddress(BaseRequest.CLIENT_ADDRESS);
					lightmessage.setType(light.getType());
					lightmessage.setLevel(light.getLevel());
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(lightmessage, socket,CommandID.CONTROL_LIGHT_RESP);
					break;
				case CommandID.RESET_EQUIPMENT:
					ResetMessage rest =new ResetMessage(new Header());
					rest.setAddress(BaseRequest.CLIENT_ADDRESS);
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(rest, socket,CommandID.RESET_EQUIPMENT_RESP);							
					break;
				case CommandID.SET_REST_FACTORY:
					SetRestFactorySettingMessage fac =new SetRestFactorySettingMessage(new Header());
					fac.setAddress(BaseRequest.CLIENT_ADDRESS);
					obj = (SetBaseRespMessage) socketClietnt.sendCommond(fac, socket,CommandID.SET_REST_FACTORY_RESP);							
					break;
				}
				
				if (obj != null) {
					response = new BaseResponse();
					if(obj.getResult() ==1){
					   response.setStatus(BaseResponse.STATUS_OK);
					   response.setStatusText("设置成功");
					}
					else{
					   response.setStatus(-6);	
					   response.setStatusText("设置失败");
					}
					
				} else {
					response = new BaseResponse();
					response.setStatus(-1);
					response.setStatusText("设备无响应");
				}

			} else {
				response = new BaseResponse();
				response.setStatus(-1);
				response.setStatusText("设备不在线");
			}

			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
			BaseResponse response = new BaseResponse();
			response.setStatus(-2);
			response.setStatusText("Exception");
			return response;
		}
		finally{
//			socketClietnt.stopSocket(socket);
			socketConnPool.returnConnection(request.getIp(), sysConfig.getInt("device.port", 5000), socket);
		}
	}
	
	@PostConstruct
	private void init() throws Exception {
		socketConnPool.createPool();
	}
	
}
