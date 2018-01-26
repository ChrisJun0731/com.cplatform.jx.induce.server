package com.cplatform.jx.induce.server.protocol.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.constants.ErrorCode;
import com.cplatform.jx.induce.server.protocol.message.AbstractMessage;
import com.cplatform.jx.induce.server.protocol.message.ActiveTestMessage;
import com.cplatform.jx.induce.server.protocol.message.ActiveTestRespMessage;
import com.cplatform.jx.induce.server.protocol.message.CleanFileMessage;
import com.cplatform.jx.induce.server.protocol.message.ClearFileRespMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlCardPowerSwitchMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlCardPowerSwitchRespMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlLightMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlLightRespMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlLocalPowerSwitchMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlLocalPowerSwitchRespMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlScreenPowerMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlScreenPowerRespMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlSwitchMessage;
import com.cplatform.jx.induce.server.protocol.message.ControlSwitchRespMessage;
import com.cplatform.jx.induce.server.protocol.message.DetectionPointMessage;
import com.cplatform.jx.induce.server.protocol.message.DetectionPointRespMessage;
import com.cplatform.jx.induce.server.protocol.message.GetScreenImgMessage;
import com.cplatform.jx.induce.server.protocol.message.GetScreenImgRespMessage;
import com.cplatform.jx.induce.server.protocol.message.GetScreenLatticeMessage;
import com.cplatform.jx.induce.server.protocol.message.GetScreenLatticeRespMessage;
import com.cplatform.jx.induce.server.protocol.message.Header;
import com.cplatform.jx.induce.server.protocol.message.QueryBaseParamMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryBaseParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryControlParamMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryControlParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryDetectionPointMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryDetectionPointRespMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryDisplayContentMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryDisplayContentRespMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryLightParamMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryLightParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryScreenStateMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryScreenStateRespMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryVersionMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryVersionRespMessage;
import com.cplatform.jx.induce.server.protocol.message.ReadBadPointMessage;
import com.cplatform.jx.induce.server.protocol.message.ReadBadPointRespMessage;
import com.cplatform.jx.induce.server.protocol.message.ReadBadPointTwoMessage;
import com.cplatform.jx.induce.server.protocol.message.ReadBadPointTwoRespMessage;
import com.cplatform.jx.induce.server.protocol.message.ResetMessage;
import com.cplatform.jx.induce.server.protocol.message.ResetRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SendFileCompleteRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SendFileContentMessage;
import com.cplatform.jx.induce.server.protocol.message.SendFileContentRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SendFileNameMessage;
import com.cplatform.jx.induce.server.protocol.message.SendFileNameRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetAuthSecurityParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetAuthSecurityParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetBaseParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetBaseParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetBaseRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetCmsDisplayTimeMessage;
import com.cplatform.jx.induce.server.protocol.message.SetCmsDisplayTimeRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetCmsLightTimeMessage;
import com.cplatform.jx.induce.server.protocol.message.SetCmsLightTimeRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetControlCardNameMessage;
import com.cplatform.jx.induce.server.protocol.message.SetControlCardNameRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetControlLightMessage;
import com.cplatform.jx.induce.server.protocol.message.SetControlLightRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetControlParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetControlParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetDateTimeMessage;
import com.cplatform.jx.induce.server.protocol.message.SetDateTimeRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetDetectionPointMessage;
import com.cplatform.jx.induce.server.protocol.message.SetDetectionPointRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetDisplayListMessage;
import com.cplatform.jx.induce.server.protocol.message.SetDisplayListRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetHighTemperatureParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetHighTemperatureParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetInSourceParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetInSourceParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetPowerExceptionParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetPowerExceptionParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetRestFactorySettingMessage;
import com.cplatform.jx.induce.server.protocol.message.SetRestFactorySettingRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetScreenTimeSwitchParamMessage;
import com.cplatform.jx.induce.server.protocol.message.SetScreenTimeSwitchParamRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetVirtualConnTestMessage;
import com.cplatform.jx.induce.server.protocol.message.SetVirtualConnTestRespMessage;
import com.cplatform.jx.induce.server.protocol.message.UpdateLocalScreenContentMessage;
import com.cplatform.jx.induce.server.protocol.message.UpdateLocalScreenContentRespMessage;
import com.cplatform.jx.induce.server.protocol.message.VersionUpdateControlCardMessage;
import com.cplatform.jx.induce.server.protocol.message.VersionUpdateControlCardRespMessage;
import com.cplatform.jx.induce.server.protocol.message.info.LightTimeInfo;
import com.cplatform.jx.induce.server.protocol.message.info.PTimeInfo;
import com.cplatform.jx.induce.server.protocol.message.info.PlayTimeInfo;
import com.cplatform.jx.induce.server.protocol.message.info.SwitchTimeInfo;
import com.cplatform.jx.induce.server.protocol.tools.DES;
import com.cplatform.jx.induce.server.protocol.tools.DTool;
import com.cplatform.jx.induce.server.protocol.tools.MD5;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;
/**
 * 连接金晓控制卡客户端. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年11月2日 下午1:43:10
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */

@Component
public class SocketClient implements CommandID, ErrorCode{
	
	/** 普通日志. */
	private Logger log = Logger.getLogger(SocketClient.class);
	
	/** Socket链路是否正常. */
	public boolean socketIsAlive;
	

	/** 字符串缓冲区，主要用于日志字符组合. */
	private StringBuffer buffer;
	
	/**构造函数*/
	public SocketClient() {	
		buffer = new StringBuffer();
		socketIsAlive = true;
		
	}
	
