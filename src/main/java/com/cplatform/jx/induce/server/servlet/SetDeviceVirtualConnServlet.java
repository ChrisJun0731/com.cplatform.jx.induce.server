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
 * 设置设备虚拟连接. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月7日 下午3:03:24
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * 
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Controller
@RequestMapping("/setDeviceVirtual")
public class SetDeviceVirtualConnServlet extends AbstractService<SetDeviceVirtualConnRequest, BaseResponse> {

	/** 日志记录器 */
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
				response.setStatusText("请求参数异常");
				return response;
			}
			if (request.getIsOpen() == 0) {
				BaseResponse response = setDeviceParamService.process(request, CommandID.SET_VIRTUAL_CONN_TEST);
				return response;
			} else {
				// 处理PLAY001.LST
				String temp = dealFile(request.getPath());
				if (temp == null) {
					logger.info("处理资源文件失败,无法设置虚连接播放列表。 request=" + request.toString());
					BaseResponse response = new BaseResponse();
					response.setStatus(-3);
					response.setStatusText("播放列表处理异常");
					return response;
				} else {
					request.setBroadcast(100);
					request.setIsOpen(1);
					BaseResponse response = pushContentService.process(request, 2, request.getIp());
					
					//删除临时文件
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
		// 创建临时文件夹
		// 拷贝文件
		// lst改名成 play001.lst
		try {
			List<File> fileList = FileTools.listFile(new File(filePath));
			if (fileList == null || fileList.size() == 0) {
				logger.error("获取资源文件异常 path:" + filePath);
				return null;
			}
			String temp = "";
			if (filePath.endsWith("/")) {
				temp = filePath + "temp";
			} else {
				temp = filePath + "/temp";
			}
			
			//先删除 temp
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
			logger.error("拷贝资源文件异常 path:" + filePath + " error:" + e);
		}
		return null;
	}

}
