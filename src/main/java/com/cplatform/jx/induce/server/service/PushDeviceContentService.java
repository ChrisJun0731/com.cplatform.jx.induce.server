package com.cplatform.jx.induce.server.service;

import java.io.File;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.PlayShow;
import com.cplatform.jx.induce.PlayTime;
import com.cplatform.jx.induce.PushContentRequest;
import com.cplatform.jx.induce.PushRealTimeContentRequest;
import com.cplatform.jx.induce.SetDeviceVirtualConnRequest;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.message.Header;
import com.cplatform.jx.induce.server.protocol.message.SendFileContentMessage;
import com.cplatform.jx.induce.server.protocol.message.SetBaseRespMessage;
import com.cplatform.jx.induce.server.protocol.message.SetCmsDisplayTimeMessage;
import com.cplatform.jx.induce.server.protocol.message.SetDisplayListMessage;
import com.cplatform.jx.induce.server.protocol.message.SetVirtualConnTestMessage;
import com.cplatform.jx.induce.server.protocol.message.info.PTimeInfo;
import com.cplatform.jx.induce.server.protocol.message.info.PlayTimeInfo;
import com.cplatform.jx.induce.server.protocol.net.SocketClient;
import com.cplatform.jx.induce.server.protocol.net.SocketConnectionPool;
import com.cplatform.spring.sync.SyncLock;