	/**跟服务端建立连接*/
	public SocketChannel  connect(String host,int port) {
		int ret =0;
		InetSocketAddress isa = null;
		SocketChannel socket = null;
		try{
			isa = new InetSocketAddress(host, port);
		}
		catch (Exception e) {
			isa =null;
			log.error("和控制卡服务建立链路失败，IP:"+host+":"+port, e);
			ret = -1;
		}
		if(ret ==0){
		try {			
//			log.debug("连接中....");
			socket = SocketChannel.open(isa); // 打开套节字
			log.debug("IP:"+host+":"+port+"获取连接成功");
			
			// socket.socket().setTcpNoDelay(true);
			socket.configureBlocking(false); // 设置socket为非阻塞模式
			socketIsAlive = true;
			ret =1;
		}
		catch (Exception e) {
			stopSocket(socket);
			log.error("和控制卡服务建立链路失败，IP:"+host+":"+port, e);
			ret =-2;
		}

		}
		return socket;
	}

	
	/**
	 * 向服务端发送指令消息
	 * 
	 * @param info
	 *            MtInfo
	 */
	public AbstractMessage sendCommond(AbstractMessage info,SocketChannel socket,int respCommandId) {
		if (info == null) {
			return null;

		}
		Header head = info.getHeader();
		int commond = head.getCommand();
		String client ="";
		try {
			SocketAddress ip = socket.getRemoteAddress();
			client = ip.toString();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
//			e1.printStackTrace();
		}
		
		log.info("Client:"+client+"下发的命令:"+commond);
		ByteBuffer bytes = null;
		String sendcontent =info.toString();
		switch(commond){
			case QUERY_SCREEN_STATE:
				//组织请求包
				QueryScreenStateMessage sm = (QueryScreenStateMessage)info;				
				bytes = sm.byteBufferedMessage(sm.getHeader().getTotalLength());
				sendcontent = sm.toString();
				break;
			case QUERY_BASE_PARAM:
				QueryBaseParamMessage bp = (QueryBaseParamMessage)info;				
				bytes = bp.byteBufferedMessage(bp.getHeader().getTotalLength());	
				sendcontent = bp.toString();
				break;
			case QUERY_CONTROL_PARAM:
				QueryControlParamMessage cp = (QueryControlParamMessage)info;				
				bytes = cp.byteBufferedMessage(cp.getHeader().getTotalLength());	
				sendcontent = cp.toString();
				break;
			case QUERY_DISPLAY_CONTENT:
				QueryDisplayContentMessage dc = (QueryDisplayContentMessage)info;				
				bytes = dc.byteBufferedMessage(dc.getHeader().getTotalLength());	
				sendcontent = dc.toString();
				break;
			case QUERY_LIGHT_PARAM:
				QueryLightParamMessage lp = (QueryLightParamMessage)info;				
				bytes = lp.byteBufferedMessage(lp.getHeader().getTotalLength());	
				sendcontent = lp.toString();
				break;
			case QUERY_VERSION:
				QueryVersionMessage vm = (QueryVersionMessage)info;				
				bytes = vm.byteBufferedMessage(vm.getHeader().getTotalLength());	
				sendcontent = vm.toString();
				break;
			case GET_SCREEN_LATTICE:
				GetScreenLatticeMessage sl = (GetScreenLatticeMessage)info;				
				bytes = sl.byteBufferedMessage(sl.getHeader().getTotalLength());	
				sendcontent = sl.toString();
				break;
			case DETECTION_POINT:
				DetectionPointMessage dp = (DetectionPointMessage)info;
				bytes = dp.byteBufferedMessage(dp.getHeader().getTotalLength());	
				sendcontent = dp.toString();
				break;
			case TEST_SCREEN:
				break;
			case CONTROL_SCREEN:
				ControlSwitchMessage cs =(ControlSwitchMessage)info;
				bytes = cs.byteBufferedMessage(cs.getHeader().getTotalLength());	
				sendcontent = cs.toString();
				break;
			case CONTROL_LIGHT:
				ControlLightMessage cl =(ControlLightMessage)info;
				bytes = cl.byteBufferedMessage(cl.getHeader().getTotalLength());	
				sendcontent = cl.toString();
				break;
			case SET_LIGHT_CONTROL_PARAM:
				SetControlLightMessage scl =(SetControlLightMessage)info;
				bytes = scl.byteBufferedMessage(scl.getHeader().getTotalLength());	
				sendcontent = scl.toString();
				break;
			case SET_DATETIME:
				SetDateTimeMessage sdt =(SetDateTimeMessage)info;
				bytes = sdt.byteBufferedMessage(sdt.getHeader().getTotalLength());	
				sendcontent = sdt.toString();
				break;
			case RESET_EQUIPMENT:
				ResetMessage rest =(ResetMessage)info;
				bytes = rest.byteBufferedMessage(rest.getHeader().getTotalLength());	
				sendcontent = rest.toString();
				break;
			case SET_REST_FACTORY:
				SetRestFactorySettingMessage restIp = (SetRestFactorySettingMessage)info;
				bytes = restIp.byteBufferedMessage(restIp.getHeader().getTotalLength());	
				sendcontent = restIp.toString();
				break;
			case SET_BASE_PARAM:
				SetBaseParamMessage setbp = (SetBaseParamMessage)info;
				bytes = setbp.byteBufferedMessage(setbp.getHeader().getTotalLength());	
				sendcontent = setbp.toString();
				break;
			case SET_AUTH_SECURITY_PARAM:
				SetAuthSecurityParamMessage setAuth = (SetAuthSecurityParamMessage)info;
				bytes = setAuth.byteBufferedMessage(setAuth.getHeader().getTotalLength());	
				sendcontent = setAuth.toString();
				break;
			case SET_CONTROL_CARD_NAME:
				SetControlCardNameMessage setName = (SetControlCardNameMessage)info;
				bytes = setName.byteBufferedMessage(setName.getHeader().getTotalLength());
				sendcontent = setName.toString();
				break;
			case CONTROL_SCREEN_POWER:
				ControlScreenPowerMessage contSPower = (ControlScreenPowerMessage)info;
				bytes = contSPower.byteBufferedMessage(contSPower.getHeader().getTotalLength());	
				sendcontent = contSPower.toString();
				break;
			case CONTROL_LOCAL_POWER_SWITCH:
				ControlLocalPowerSwitchMessage contlPower = (ControlLocalPowerSwitchMessage)info;
				bytes = contlPower.byteBufferedMessage(contlPower.getHeader().getTotalLength());	
				sendcontent = contlPower.toString();
				break;
			case CONTROL_CARD_POWER_SWITCH:
				ControlCardPowerSwitchMessage contcPower = (ControlCardPowerSwitchMessage)info;
				bytes = contcPower.byteBufferedMessage(contcPower.getHeader().getTotalLength());	
				sendcontent = contcPower.toString();
				break;
			case SET_CONTROL_PARAM:
				SetControlParamMessage setContp = (SetControlParamMessage)info;
				bytes = setContp.byteBufferedMessage(setContp.getHeader().getTotalLength());	
				sendcontent = setContp.toString();
				break;
			case SET_HIGH_TEMPERATURE_PARAM:
				SetHighTemperatureParamMessage setHightTemp = (SetHighTemperatureParamMessage)info;
				bytes = setHightTemp.byteBufferedMessage(setHightTemp.getHeader().getTotalLength());	
				sendcontent = setHightTemp.toString();
				break;
			case SET_VIRTUAL_CONN_TEST:
				SetVirtualConnTestMessage setVirtual = (SetVirtualConnTestMessage)info;
				bytes = setVirtual.byteBufferedMessage(setVirtual.getHeader().getTotalLength());	
				sendcontent = setVirtual.toString();
				break;
			case SET_POWER_EXCEPTION_PARAM:
				SetPowerExceptionParamMessage setPowerExcepton = (SetPowerExceptionParamMessage)info;
				bytes = setPowerExcepton.byteBufferedMessage(setPowerExcepton.getHeader().getTotalLength());	
				sendcontent = setPowerExcepton.toString();
				break;
			case SET_DETECTION_POINT_PARAM:
				SetDetectionPointMessage setDetectionPoint = (SetDetectionPointMessage)info;
				bytes = setDetectionPoint.byteBufferedMessage(setDetectionPoint.getHeader().getTotalLength());	
				sendcontent = setDetectionPoint.toString();
				break;
			case QUERY_DETECTION_POINT_PARAM:
				QueryDetectionPointMessage queryDetectionPoint = (QueryDetectionPointMessage)info;
				bytes = queryDetectionPoint.byteBufferedMessage(queryDetectionPoint.getHeader().getTotalLength());	
				sendcontent = queryDetectionPoint.toString();
				break;
			case SET_IN_SOURCE_PARAM:
				SetInSourceParamMessage setInSourceParam = (SetInSourceParamMessage)info;
				bytes = setInSourceParam.byteBufferedMessage(setInSourceParam.getHeader().getTotalLength());	
				sendcontent = setInSourceParam.toString();
				break;
			case UPGRADE_CONTROL_CARD:
				VersionUpdateControlCardMessage versionUpdate = (VersionUpdateControlCardMessage)info;
				bytes = versionUpdate.byteBufferedMessage(versionUpdate.getHeader().getTotalLength());	
				sendcontent = versionUpdate.toString();
				break;
			case SET_SCREEN_TIME_SWITCH_PARAM:
				SetScreenTimeSwitchParamMessage setScreenTime = (SetScreenTimeSwitchParamMessage)info;
				bytes = setScreenTime.byteBufferedMessage(setScreenTime.getHeader().getTotalLength());	
				sendcontent = setScreenTime.toString();
				break;
			case SET_CMS_DISPLAY_TIME:
				SetCmsDisplayTimeMessage setDisplayTime = (SetCmsDisplayTimeMessage)info;
				bytes = setDisplayTime.byteBufferedMessage(setDisplayTime.getHeader().getTotalLength());	
				sendcontent = setDisplayTime.toString();
				break;
			case SET_CMS_LIGHT_TIME:
				SetCmsLightTimeMessage setLightTime = (SetCmsLightTimeMessage)info;
				bytes = setLightTime.byteBufferedMessage(setLightTime.getHeader().getTotalLength());	
				sendcontent = setLightTime.toString();
				break;
			case CLEAR_FILE:
				CleanFileMessage clearFile = (CleanFileMessage)info;
				bytes = clearFile.byteBufferedMessage(clearFile.getHeader().getTotalLength());	
				sendcontent = clearFile.toString();
				break;
			case SET_DISPLAY_LIST:
				SetDisplayListMessage setDisplayList = (SetDisplayListMessage)info;
				bytes = setDisplayList.byteBufferedMessage(setDisplayList.getHeader().getTotalLength());
				sendcontent = setDisplayList.toString();
				break;
			case ACTIVE_TEST:
				ActiveTestMessage activeTest = (ActiveTestMessage)info;
				bytes = activeTest.byteBufferedMessage(activeTest.getHeader().getTotalLength());
				sendcontent = activeTest.toString();
				break;				
			case GET_SCREEN_IMG:
				GetScreenImgMessage getBMP = (GetScreenImgMessage)info;
				bytes = getBMP.byteBufferedMessage(getBMP.getHeader().getTotalLength());
				sendcontent = getBMP.toString();
				break;	
			case SEND_FILE_NAME:
				SendFileNameMessage sendFileName = (SendFileNameMessage) info;
				bytes = sendFileName.byteBufferedMessage(sendFileName.getHeader().getTotalLength());
				sendcontent = sendFileName.toString();
				break;
			case SEND_FILE_CONTENT:
				SendFileContentMessage sendFileConent = (SendFileContentMessage) info;
				bytes = sendFileConent.byteBufferedMessage(sendFileConent.getHeader().getTotalLength());
				sendcontent = sendFileConent.toString();
				break;
			case UPDATE_LOCAL_SCREEN:
				UpdateLocalScreenContentMessage updatelocalcontent = (UpdateLocalScreenContentMessage) info;
				bytes = updatelocalcontent.byteBufferedMessage(updatelocalcontent.getHeader().getTotalLength());
				sendcontent = updatelocalcontent.toString();
				break;
			default:
				log.error("消息命令不合法"+commond);
				break;
		}
		try{
		//发送数据
		if (bytes == null) {
			buffer.setLength(0);
			buffer.append("转换为字节流时出错，无法发送此命令消息：");
			buffer.append(commond);
			buffer.append(" 设备地址为：");
			buffer.append(client);
			buffer.append(info.getHeader().getAddress());
			log.error(buffer.toString());
			return null;
		}
		
		int num = socket.write(bytes);	
		log.info("设备："+client+"命令参数:"+sendcontent);
		System.out.println(num);

		//读数据
	    byte[] rec = receiveData(socket);
	    
        log.info("设备："+client+"响应长度:"+rec.length);
        System.out.println("响应长度:"+rec.length);
	    //处理数据
	    List<byte[]> lis =DealBytes(rec);
	     if(lis.size()>0)
		     return DealMessage(lis,respCommandId, client);
	     else
	    	 return null;
	
	}
	catch (IOException e) {
		stopSocket(socket);
		log.error("", e);
		buffer.setLength(0);
		buffer.append("CommondId:");
		buffer.append(commond);
		buffer.append("\t");
		buffer.append(" 设备地址为：");
		buffer.append(info.getHeader().getAddress());
		log.error("发送命令出现异常:" + buffer.toString(), e);
	}
		 return null;	
	}
	
