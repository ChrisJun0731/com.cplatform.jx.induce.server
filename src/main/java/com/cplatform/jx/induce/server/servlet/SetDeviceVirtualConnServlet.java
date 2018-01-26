package com.cplatform.jx.induce.server.servlet;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.SetDeviceVirtualConnRequest;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.service.PushDeviceContentService;
import com.cplatform.jx.induce.server.service.SetDeviceParamService;
import com.cplatform.util2.FileTools;

import net.sf.json.JSONObject;

/**
 * 
 * �����豸��������. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��7�� ����3:03:24
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setDeviceVirtual")
public class SetDeviceVirtualConnServlet extends AbstractService<SetDeviceVirtualConnRequest, BaseResponse> {

	/** ��־��¼�� */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SetDeviceParamService setDeviceParamService;

	@Autowired
	private PushDeviceContentService pushContentService;

	@Override
	protected boolean needCheckSignature() {
		return false;
	}

	@Override
	protected boolean needCheckTransaction() {
		return false;
	}

	@Override
	public BaseResponse processRequest(SetDeviceVirtualConnRequest request) {
		try {
			if (request == null) {
				BaseResponse response = new BaseResponse();
				response.setStatus(-3);
				response.setStatusText("��������쳣");
				return response;
			}
			if (request.getIsOpen() == 0) {
				BaseResponse response = setDeviceParamService.process(request, CommandID.SET_VIRTUAL_CONN_TEST);
				return response;
			} else {
				// ����PLAY001.LST
				String temp = dealFile(request.getPath());
				if (temp == null) {
					logger.info("������Դ�ļ�ʧ��,�޷����������Ӳ����б� request=" + request.toString());
					BaseResponse response = new BaseResponse();
					response.setStatus(-3);
					response.setStatusText("�����б����쳣");
					return response;
				} else {
					request.setBroadcast(100);
					request.setIsOpen(1);
					BaseResponse response = pushContentService.process(request, 2, request.getIp());
					
					//ɾ����ʱ�ļ�
					delFile(temp);
					return response;
				}
			}
		} catch (Exception ex) {
			logger.error(ex, ex);
			BaseResponse response = new BaseResponse();
			response.setStatus(-2);
			response.setStatusText("Exception");
			return response;
		}
	}

	@Override
	protected SetDeviceVirtualConnRequest requestJsonToObject(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return (SetDeviceVirtualConnRequest) JSONObject.toBean(jsonObject, SetDeviceVirtualConnRequest.class);
	}

	private void delFile(String filePath) {
		File delFile = new File(filePath);
		FileTools.rmDir(delFile);
	}

	private String dealFile(String filePath) {
		// ������ʱ�ļ���
		// �����ļ�
		// lst������ play001.lst
		try {
			List<File> fileList = FileTools.listFile(new File(filePath));
			if (fileList == null || fileList.size() == 0) {
				logger.error("��ȡ��Դ�ļ��쳣 path:" + filePath);
				return null;
			}
			String temp = "";
			if (filePath.endsWith("/")) {
				temp = filePath + "temp";
			} else {
				temp = filePath + "/temp";
			}
			
			//��ɾ�� temp
			new File(temp).delete();

			for (File file : fileList) {
				if (file.getName().endsWith("lst")) {
					FileTools.copy(filePath + "/" + file.getName(), temp + "/play100.lst");
				} else {
					FileTools.copy(filePath + "/" + file.getName(), temp + "/" + file.getName());
				}
			}
			return temp;
		} catch (Exception e) {
			logger.error("������Դ�ļ��쳣 path:" + filePath + " error:" + e);
		}
		return null;
	}

}