/**
 * 
 * ���ݷ���. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��7�� ����4:11:19
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Component
public class PushDeviceContentService {

	/** ��־��¼�� */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SocketClient socketClietnt;

	@Autowired
	private SocketConnectionPool socketConnPool;

	/** sysConfig */
	@Resource(name = "sysConfig")
	private PropertiesConfiguration sysConfig;

	/***
	 * 
	 * @param request
	 * @param type 0 ��ʱ���ţ�1 ʵʱ���� ��2�����Ӵ�����
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	@SyncLock(key = "#ip")
	public BaseResponse process(Object request, int type, String ip) throws Exception {
		//
		String host = null;
		String filePath = null;
		SocketChannel socket = null;
		try {
			BaseResponse response = null;
			
			if (type == 0) {
				host = ((PushContentRequest) request).getIp();
				filePath = ((PushContentRequest) request).getFilePath();
			} else if(type ==1) {
				host = ((PushRealTimeContentRequest) request).getIp();
				filePath = ((PushRealTimeContentRequest) request).getFilePath();
			} else{
				host = ((SetDeviceVirtualConnRequest) request).getIp();
				filePath = ((SetDeviceVirtualConnRequest) request).getPath();
			}
			socket = socketConnPool.getConnection(host, sysConfig.getInt("device.port", 5000)); // socketClietnt.connect(request.getIp(),
																								// sysConfig.getInt("device_port",
																								// 5000));
			if (socket != null) {

				// ������Դ�ļ�
				logger.info("��ʼ���豸:" + host + " �ϴ��ļ�...");
				List<File> lis = GetFileList(filePath);
				int result = 0;
				if (lis != null && (!lis.isEmpty())) {
					for (File f : lis) {
						result = SendFile(f.getName(), false, filePath, false, "", socket);
						if (result == 2) {
							logger.info("�ɹ����豸:" + host + " �ϴ��ļ�:" + f.getName());
							continue;
						} else {
							break;
						}
					}
				}

				if (result == 2) {
					if (type == 0) {
						PushContentRequest req = (PushContentRequest) request;
						logger.info("��ʼ���豸:" + host + " ���Ͷ�ʱ�����б�...");
						// ���Ͷ�ʱ����
						List<PlayShow> playtiem = req.getShow();
						if (playtiem != null && (!playtiem.isEmpty())) {
							int num = 0;
							List<PlayTimeInfo> plays = new ArrayList<PlayTimeInfo>();
							for (PlayShow entry : playtiem) {

								int item = entry.getItem();
								List<PlayTime> play = entry.getPlayTime();

								for (PlayTime time : play) {

									String start = time.getStartTime();
									String end = time.getEndTime();

									PlayTimeInfo playtime = new PlayTimeInfo();
									PTimeInfo ps = new PTimeInfo();
									ps.setDay(Integer.valueOf(start.substring(6, 8)));
									ps.setHours(Integer.valueOf(start.substring(8, 10)));
									ps.setMinutes(Integer.valueOf(start.substring(10, 12)));
									ps.setMonth(Integer.valueOf(start.substring(4, 6)));
									ps.setSeconds(Integer.valueOf(start.substring(12, 14)));
									ps.setYear(Integer.valueOf(start.substring(0, 4)));
									playtime.setBeginTime(ps);

									PTimeInfo pe = new PTimeInfo();
									pe.setDay(Integer.valueOf(end.substring(6, 8)));
									pe.setHours(Integer.valueOf(end.substring(8, 10)));
									pe.setMinutes(Integer.valueOf(end.substring(10, 12)));
									pe.setMonth(Integer.valueOf(end.substring(4, 6)));
									pe.setSeconds(Integer.valueOf(end.substring(12, 14)));
									pe.setYear(Integer.valueOf(end.substring(0, 4)));
									playtime.setEndTime(pe);

									playtime.setPlaylistnum(req.getPlaylistnum());
									playtime.setPlaynum(item);

									plays.add(playtime);
									num++;
								}
							}

							SetCmsDisplayTimeMessage cmsdisplay = new SetCmsDisplayTimeMessage(new Header());
							cmsdisplay.setAddress(BaseRequest.CLIENT_ADDRESS);
							cmsdisplay.setPlaytimeInfo(plays);
							cmsdisplay.setTimeNum(num);
							SetBaseRespMessage obj = (SetBaseRespMessage) socketClietnt.sendCommond(cmsdisplay, socket,
									CommandID.SET_CMS_DISPLAY_TIME_RESP);

							if (obj != null) {
								response = new BaseResponse();
								if (obj.getResult() == 1) {
									response.setStatus(BaseResponse.STATUS_OK);
								} else {
									response.setStatus(-6);
									response.setStatusText("���ò���ʱ��ʧ��");
								}

							} else {
								response = new BaseResponse();
								response.setStatus(-1);
								response.setStatusText("�豸����Ӧ");
							}

						} else {
							response = new BaseResponse();
							response.setStatus(-4);
							response.setStatusText("����ʱ�䲻��Ϊ��");
						}
					} else if(type ==1){
						PushRealTimeContentRequest realreq = (PushRealTimeContentRequest) request;
						logger.info("��ʼ���豸:" + host + " ����ָ�������б� :" + realreq.getPlaylistnum());

						SetDisplayListMessage setdisplay = new SetDisplayListMessage(new Header());
						setdisplay.setAddress(BaseRequest.CLIENT_ADDRESS);
						setdisplay.setListNum(realreq.getPlaylistnum());

						SetBaseRespMessage obj = (SetBaseRespMessage) socketClietnt.sendCommond(setdisplay, socket,
								CommandID.SET_DISPLAY_LIST_RESP);

						if (obj != null) {
							response = new BaseResponse();
							if (obj.getResult() == 1) {
								response.setStatus(BaseResponse.STATUS_OK);
							} else {
								response.setStatus(-6);
								response.setStatusText("����ָ�������б�ʧ��");
							}

						} else {
							response = new BaseResponse();
							response.setStatus(-1);
							response.setStatusText("�豸����Ӧ");
						}
					} else {
						
						SetDeviceVirtualConnRequest vir = (SetDeviceVirtualConnRequest)request;
						SetVirtualConnTestMessage virtest =new SetVirtualConnTestMessage(new Header());
						virtest.setAddress(BaseRequest.CLIENT_ADDRESS);
						virtest.setBroadcast(vir.getBroadcast());
						virtest.setIsOpen(vir.getIsOpen());
						virtest.setTime(vir.getTime());
						SetBaseRespMessage obj = (SetBaseRespMessage) socketClietnt.sendCommond(virtest, socket,CommandID.SET_VIRTUAL_CONN_TEST_RESP);
						if (obj != null) {
							response = new BaseResponse();
							if (obj.getResult() == 1) {
								response.setStatus(BaseResponse.STATUS_OK);
							} else {
								response.setStatus(-6);
								response.setStatusText("����������ʧ��");
							}

						} else {
							response = new BaseResponse();
							response.setStatus(-1);
							response.setStatusText("�豸����Ӧ");
						}
					}

				} else {
					response = new BaseResponse();
					response.setStatus(-5);
					response.setStatusText("��Դ�ļ��ϴ�ʧ��");
				}

			} else {
				response = new BaseResponse();
				response.setStatus(-1);
				response.setStatusText("�豸������");

			}

			return response;
		} catch (Exception ex) {
			logger.error(ex, ex);
			BaseResponse response = new BaseResponse();
			response.setStatus(-2);
			response.setStatusText("Exception");
			return response;
		} finally {
			// socketClietnt.stopSocket(socket);
			socketConnPool.returnConnection(host, sysConfig.getInt("device.port", 5000), socket);
		}
	}

	private List<File> GetFileList(String filePath) {
		List<File> listFile = null;
		File file = new File(filePath);
		File[] files = file.listFiles();
		if (null != files && files.length > 0) {
			listFile = new ArrayList<File>();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					listFile.add(files[i]);
				}
			}
		}
		return listFile;
	}

	private int SendFile(String fileName, boolean ismd5, String filePath, boolean isDes, String desKey,
			SocketChannel socket) {
		int ret = -1;
		// �����ļ�����
		ret = socketClietnt.sendFileName(fileName, ismd5, filePath + "/" + fileName, isDes, desKey, socket);
		if (ret == 1) {
			// �����ļ�����
			ret = socketClietnt.sendFileContent(filePath + "/" + fileName, SendFileContentMessage.FILE_BLOCK_MAX_SIZE,
					socket);
		}

		return ret;
	}

	@PostConstruct
	private void init() throws Exception {
		socketConnPool.createPool();
	}
}