	/**
	 * 接收 响应字节
	 * @param socketChannel
	 * @return
	 * @throws IOException
	 */
	 private byte[] receiveData(SocketChannel socketChannel) throws IOException {  
		 byte[] bytes = null;  
         ByteArrayOutputStream baos = new ByteArrayOutputStream();            
         try {  
             ByteBuffer buffer = ByteBuffer.allocateDirect(1024);   
             int count = 0;  
             int i=0;
             int num =0;
             while(true){
             if ((count = socketChannel.read(buffer)) >= 0) {  
//            	 System.out.println("count:"+count);
                 buffer.flip();  
                 bytes = new byte[count];  
                 buffer.get(bytes);  
                 baos.write(bytes);  
                 buffer.clear();  
             }   
             bytes = null;
             bytes = baos.toByteArray();               
             
             i++;
             if(i>300){
            	 if(bytes.length ==0&&num<3){
                	 //继续等待读取 200
            		 i= 100;
            		 num++;
                 }
            	 else
            	   break;
             }
            
             try {
	            Thread.sleep(5);
            }
            catch (InterruptedException e) {
	         
	            e.printStackTrace();
            }
           }
             
         } finally {  
             try {  
            	//关闭 连接
//                 socketChannel.socket().shutdownInput();  
                 
                 baos.close();  
             } catch(Exception ex) {}  
         }  
         return bytes;  
     }  
   
	 /**
	  * 数据 清洗处理
	  * @param bytes
	  */
	private List<byte[]> DealBytes(byte[] bytes) {
		// 处理数据
		int isbegin = 0;
		int isend = 0;
		byte[] tempbytes = null;
		int tempcount = 0;
         List<byte[]> lis = new ArrayList<byte[]>();
		for (int i = 0; i < bytes.length; i++) {
			// 开始
			if (bytes[i] == (byte) MESSAGE_BEGIN && isbegin == 0) {
				// 数据域开始
				isbegin = i + 1;
			}
			// 结束
			else if (bytes[i] == (byte) MESSAGE_END) {
				// 数据域 结尾
				isend = i;
			}
			// 数据域
			if (isbegin > 0 && isend > 0) {

				tempcount = isend - isbegin;
				tempbytes = new byte[tempcount];
				if (tempcount > 0) {
					System.arraycopy(bytes, isbegin, tempbytes, 0, isend - isbegin);

					//des 解密
					
					// 反转义
					byte[] unchar = ToolKits.uncovertChar(tempbytes);

					//待处理消息体
					lis.add(unchar);
					// 处理实际包体
//					DealMessage(unchar);
				}
				isbegin = 0;
				isend = 0;
			} else {
				// 无效数据	
			}
		}

		return lis;
	}
	 
	 private AbstractMessage DealMessage(List<byte[]> lists,int commandId,String client){
		 int length =0;
		 Header header = new Header();
		 byte[] bytes = null;
		 for(byte[] temp :lists){
		  length = temp.length;
		 //解析头
		 byte[] headbytes =new byte[3];
		 System.arraycopy(temp, 0, headbytes, 0, 3);
		 header.byteToMessage(headbytes);
		 if(header.getCommand() == commandId)
			 bytes = temp;
		 }
		 if(bytes == null) return null;
		 String respcontent ="";
		 //数据域 加解密？		 
		 byte[] contentbytes = new byte[length -3];
		 System.arraycopy(bytes, 3, contentbytes, 0, length -3);
		 
//		 byte[] byt =null;
//        try {
//	        byt = DES.decrypt(contentbytes,"novamima");
//        }
//        catch (Exception e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//        }

			 
		 ByteBuffer buffer = ByteBuffer.allocate(length-3);
		 buffer.put(contentbytes);
		 buffer.flip();
		 
		 System.out.print("CommandId"+header.getCommand());
		 AbstractMessage obj =null;
		 switch(header.getCommand()){
			 case ACTIVE_TEST:
				 ActiveTestRespMessage activeResp = new ActiveTestRespMessage(header);
				 activeResp.byteToMessage(buffer);
				 obj =activeResp;
				 respcontent = activeResp.toString();
				 break;
			 case QUERY_SCREEN_STATE_RESP:
				 QueryScreenStateRespMessage queryScreenStateResp = new QueryScreenStateRespMessage(header);
				 queryScreenStateResp.byteToMessage(buffer);
				 obj =queryScreenStateResp;
				 respcontent = queryScreenStateResp.toString();
				 return queryScreenStateResp;
			 case QUERY_BASE_PARAM_RESP:
				 QueryBaseParamRespMessage queryBaseParamResp = new QueryBaseParamRespMessage(header);
				 queryBaseParamResp.byteToMessage(buffer);
				 obj =queryBaseParamResp;
				 respcontent = queryBaseParamResp.toString();
				 return queryBaseParamResp;
			 case QUERY_CONTROL_PARAM_RESP:
				 QueryControlParamRespMessage queryControlParamResp = new QueryControlParamRespMessage(header);
				 queryControlParamResp.byteToMessage(buffer);
				 obj =queryControlParamResp;
				 respcontent = queryControlParamResp.toString();
				 break;
			 case QUERY_DISPLAY_CONTENT_RESP:
				 QueryDisplayContentRespMessage queryDisContentResp = new QueryDisplayContentRespMessage(header);
				 queryDisContentResp.byteToMessage(buffer);
				 obj =queryDisContentResp;
				 respcontent = queryDisContentResp.toString();
				 break;
			 case QUERY_LIGHT_PARAM_RESP:
				 QueryLightParamRespMessage queryLightParamResp = new QueryLightParamRespMessage(header);
				 queryLightParamResp.byteToMessage(buffer);
				 obj =queryLightParamResp;
				 respcontent = queryLightParamResp.toString();
				 break;
			 case QUERY_VERSION_RESP:
				 QueryVersionRespMessage queryVerResp = new QueryVersionRespMessage(header);
				 queryVerResp.byteToMessage(buffer);
				 obj =queryVerResp;
				 respcontent = queryVerResp.toString();
				 break;
			 case GET_SCREEN_LATTICE_RESP:
				 GetScreenLatticeRespMessage getScreeLaResp = new GetScreenLatticeRespMessage(header);
				 getScreeLaResp.byteToMessage(buffer);
				 obj =getScreeLaResp;
				 respcontent = getScreeLaResp.toString();
				 break;
			 case DETECTION_POINT_RESP:
				 DetectionPointRespMessage detecPoinResp =new DetectionPointRespMessage(header);
				 detecPoinResp.byteToMessage(buffer);
				 obj =detecPoinResp;
				 respcontent = detecPoinResp.toString();
				 break;
			 case CONTROL_SCREEN_RESP:
//				 ControlSwitchRespMessage conSwitchResp =new ControlSwitchRespMessage(header);
				 SetBaseRespMessage conSwitchResp =new SetBaseRespMessage(header);
				 conSwitchResp.byteToMessage(buffer);
				 obj =conSwitchResp;
				 respcontent = conSwitchResp.toString();
				 break;
			 case CONTROL_LIGHT_RESP:
//				 ControlLightRespMessage conLightResp =new ControlLightRespMessage(header);
				 SetBaseRespMessage conLightResp =new SetBaseRespMessage(header);
				 conLightResp.byteToMessage(buffer);
				 obj =conLightResp;
				 respcontent = conLightResp.toString();
				 break;
			 case SET_LIGHT_CONTROL_PARAM_RESP:
//				 SetControlLightRespMessage setLightResp =new SetControlLightRespMessage(header);
				 SetBaseRespMessage setLightResp =new SetBaseRespMessage(header);
				 setLightResp.byteToMessage(buffer);
				 obj =setLightResp;
				 respcontent = setLightResp.toString();
				 break;
			 case SET_DATETIME_RESP:
//				 SetDateTimeRespMessage setDateTimeResp =new SetDateTimeRespMessage(header);
				 SetBaseRespMessage setDateTimeResp =new SetBaseRespMessage(header);
				 setDateTimeResp.byteToMessage(buffer);
				 obj =setDateTimeResp;
				 respcontent = setDateTimeResp.toString();
				 break;
			 case RESET_EQUIPMENT_RESP:
//				 ResetRespMessage restResp =new ResetRespMessage(header);
				 SetBaseRespMessage restResp =new SetBaseRespMessage(header);
				 restResp.byteToMessage(buffer);
				 obj =restResp;
				 respcontent = restResp.toString();
				 break;
			 case SET_REST_FACTORY_RESP:
//				 SetRestFactorySettingRespMessage restIpResp =new SetRestFactorySettingRespMessage(header);
				 SetBaseRespMessage restIpResp =new SetBaseRespMessage(header);
				 restIpResp.byteToMessage(buffer);
				 obj =restIpResp;
				 respcontent = restIpResp.toString();
				 break;
			 case SET_BASE_PARAM_RESP:
//				 SetBaseParamRespMessage setbpResp =new SetBaseParamRespMessage(header);
				 SetBaseRespMessage setbpResp =new SetBaseRespMessage(header);
				 setbpResp.byteToMessage(buffer);
				 obj =setbpResp;
				 respcontent = setbpResp.toString();
				 break;
			 case SET_AUTH_SECURITY_PARAM_RESP:
//				 SetAuthSecurityParamRespMessage setAuthResp =new SetAuthSecurityParamRespMessage(header);
				 SetBaseRespMessage setAuthResp =new SetBaseRespMessage(header);
				 setAuthResp.byteToMessage(buffer);
				 obj =setAuthResp;
				 respcontent = setAuthResp.toString();
				 break;
			 case SET_CONTROL_CARD_NAME_RESP:
				 SetControlCardNameRespMessage setCardName = new SetControlCardNameRespMessage(header);
				 setCardName.byteToMessage(buffer);
				 obj =setCardName;
				 respcontent = setCardName.toString();
				 break;
			 case CONTROL_SCREEN_POWER_RESP:
//				 ControlScreenPowerRespMessage controlScreenPower = new ControlScreenPowerRespMessage(header);
				 SetBaseRespMessage controlScreenPower =new SetBaseRespMessage(header);
				 controlScreenPower.byteToMessage(buffer);
				 obj =controlScreenPower;
				 respcontent = controlScreenPower.toString();
				 break;
			 case CONTROL_LOCAL_POWER_SWITCH_RESP:
//				 ControlLocalPowerSwitchRespMessage controlLocalPower = new ControlLocalPowerSwitchRespMessage(header);
				 SetBaseRespMessage controlLocalPower =new SetBaseRespMessage(header);
				 controlLocalPower.byteToMessage(buffer);
				 obj =controlLocalPower;
				 respcontent = controlLocalPower.toString();
				 break;
			 case CONTROL_CARD_POWER_SWITCH_RESP:
//				 ControlCardPowerSwitchRespMessage controlCardPower = new ControlCardPowerSwitchRespMessage(header);
				 SetBaseRespMessage controlCardPower =new SetBaseRespMessage(header);
				 controlCardPower.byteToMessage(buffer);
				 obj =controlCardPower;
				 respcontent = controlCardPower.toString();
				 break;
			 case SET_CONTROL_PARAM_RESP:
//				 SetControlParamRespMessage setControlP = new SetControlParamRespMessage(header);
				 SetBaseRespMessage setControlP =new SetBaseRespMessage(header);
				 setControlP.byteToMessage(buffer);
				 obj =setControlP;
				 respcontent = setControlP.toString();
				 break;
			 case SET_HIGH_TEMPERATURE_PARAM_RESP:
//				 SetHighTemperatureParamRespMessage setHighlP = new SetHighTemperatureParamRespMessage(header);
				 SetBaseRespMessage setHighlP =new SetBaseRespMessage(header);
				 setHighlP.byteToMessage(buffer);
				 obj =setHighlP;
				 respcontent = setHighlP.toString();
				 break;
			 case SET_VIRTUAL_CONN_TEST_RESP:
//				 SetVirtualConnTestRespMessage setVirtualResp = new SetVirtualConnTestRespMessage(header);
				 SetBaseRespMessage setVirtualResp =new SetBaseRespMessage(header);
				 setVirtualResp.byteToMessage(buffer);
				 obj =setVirtualResp;
				 respcontent = setVirtualResp.toString();
				 break;
			 case SET_POWER_EXCEPTION_PARAM_RESP:
//				 SetPowerExceptionParamRespMessage setPEResp = new SetPowerExceptionParamRespMessage(header);
				 SetBaseRespMessage setPEResp =new SetBaseRespMessage(header);
				 setPEResp.byteToMessage(buffer);
				 obj =setPEResp;
				 respcontent = setPEResp.toString();
				 break;
			 case SET_DETECTION_POINT_PARAM_RESP:
//				 SetDetectionPointRespMessage setDPResp = new SetDetectionPointRespMessage(header);
				 SetBaseRespMessage setDPResp =new SetBaseRespMessage(header);
				 setDPResp.byteToMessage(buffer);
				 obj =setDPResp;
				 respcontent = setDPResp.toString();
				 break;
			 case QUERY_DETECTION_POINT_PARAM_RESP:
				 QueryDetectionPointRespMessage queryDPResp = new QueryDetectionPointRespMessage(header);
				 queryDPResp.byteToMessage(buffer);
				 obj =queryDPResp;
				 respcontent = queryDPResp.toString();
				 break;
			 case SET_IN_SOURCE_PARAM_RESP:
//				 SetInSourceParamRespMessage setInSourceResp = new SetInSourceParamRespMessage(header);
				 SetBaseRespMessage setInSourceResp =new SetBaseRespMessage(header);
				 setInSourceResp.byteToMessage(buffer);
				 obj =setInSourceResp;
				 respcontent = setInSourceResp.toString();
				 break;
			 case UPGRADE_CONTROL_CARD_RESP:
//				  VersionUpdateControlCardRespMessage versionUpdateResp = new VersionUpdateControlCardRespMessage(header);
				  SetBaseRespMessage versionUpdateResp =new SetBaseRespMessage(header);
				  versionUpdateResp.byteToMessage(buffer);
				  obj =versionUpdateResp;
				  respcontent = versionUpdateResp.toString();
				  break;
			 case SET_SCREEN_TIME_SWITCH_PARAM_RESP:
//				 SetScreenTimeSwitchParamRespMessage setScreenTimeSwitchResp = new SetScreenTimeSwitchParamRespMessage(header);
				 SetBaseRespMessage setScreenTimeSwitchResp =new SetBaseRespMessage(header);
				 setScreenTimeSwitchResp.byteToMessage(buffer);
				 obj =setScreenTimeSwitchResp;
				 respcontent = setScreenTimeSwitchResp.toString();
				  break;
			 case SET_CMS_DISPLAY_TIME_RESP:
//				 SetCmsDisplayTimeRespMessage setDisplayTimeResp = new SetCmsDisplayTimeRespMessage(header);
				 SetBaseRespMessage setDisplayTimeResp =new SetBaseRespMessage(header);
				 setDisplayTimeResp.byteToMessage(buffer);
				 obj =setDisplayTimeResp;
				 respcontent = setDisplayTimeResp.toString();
				  break;
			 case SET_CMS_LIGHT_TIME_RESP:
//				 SetCmsLightTimeRespMessage setLightTimeResp = new SetCmsLightTimeRespMessage(header);
				 SetBaseRespMessage setLightTimeResp =new SetBaseRespMessage(header);
				 setLightTimeResp.byteToMessage(buffer);
				 obj =setLightTimeResp;
				 respcontent = setLightTimeResp.toString();
				  break;
			 case CLEAR_FILE_RESP:
//				 ClearFileRespMessage clearfileResp = new ClearFileRespMessage(header);
				 SetBaseRespMessage clearfileResp =new SetBaseRespMessage(header);
				 clearfileResp.byteToMessage(buffer);
				 obj =clearfileResp;
				 respcontent = clearfileResp.toString();
				  break;
			 case SET_DISPLAY_LIST_RESP:
//				 SetDisplayListRespMessage setDisplayListResp = new SetDisplayListRespMessage(header);
				 SetBaseRespMessage setDisplayListResp =new SetBaseRespMessage(header);
				 setDisplayListResp.byteToMessage(buffer);
				 obj =setDisplayListResp;
				 respcontent = setDisplayListResp.toString();
				  break;
			 case GET_SCREEN_IMG_RESP:
				 GetScreenImgRespMessage getBMPResp = new GetScreenImgRespMessage(header);
				 getBMPResp.byteToMessage(buffer,length -3);
				 try {
//	                saveFile(getBMPResp.getBlock(),"Test.bmp");
                }
                catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
                }
				 respcontent = getBMPResp.toString();
				  break;
			 case SEND_FILE_NAME_RESP:
				 SendFileNameRespMessage sendfilenameResp =new SendFileNameRespMessage(header);
//				 SetBaseRespMessage sendfilenameResp =new SetBaseRespMessage(header);
				 sendfilenameResp.byteToMessage(buffer);
				 obj =sendfilenameResp;
				 respcontent = sendfilenameResp.toString();
				 break;
			 case SEND_FILE_CONTENT_RESP:
				 SendFileContentRespMessage sendfileContentResp =new SendFileContentRespMessage(header);
				 sendfileContentResp.byteToMessage(buffer);
				 obj =sendfileContentResp;
				 respcontent = sendfileContentResp.toString();
				 break;
			 case SEND_FILE_COMPLETE:
//				 SendFileCompleteRespMessage sendfilecompleteResp =new SendFileCompleteRespMessage(header);
				 SetBaseRespMessage sendfilecompleteResp =new SetBaseRespMessage(header);
				 sendfilecompleteResp.byteToMessage(buffer);
				 obj =sendfilecompleteResp;
				 respcontent = sendfilecompleteResp.toString();
				 break;
			 case UPDATE_LOCAL_SCREEN_RESP:
//				 UpdateLocalScreenContentRespMessage updatelocalcontentResp = new UpdateLocalScreenContentRespMessage(header);
				 SetBaseRespMessage updatelocalcontentResp =new SetBaseRespMessage(header);
				 updatelocalcontentResp.byteToMessage(buffer);
				 obj =updatelocalcontentResp;
				 respcontent = updatelocalcontentResp.toString();
				 break;
			default:
				  break;
		 }
		 log.info("设备："+client+"响应："+respcontent);
		 return obj;
	 }
	
	/**
	 * 关闭端口
	 */
	public void stopSocket(SocketChannel socket) {
		try {
			socketIsAlive = true;
			if (socket != null) {
				socket.close();
				socket = null;
			}
		}
		catch (Exception e) {
			log.error("", e);
		}
	}
	
	private void saveFile(byte[] bytes,String path,String filename) throws Exception {
		try {
			File filedir = new File(path);
			if (!filedir.exists())
				filedir.mkdirs();
			File file = new File(filedir, filename);
			OutputStream os = new FileOutputStream(file);
			os.write(bytes, 0, bytes.length);
			os.flush();
			os.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 截取当前屏幕BMP
	 */
	public int captureScreen(int blockSize,SocketChannel socket,String path,String fileName) {

		Header header = new Header();
		GetScreenImgMessage info = new GetScreenImgMessage(header);
		info.setBlockSize(blockSize);
		info.setAddress(0xffff);

		// 创建指令
		ByteBuffer bytes = null;
		bytes = info.byteBufferedMessage(info.getHeader().getTotalLength());

		try {
			// 下发指令
			int num = socket.write(bytes);
//			System.out.println(num);
		}
		catch (Exception e) {
            log.error("发送截屏幕指令异常",e);
            return -1;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 一次性读数据
			byte[] rec = receiveData(socket);
			// 数据清洗
			List<byte[]> lis = DealBytes(rec);
			int bmp = 0;
			// 处理
			for (byte[] bs : lis) {

				int length = bs.length;
				// 解析头
				header = new Header();
				byte[] headbytes = new byte[3];
				System.arraycopy(bs, 0, headbytes, 0, 3);
				header.byteToMessage(headbytes);

				// 数据域 加解密？
				byte[] contentbytes = new byte[length - 3];
				System.arraycopy(bs, 3, contentbytes, 0, length - 3);

				ByteBuffer buffer = ByteBuffer.allocate(length - 3);
				buffer.put(contentbytes);
				buffer.flip();

				if (header.getCommand() == GET_SCREEN_IMG_RESP) {
					// 处理保存BMP数据
					GetScreenImgRespMessage getBMPResp = new GetScreenImgRespMessage(header);
					getBMPResp.byteToMessage(buffer, length - 3);
					if (getBMPResp.getBlock().length >= blockSize) {
						baos.write(getBMPResp.getBlock());
						bmp = 1;
					} else {
						baos.write(getBMPResp.getBlock());
						bmp = 2;
						byte[] bmps = baos.toByteArray();
						saveFile(bmps,path,fileName);
						baos.reset();
					}
				}/* else
					// 统一处理
					DealMessage(bs);*/
			}
			if (bmp == 1) {
				byte[] bmps = baos.toByteArray();
				if (bmps.length > 0)
					saveFile(bmps,path,fileName);
			}
			return 0;
		}
		catch (Exception e) {
			log.error("解析截屏幕指令响应异常",e);
            return -2;
		}
		finally {
			try {
				baos.close();
			}
			catch (Exception ex) {
			}
		}

	}
	
	/**
	 * 读取坏点信息
	 */
	public void readBadBlock(SocketChannel socket,String path){
		
		Header header = new Header();
		ReadBadPointMessage info = new ReadBadPointMessage(header);
		info.setAddress(0xffff);

		// 创建指令
		ByteBuffer bytes = null;
		bytes = info.byteBufferedMessage(info.getHeader().getTotalLength());

		try {
			// 下发指令
			int num = socket.write(bytes);
			System.out.println(num);
		}
		catch (Exception e) {

		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 一次性读数据
			byte[] rec = receiveData(socket);
			// 数据清洗
			List<byte[]> lis = DealBytes(rec);
			int bmp = 0;
			int blockSize =0;
			// 处理
			handlerReadBadPointMessage(baos,lis,blockSize,bmp,socket,path);
			if (bmp == 1) {
				byte[] bmps = baos.toByteArray();
				if (bmps.length > 0)
					saveFile(bmps,path,"badPoint.txt");
			}			
		}
		catch (Exception e) {

		}
		finally {
			try {
				baos.close();
			}
			catch (Exception ex) {
			}
		}

	}
	
	private void handlerReadBadPointMessage(ByteArrayOutputStream baos,List<byte[]> lis,int blockSize,int bmp,SocketChannel socket,String path){
		if(lis == null||lis.size()<=0) return;
		try{
		for (byte[] bs : lis) {

			int length = bs.length;
			// 解析头
			Header header = new Header();
			byte[] headbytes = new byte[3];
			System.arraycopy(bs, 0, headbytes, 0, 3);
			header.byteToMessage(headbytes);

			// 数据域 加解密？
			byte[] contentbytes = new byte[length - 3];
			System.arraycopy(bs, 3, contentbytes, 0, length - 3);

			ByteBuffer buffer = ByteBuffer.allocate(length - 3);
			buffer.put(contentbytes);
			buffer.flip();

			if (header.getCommand() == READ_BAD_POINT_RESP) {
				// 获取到 块大小 
				ReadBadPointRespMessage readBadPointResp = new ReadBadPointRespMessage(header);
				readBadPointResp.byteToMessage(buffer);
				blockSize = readBadPointResp.getBlock();
				System.out.print(readBadPointResp.toString());
			} 
			else if(header.getCommand() == READ_BAD_POINT_TWO){
				ReadBadPointTwoMessage readBadTwoPoint = new ReadBadPointTwoMessage(header);
				readBadTwoPoint.byteToMessage(buffer);
				System.out.print(readBadTwoPoint.toString());
				if (readBadTwoPoint.getBadPoint().length == blockSize) {
					baos.write(readBadTwoPoint.getBadPoint());
					bmp = 1;
					//响应
					handlerReadBadPointTwoRespMessage(0xffff,readBadTwoPoint.getBlockNum(),1,baos,blockSize,bmp,socket,path);					
				} else {
					baos.write(readBadTwoPoint.getBadPoint());
					bmp = 2;
					byte[] bmps = baos.toByteArray();
					saveFile(bmps,path,"badPoint.txt");
					baos.reset();
					//响应
					handlerReadBadPointTwoRespMessage(0xffff,readBadTwoPoint.getBlockNum(),1,baos,blockSize,bmp,socket,path);
				}
			}
			/*else 
				// 统一处理
				DealMessage(bs);*/
		}
		}
		catch(Exception e){
			
		}
	}
	
	private void handlerReadBadPointTwoRespMessage(int address,int blockNum,int result,ByteArrayOutputStream baos,int blockSize,int bmp,SocketChannel socket,String path){
		ReadBadPointTwoRespMessage resp =new ReadBadPointTwoRespMessage(new Header());
		resp.setAddress(address);
		resp.setBlockSize(blockNum);
		resp.setResult(result);
		ByteBuffer bytes = resp.byteBufferedMessage(resp.getHeader().getTotalLength());	
		//发送响应
		try {
	        socket.write(bytes);
	        byte[] rec = receiveData(socket);
	        List<byte[]> lis =DealBytes(rec);
	        //消息处理
	        handlerReadBadPointMessage(baos,lis,blockSize,bmp,socket,path);
	        
        }
        catch (IOException e) {
	       
	        e.printStackTrace();
        }	
	}
	
	/**
	 * 下发文件
	 * @param fileName
	 * @param filePath
	 * @return 0 同名，－１文件名接收失败，１成功
	 */
	public int sendFileName(String fileName,boolean ismd5,String filePath,boolean isdes,String deskey,SocketChannel socket){
		int ret = -1;
		if(ismd5){
			File file =new File(filePath);
			String md5="";
            try {
	            md5 = MD5.getFileMD5String(file);
            }
            catch (IOException e) {
	            e.printStackTrace();
            }
			String[] filename_split = fileName.split("\\.");
			fileName = filename_split[0]+"_"+md5+"."+filename_split[1];
		}
		System.out.print("下发文件名："+fileName+"\n");
		//下发文件名
		SendFileNameMessage sendFileName = new SendFileNameMessage(new Header());
		sendFileName.setAddress(0xffff);
		sendFileName.setFileName(fileName);
		sendFileName.setBlockSize(SendFileNameMessage.MAX_BLOCK_SIZE);
		if(isdes){
			sendFileName.setDES(isdes);
			sendFileName.setDesKey(deskey);
		}
		SendFileNameRespMessage resp =(SendFileNameRespMessage) sendCommond(sendFileName,socket,CommandID.SEND_FILE_NAME_RESP);
		
		if(resp != null) {
			if(resp.getResult()==SendFileNameRespMessage.RESULT_OK){
				//下发文件
				ret = 1;		
			}
			else if(resp.getResult()==SendFileNameRespMessage.RESULT_SIMILAR){
				//同名错误
				ret =0;
			}
			else{
				//接收失败
				ret =-1;
			}
		}	
		
		return  ret;
	}
	
	
	public int sendFileContent(String filePath,int blockSize,SocketChannel socket){
		
		if(filePath == null||filePath.isEmpty()){
			//文件路径为空，不合法
			return -1;
		}
		
		//读取文件
	     InputStream in = null;
		 try {
	            // 一次读多个字节
	            byte[] tempbytes = new byte[blockSize];
	            int byteread = 0;
	            in = new FileInputStream(filePath);
	            int blockNum = 0;
	            int ret= 0;
	            int beforeTemp =0;		
	            // 读入多个字节到字节数组中，byteread为一次读入的字节数
	            while ((byteread = in.read(tempbytes)) != -1) {
	            	beforeTemp = byteread;
//	                System.out.write(tempbytes, 0, byteread);
	                blockNum++;
	                //下发 文件
	                ret= sendFileContentBlock(blockNum,tempbytes,byteread,socket);
	               //重新发送 尝试 一次
	               if(ret== -1){
	            	   ret= sendFileContentBlock(blockNum,tempbytes,byteread,socket);
	            	   if(ret != 1||ret != 2){
	            		   return 0;//发送失败
	            	   }
	               }
	               if(ret ==2)
	            	   return ret;
	               
	            }	            
	            //下发　空数据内容消息
	        	if(beforeTemp==blockSize&& ret==1){
	        		 //下发 文件
	        		blockNum++;
	        		   ret= sendFileContentBlock(blockNum,null,0,socket);
		               //重新发送 尝试 一次
		               if(ret== -1){
		            	   ret= sendFileContentBlock(blockNum,null,0,socket);
		            	   if(ret != 1||ret != 2){
		            		   return 0;//发送失败
		            	   }
		               }
		               if(ret ==2)
		            	   return ret;
				}
				
	        	return ret;
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e1) {
	                }
	            }
	        }
		 return -1;
	}
	
	/**
	 * 发送文件块
	 * @param blockNum
	 * @param tempbytes
	 * @return
	 */
	private int sendFileContentBlock(int blockNum,byte[] tempbytes,int templength,SocketChannel socket ){
		byte[] temp =null;
		if(templength >0){
			temp =new byte[templength];
			System.arraycopy(tempbytes, 0, temp, 0, templength);
		}
		int ret = -1;
		  //下发 文件
    	SendFileContentMessage sendFileContent =new SendFileContentMessage(new Header());
		sendFileContent.setAddress(0xffff);
		sendFileContent.setBlockNum(blockNum);
		sendFileContent.setFileContent(temp);
		
		ByteBuffer bytes =sendFileContent.byteBufferedMessage(sendFileContent.getHeader().getTotalLength());
		try {
			// 下发指令
			int num = socket.write(bytes);
			System.out.println(num);
			// 一次性读数据
			byte[] rec = receiveData(socket);
			// 数据清洗
			List<byte[]> lis = DealBytes(rec);
			// 处理
			 ret = handlerSendFileContenMessage(lis,blockNum);
		}
		catch (Exception e) {
		}	
		
		return ret;
	}
		
		
	private int handlerSendFileContenMessage(List<byte[]> lis, int blockNum) {

		int ret =-1;
		try {
			for (byte[] bs : lis) {

				int length = bs.length;
				// 解析头
				Header header = new Header();
				byte[] headbytes = new byte[3];
				System.arraycopy(bs, 0, headbytes, 0, 3);
				header.byteToMessage(headbytes);

				// 数据域 加解密？
				byte[] contentbytes = new byte[length - 3];
				System.arraycopy(bs, 3, contentbytes, 0, length - 3);

				ByteBuffer buffer = ByteBuffer.allocate(length - 3);
				buffer.put(contentbytes);
				buffer.flip();

				if (header.getCommand() == SEND_FILE_CONTENT_RESP) {
					// 获取到 块大小
					SendFileContentRespMessage sendFileContentResp = new SendFileContentRespMessage(header);
					sendFileContentResp.byteToMessage(buffer);

					System.out.print(sendFileContentResp.toString());
					if (sendFileContentResp.getBlockNum() == blockNum && sendFileContentResp.getResult() == 1) {
						// 成功　继续下发
						ret = 1;
					} else {
						// 失败　重新下发
						ret = -1;
					}
				} else if (header.getCommand() == SEND_FILE_COMPLETE) {
					SendFileCompleteRespMessage sendFileComplete = new SendFileCompleteRespMessage(header);
					sendFileComplete.byteToMessage(buffer);
					System.out.print(sendFileComplete.toString());
					if (sendFileComplete.getResult() == 1) {
						// 响应发送成功
						ret = 2;
					} else {
						ret = -2;
					}
				} /*else
					// 统一处理
					DealMessage(bs,0);*/
			}
		}
		catch (Exception e) {

		}
		return ret;
	}
	
	/**
	 * 发送心跳消息
	 */
	public int makeActiveTest(String name,SocketChannel socket) {
		int ret =0;
		try {
			ActiveTestMessage atm = new ActiveTestMessage(new Header());
			atm.setName(name);
			atm.setAddress(0xffff);
		    
			if (log.isDebugEnabled()) {
				log.debug("发送了一个激活测试包。");
			}
			ActiveTestRespMessage atmresp = (ActiveTestRespMessage)sendCommond(atm,socket,CommandID.ACTIVE_TEST);
			if(atmresp != null){
				ret = 1;
			}
		}
		catch (Exception e) {
			stopSocket(socket);
			log.error("发送激活测试包时出现异常。", e);
		}
		return ret;
	}
	
	public static void main(String[] args) {
		
		SocketClient client = new SocketClient();
//		
		SocketChannel socket = client.connect("192.168.50.158",5000);
		Header header =new Header();
		QueryScreenStateMessage info = new QueryScreenStateMessage(header);
//		QueryBaseParamMessage info =new QueryBaseParamMessage(header);
//		QueryControlParamMessage info =new QueryControlParamMessage(header);
//		QueryDisplayContentMessage info =new QueryDisplayContentMessage(header);
//		QueryLightParamMessage info =new QueryLightParamMessage(header);
//		QueryVersionMessage info =new QueryVersionMessage(header);
//		GetScreenLatticeMessage info =new GetScreenLatticeMessage(header);
//		DetectionPointMessage info =new DetectionPointMessage(header);
//		ControlSwitchMessage info =new ControlSwitchMessage(header);
//		info.setSwitchs(ControlSwitchMessage.SCREEN_OPEN);
		
//		ControlLightMessage info =new ControlLightMessage(header);
//		info.setType(ControlLightMessage.LIGHT_TYPE_MANUAL);
//		info.setLevel(255);
		
//		SetControlLightMessage info =new SetControlLightMessage(header);
//		info.setLight1(1);
//		info.setWidth1(100);
		
		
//		SetDateTimeMessage info =new SetDateTimeMessage(header);
//		info.setDay(22);
//		info.setHours(17);
//		info.setMinutes(9);
//		info.setMonth(2);
//		info.setSeconds(20);
//		info.setYear(2017);
		
//		ResetMessage info = new ResetMessage(header);
		
//		SetBaseParamMessage info =new SetBaseParamMessage(header);
//		info.setCmsip("192.168.0.100");
//		info.setGateway("192.168.50.254");
//		info.setIp("192.168.50.158");
//		info.setMask("255.255.255.255");
//		info.setPort(5000);
//		info.setReport(0);
//		info.setScreenNum(0);
		
//		SetAuthSecurityParamMessage info =new SetAuthSecurityParamMessage(header);
//		info.setIsDES(SetAuthSecurityParamMessage.DES_CLOSE);
//		info.setIsMD5(SetAuthSecurityParamMessage.MD5_OPEN);
//		info.setKey("novamima");
//		info.setAuthorization("");
		
//		SetControlCardNameMessage info =new SetControlCardNameMessage(header);
//		info.setType(SetControlCardNameMessage.SET_NAME);
//		info.setName("Pluto2017中");
		
//		ControlScreenPowerMessage info= new ControlScreenPowerMessage(header);
//		info.setPowerswitch(ControlScreenPowerMessage.SCREEN_OPEN);
		
//		ControlLocalPowerSwitchMessage info =new ControlLocalPowerSwitchMessage(header);
//		info.setType(ControlLocalPowerSwitchMessage.SCREEN_OPEN);
		
//		ControlCardPowerSwitchMessage info =new ControlCardPowerSwitchMessage(header);
//		info.setLocIndex(0);
//		info.setPowerIndex(0);
//		info.setStatus(ControlCardPowerSwitchMessage.POWER_OPEN);
		
		
//		SetControlParamMessage info =new SetControlParamMessage(header);
//		info.setCloseScreen(0);
		
//		SetHighTemperatureParamMessage info = new SetHighTemperatureParamMessage(header);
//		info.setFanLocation(fanLocation);
//		info.setFanLocationCms(fanLocationCms);
//		info.setIsClosePower(isClosePower);
//		info.setIsOpen(isOpen);
//		info.setLimit(limit);
		
//		SetVirtualConnTestMessage info =new SetVirtualConnTestMessage(header);
//		info.setIsOpen(SetVirtualConnTestMessage.CLOSE);
//		info.setTime(100);
//		info.setBroadcast(0);
		
//		SetPowerExceptionParamMessage info = new SetPowerExceptionParamMessage(header);
//		info.setIsClose(SetPowerExceptionParamMessage.CLOSE);
		
//		SetDetectionPointMessage info = new SetDetectionPointMessage(header);
//		info.setLightColor(SetDetectionPointMessage.SCREEN_COLOR_BLUE);
//		info.setLimit(SetDetectionPointMessage.CURRENT_TWO);
//		info.setIsCurrentAdd(SetDetectionPointMessage.CLOSE_CURRENT_ADD);
//		info.setRed(2);
//		info.setGreen(2);
//		info.setBlue(2);
//		info.setType(SetDetectionPointMessage.TYPE_OPEN);
		
//		QueryDetectionPointMessage info =new QueryDetectionPointMessage(header);
		
//		SetInSourceParamMessage info =new SetInSourceParamMessage(header);
//		info.setType(SetInSourceParamMessage.SOURCE_OUT);
		
//		VersionUpdateControlCardMessage info =new VersionUpdateControlCardMessage(header);
//		info.setFileName("F939A.cab ");
		
		//设置 自动开关屏时间 人工 干预关屏 会失效 必须把 时间段去消掉
//		SetScreenTimeSwitchParamMessage info = new SetScreenTimeSwitchParamMessage(header);
//		info.setPtimeNum(0);
//		SwitchTimeInfo[] list =new SwitchTimeInfo[1];
//		SwitchTimeInfo s =new SwitchTimeInfo();
//		PTimeInfo p =new PTimeInfo();
//		p.setDay(17);
//		p.setHours(10);
//		p.setMinutes(10);
//		p.setMonth(2);
//		p.setSeconds(10);
//		p.setYear(2017);
//		s.setBeginTime(p);
//		PTimeInfo p1 =new PTimeInfo();
//		p1.setDay(17);
//		p1.setHours(12);
//		p1.setMinutes(00);
//		p1.setMonth(2);
//		p1.setSeconds(10);
//		p1.setYear(2017);
//		s.setEndTime(p1);
//		list[0] =s;
//		info.setStimeInfo(list);
		//测试成功
//		SetCmsDisplayTimeMessage info =new SetCmsDisplayTimeMessage(header);
//		
//		List<PlayTimeInfo> list =new ArrayList<PlayTimeInfo>();
//		PlayTimeInfo play =new PlayTimeInfo();
//		PTimeInfo p =new PTimeInfo();
//		p.setDay(10);
//		p.setHours(11);
//		p.setMinutes(56);
//		p.setMonth(3);
//		p.setSeconds(10);
//		p.setYear(2017);
//		play.setBeginTime(p);
//		PTimeInfo p1 =new PTimeInfo();
//		p1.setDay(10);
//		p1.setHours(14);
//		p1.setMinutes(20);
//		p1.setMonth(3);
//		p1.setSeconds(10);
//		p1.setYear(2017);
//		play.setEndTime(p1);
//		play.setPlaylistnum(2);
//		play.setPlaynum(2);
		
		
		/////////
//		play =new PlayTimeInfo();
//		 p =new PTimeInfo();
//		p.setDay(10);
//		p.setHours(14);
//		p.setMinutes(20);
//		p.setMonth(3);
//		p.setSeconds(10);
//		p.setYear(2017);
//		play.setBeginTime(p);
//		 p1 =new PTimeInfo();
//		p1.setDay(10);
//		p1.setHours(14);
//		p1.setMinutes(50);
//		p1.setMonth(3);
//		p1.setSeconds(10);
//		p1.setYear(2017);
//		play.setEndTime(p1);
//		play.setPlaylistnum(2);
//		play.setPlaynum(3);
		
//		info.setTimeNum(1);
//		list.add(play);
//		info.setPlaytimeInfo(list);
		//测试不成功
//		SetCmsLightTimeMessage info = new SetCmsLightTimeMessage(header);		
//
//		LightTimeInfo[] list =new LightTimeInfo[1];
//		LightTimeInfo play =new LightTimeInfo();
//		PTimeInfo p =new PTimeInfo();
//		p.setDay(22);
//		p.setHours(16);
//		p.setMinutes(46);
//		p.setMonth(2);
//		p.setSeconds(10);
//		p.setYear(2017);
//		play.setBeginTime(p);
//		PTimeInfo p1 =new PTimeInfo();
//		p1.setDay(22);
//		p1.setHours(16);
//		p1.setMinutes(48);
//		p1.setMonth(2);
//		p1.setSeconds(10);
//		p1.setYear(2017);
//		play.setEndTime(p1);
//		play.setLightValue(100);
//		info.setTimeNum(1);
//		list[0] =play;
//		info.setLightTimeInfo(list);
		
//		ClearFileMessage info =new ClearFileMessage(header);
//		info.setType(ClearFileMessage.CLEAR_NOUSE_FILE);
		
		//有可能没有消息返回
//		SetDisplayListMessage info = new SetDisplayListMessage(header);
//		info.setListNum(2);
		
//		ActiveTestMessage info =new ActiveTestMessage(header);
//		info.setName("Pluto2017中");
		
//		GetScreenImgMessage info =new GetScreenImgMessage(header);
//		info.setBlockSize(GetScreenImgMessage.MAX_BLOCK_SIZE);
		
		//测试不成功
//		UpdateLocalScreenContentMessage info =new UpdateLocalScreenContentMessage(header);
//		info.setAreaIndex(0);
//		info.setType(1);
//		String item="[item0]"+
//		        "param=100,1,1,1,0,5,1,0,1"+
//                "txt1=0,0,3,1616,1,8,中国你好,0,0,0"+
//		        "txtparam1=0,0";
//		info.setItem(item);
//		
		info.setAddress(0xffff);
	    client.sendCommond(info,socket,CommandID.QUERY_SCREEN_STATE_RESP);

		
//		client.CaptureScreen(1024);
	    
//		client.readBadBlock();
		
//		client.sendFileName("testLink.png",false,"F:\\T\\testLink.png",false,"12");
//		System.out.print("\n");
//		 int ret = client.sendFileContent("F:\\T\\testLink.png", SendFileContentMessage.FILE_BLOCK_MAX_SIZE);
//		System.out.print("ret:"+ret);
	    
//	    client.stopSocket(socket);
	    
//	    while(true){
//	      int ret = client.makeActiveTest("Pluto2017中");
//	      System.out.print(ret);
//	       System.out.print("\n");
//	       try {
//	        Thread.sleep(1000);
//        }
//        catch (InterruptedException e) {
//	        // TODO Auto-generated catch block
//	        e.printStackTrace();
//        }
//	    }
	    
	}
}
