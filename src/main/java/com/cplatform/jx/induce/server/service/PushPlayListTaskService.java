package com.cplatform.jx.induce.server.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.BaseResponse;
import com.cplatform.jx.induce.DeviceInfo;
import com.cplatform.jx.induce.DevicePresetInfo;
import com.cplatform.jx.induce.PlayListTask;
import com.cplatform.jx.induce.PlayTaskLogInfo;
import com.cplatform.jx.induce.PushRealTimeContentRequest;
import com.cplatform.util2.FileTools;

/**
 * 
 * ���Ͳ������ݷ���. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2017��3��16�� ����9:47:30
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Component
public class PushPlayListTaskService {

	/**��������id*/
	private final int BLACKPLAY_TASKID = -200;
	@Autowired
	private PushDeviceContentService pushContentService;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	private ActInduceDao dao;

	/** ��־��¼�� */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** sysConfig */
	@Resource(name = "sysConfig")
	private PropertiesConfiguration sysConfig;


	private Map<String,Set<PlayTaskLogInfo>> taskRecord = new ConcurrentHashMap<String,Set<PlayTaskLogInfo>>();
	
	/**��ǰ��Ļ���ŵ�����ID*/
	private Map<String,String> nowPlayTaskId = new HashMap<String, String>();
	
	/**
	 * ��ȡ��ǰ��������ID
	 * @param deviceCode
	 * @return
	 */
	private String getnowPlayTaskId(String deviceCode){
		String black = nowPlayTaskId.get(deviceCode);
		if(black == null)
			black = "";
		return black;
	}
	
	/**
	 * ���õ�ǰ��������ID
	 * @param deviceCode
	 * @param taskId
	 */
	private void setnowPlayTaskId(String deviceCode,String taskId){
		nowPlayTaskId.put(deviceCode, taskId);
	}
	/**
	 * @return �����Ƿ��Ѿ�ִ��
	 */
	private PlayTaskLogInfo getTaskExe(String deviceCode,int taskId) {
		Set<PlayTaskLogInfo> info = taskRecord.get(deviceCode);
		String nowTime = dateFormat.format(new Date()).substring(0, 8);
		
		if(info != null){
			for(PlayTaskLogInfo log:info){
				//���� ���Զ�����ͣ��������������⣿
				if(log.getPushTime().compareTo(nowTime)<0||taskId == BLACKPLAY_TASKID||taskId<0){
					info.remove(log);
				}
				else{
					if(log.getTaskId()== taskId){
						return log;
					}
				}
			}			
		}
		return null;
	}
	/**
	 * �����Ѵ����¼
	 * @param info
	 */
	private void putTaskLogToMap(PlayTaskLogInfo info){
		if(info == null) return;
		Set<PlayTaskLogInfo> infos = taskRecord.get(info.getDeviceCode());
		if(infos != null){
			for(PlayTaskLogInfo log:infos){
				if(log.getTaskId() == info.getTaskId())
					infos.remove(log);
			}
		}
		else{
			infos = new HashSet<PlayTaskLogInfo>();
			taskRecord.put(info.getDeviceCode(), infos);
		}
		
		infos.add(info);
	}
	
	/**
	 * ������м�¼
	 */
	public void removeAll(){
		taskRecord.clear();
	}

	/**
	 * �ж� �����Ƿ�����Ч����
	 * 
	 * @param playListTask
	 *            ������Ϣ
	 * @return 
	 */
	private boolean getTaskTimeout(PlayListTask playListTask) {
		try {
			String nowTime = dateFormat.format(new Date());
			String playTime = playListTask.getPlayTime();
			
			String[] play = playTime.split("-");
			String nowSub = nowTime.substring(8, 12);

			if(play[1].compareTo(nowSub)>0){
				return true;
			}
			return false;
		}
		catch (Exception ex) {
			logger.error("����������Ч���쳣", ex);
			return false;
		}
	}

	/**
	 * ��ȡ��Ŀ�б��� ����һ����Ч�Ľ�Ŀ
	 * @param info
	 * @return
	 */
	private PlayListTask getLastEffective(List<PlayListTask> info){
		for(PlayListTask task:info){
			if(getTaskTimeout(task)&&task.getIsAuto()==0)
				return task;
		}
		return null;
	}
	
	/**
	 * ��ȡ�������Ž�Ŀ
	 * @param task
	 * @return
	 */
	private PlayListTask getBlankPlayList(DeviceInfo dev){
		String nowTime = dateFormat.format(new Date());
		String startTime = nowTime.substring(8, 12);
		String endTime = "2400";
		PlayListTask task = new PlayListTask();
		task.setPlaylistName("blackplay.lst");
		task.setPlaylistPath("data/traffic/");
		task.setPlayTime(startTime+"-"+endTime);
		task.setId(BLACKPLAY_TASKID);
		task.setDeviceCode(dev.getDeviceCode());
		return task;
	}
	public static void main(String[] args) {
		System.out.println("-200".compareTo("-200"));
		System.out.println("".compareTo("100"));
		System.out.println("-200".compareTo("0"));
	}
	/**
	 * ������Ҫ���͵�����<br>
	 * 
	 * @param actOrderId
	 *            ����ID
	 * @throws Exception
	 *             �쳣
	 */
	public void process(DeviceInfo deviceInfo) throws Exception {
		logger.info("��ʼ�������������,device={}" , deviceInfo.toString());
		//��ȡ��ǰ��������ID
		String currentTaskId=getnowPlayTaskId(deviceInfo.getDeviceCode());
		//�Զ������б�
		
		//�ֶ������б�
		List<PlayListTask> info = dao.getPushPlayListTaskId(deviceInfo.getDeviceCode());
		//Ԥ������
		PlayListTask devicePresetInfo=dao.getDevicePresetInfo(deviceInfo.getDeviceCode());
		PlayListTask task = null;
		if(info == null||info.isEmpty()){
			logger.info("������������Ĳ����б�����Ϊ��");
			if(StringUtils.isNotBlank(currentTaskId)){
				logger.info("��ǰ����currentTaskId��Ϊ��");
				if(currentTaskId.contains("-")){//��ǰ����ΪԤ�����ݻ����
					logger.info("��ǰ����ΪԤ�����ݻ����");
					if(StringUtils.equals(currentTaskId, BLACKPLAY_TASKID+"")&&devicePresetInfo!=null){//��ǰ����Ϊ��������Ԥ�����ݲ�Ϊ��
						//����Ԥ������
						logger.info("����Ԥ�����ݲ����б�1, deviceCode={}", deviceInfo.getDeviceCode());
						task=devicePresetInfo;
						info = new ArrayList<PlayListTask>();
						info.add(task);
					}else{
						//��ǰ����ΪԤ������
						logger.info("��ǰû����Ҫ��������, deviceCode={}", deviceInfo.getDeviceCode());
						return;
					}
					
				}else{
					logger.info("��ǰ������Ԥ������Ҳ���Ǻ���");
					Boolean isInvalid=dao.isInvalid(Integer.parseInt(currentTaskId));
					if(!isInvalid){//ʧЧ
						logger.info("��ǰ����currentTaskId"+currentTaskId+"�Ѿ�ʧЧ");
						if(devicePresetInfo!=null){
							//����Ԥ������
							logger.info("����Ԥ�����ݲ����б�1, deviceCode={}", deviceInfo.getDeviceCode());
							task=devicePresetInfo;
							info = new ArrayList<PlayListTask>();
							info.add(task);
							
						}else{
							
							//���ں�����Ŀ
							logger.info("���Ӻ��������б�1, deviceCode={}", deviceInfo.getDeviceCode());
							task = getBlankPlayList(deviceInfo);
							info = new ArrayList<PlayListTask>();
							info.add(task);
						}
					}else{
						logger.info("��ǰû����Ҫ��������1, deviceCode={}", deviceInfo.getDeviceCode());
						return;
					}
				}
				
			}else{
				logger.info("��ǰ����currentTaskIdΪ��");
				if(devicePresetInfo!=null){
					//����Ԥ������
					logger.info("����Ԥ�����ݲ����б�1, deviceCode={}", deviceInfo.getDeviceCode());
					task=devicePresetInfo;
					info = new ArrayList<PlayListTask>();
					info.add(task);
					
				}else{
					
					//���ں�����Ŀ
					logger.info("���Ӻ��������б�1, deviceCode={}", deviceInfo.getDeviceCode());
					task = getBlankPlayList(deviceInfo);
					info = new ArrayList<PlayListTask>();
					info.add(task);
				}
			}
			
//			if(!currentTaskId.contains("-")){
//					
//					
//					if(devicePresetInfo!=null){
//						//����Ԥ������
//						logger.info("����Ԥ�����ݲ����б�1, deviceCode={}", deviceInfo.getDeviceCode());
//						task=devicePresetInfo;
//						info = new ArrayList<PlayListTask>();
//						info.add(task);
//						
//					}else{
//						
//						//���ں�����Ŀ
//						logger.info("���Ӻ��������б�1, deviceCode={}", deviceInfo.getDeviceCode());
//						task = getBlankPlayList(deviceInfo);
//						info = new ArrayList<PlayListTask>();
//						info.add(task);
//					}
//					
//			}
//			else{
//			logger.info("��ǰû����Ҫ��������, deviceCode={}", deviceInfo.getDeviceCode());
//			return;
//			}
		}else{
			logger.info("������������Ĳ����б�����Ϊ�գ�sizeΪ"+info.size());
		}

		//�ȶԵ�ǰʱ�䣬��ǰ���� 
		 task = info.get(0);
		
		logger.info("��������,playTask={}",task.toString());
		PlayTaskLogInfo logs = getTaskExe(deviceInfo.getDeviceCode(),task.getId());
		if(logs != null){
			//�ж� ��ǰ�Ƿ�Ϊ ��������ID ��  �ж��Ƿ��Ѿ�ʧЧ�����ʧЧѰ�����һ��û��ʧЧ�Ľ�Ŀ ���͵�ƽ�ϣ����û�� ֱ���� ����
//			if(getnowPlayTaskId(deviceInfo.getDeviceCode()).compareTo(""+task.getId())==0){
//				String nowTime = dateFormat.format(new Date());
//				String nowSub = nowTime.substring(8, 12);
//				String [] playtiem = task.getPlayTime().split("-");
//				if(playtiem[1].compareTo(nowSub)<=0){
//					//���� ��������ѯ���һ�� û�е�ʧЧʱ��ļ�¼���������� �����û��ֱ�����ͺ���
//					PlayListTask temp = getLastEffective(info);
//					if(temp == null){
//						//���ں�����Ŀ
//						task = getBlankPlayList(deviceInfo);
//						logger.info("���Ӻ��������б�2, deviceCode={}", deviceInfo.getDeviceCode());
//					}
//					else{
//						task = temp;
//						logger.info("��ȡ�����һ��δʧЧ����2,playTask={}",task.toString());
//					}
//				}
//				else{
//					logger.info("��ǰ��Ļ���ŵ�����,playTask={},logTask={}",task.toString(),logs.toString());
//					return;
//				}
//				
//			}
//			else{
				//��ȡ��ǰ�Ĳ����б�
//				String taskId = getnowPlayTaskId(deviceInfo.getDeviceCode());
//				if(currentTaskId.equals(BLACKPLAY_TASKID)){
				if(currentTaskId.contains("-")){
					PlayListTask temp = getLastEffective(info);
					if(temp == null){
						
						if(devicePresetInfo!=null){
							//����Ԥ������
							task=devicePresetInfo;
							logger.info("����Ԥ�����ݲ����б�,playTask={},logTask={}", task.toString(),logs.toString());
						}else{
							
							//���ں�����Ŀ
							task = getBlankPlayList(deviceInfo);
							logger.info("��ǰ��Ļ���ŵ�����,playTask={},logTask={}",task.toString(),logs.toString());
//							return;
						}
					}
					else{
						task = temp;
						logger.info("��ȡ�����һ��δʧЧ����3,playTask={}",task.toString());
					}
				}
				else{
					
					String nowTime = dateFormat.format(new Date());
					String nowSub = nowTime.substring(8, 12);
					String [] playtiem = task.getPlayTime().split("-");
					if(playtiem[1].compareTo(nowSub)<=0){
						//���� ��������ѯ���һ�� û�е�ʧЧʱ��ļ�¼���������� �����û��ֱ�����ͺ���
						PlayListTask temp = getLastEffective(info);
						if(temp == null){
//							//���ں�����Ŀ
//							task = getBlankPlayList(deviceInfo);
//							logger.info("���Ӻ��������б�3, deviceCode={}", deviceInfo.getDeviceCode());
							
							if(devicePresetInfo!=null){
								//����Ԥ������
								task=devicePresetInfo;
								logger.info("����Ԥ�ò����б�3, deviceCode={}", deviceInfo.getDeviceCode());
							}else{
								
								//���ں�����Ŀ
								task = getBlankPlayList(deviceInfo);
								logger.info("���Ӻ��������б�3, deviceCode={}", deviceInfo.getDeviceCode());
//								return;
							}
						}
						else{
							task = temp;
							logger.info("��ȡ�����һ��δʧЧ����4,playTask={}",task.toString());
						}
					}
					else{
						logger.info("��ǰ��Ļ���ŵ�����1,playTask={},logTask={}",task.toString(),logs.toString());
						return;
					}
				}
				
			}
//		}
		
		if(!getTaskTimeout(task)){
			//�ж� ��ǰ�Ƿ�Ϊ ��������ID ��  �ж��Ƿ��Ѿ�ʧЧ�����ʧЧѰ�����һ��û��ʧЧ�Ľ�Ŀ ���͵�ƽ�ϣ����û�� ֱ���� ����
//			if(currentTaskId.compareTo(""+BLACKPLAY_TASKID)!=0){
				if(!currentTaskId.contains("-")){
				//���� ��������ѯ���һ�� û�е�ʧЧʱ��ļ�¼���������� �����û��ֱ�����ͺ���
				PlayListTask temp = getLastEffective(info);
				if(temp == null){
				
				
					if(devicePresetInfo!=null){
						//����Ԥ������
						task=devicePresetInfo;
						logger.info("����Ԥ�ò����б�4, deviceCode={}", deviceInfo.getDeviceCode());
					}else{
						
						//���ں�����Ŀ
						task = getBlankPlayList(deviceInfo);
						logger.info("���Ӻ��������б�4, deviceCode={}", deviceInfo.getDeviceCode());
//						return;
					}	
					
					
//				//���ں�����Ŀ
//				task = getBlankPlayList(deviceInfo);
//				logger.info("���Ӻ��������б�4, deviceCode={}", deviceInfo.getDeviceCode());
			  }	
				else{
					task = temp;
					logger.info("��ȡ�����һ��δʧЧ����4,playTask={}",task.toString());
				}
			}
			else{
		      logger.info("��������Ч����,�����ٴη���,playTask={}",task.toString());
		      return;
			}
		}
		
		//���� ��ǰ�Ѿ���������
		if(currentTaskId.compareTo(""+task.getId())==0){
			logger.info("Ŀǰ��Ļ���ŵ�����,playTask={},logTask={}",task.toString(),logs);
			return;
		}
		
		//���»�ȡ�豸��Ϣ����ֹ�豸�Ѿ�ʧЧ
		DeviceInfo device = dao.getDeviceInfo(deviceInfo.getDeviceCode());
		
		if(device == null){
			logger.info("��ȡ�豸��Ϣʧ��,�޷����͵�ǰ���� deviceCode={},playTaskId={}", deviceInfo.getDeviceCode(),task.getId());
			return;
		}

		//����PLAY001.LST 
		String temp = dealFile(task.getPlaylistPath());
		if(temp == null) {
			logger.info("������Դ�ļ�ʧ��,�޷����͵�ǰ���� deviceCode={},playTaskId={}", deviceInfo.getDeviceCode(),task.getId());
			return;
		}
		String time = dateFormat.format(new Date());
		PushRealTimeContentRequest request =new PushRealTimeContentRequest();
		request.setIp(deviceInfo.getIp());
		request.setAddress(BaseRequest.CLIENT_ADDRESS);
		request.setFilePath(temp);
		request.setPlaylistnum(1);
		request.setRequestTime(time);
		
		BaseResponse resp = pushContentService.process(request, 1, request.getIp());
		
		//���� TASK ״̬
		if(resp.getStatus() == BaseResponse.STATUS_OK){		
			//��¼��ǰ���·�����
			PlayTaskLogInfo log = new PlayTaskLogInfo();
			log.setDeviceCode(deviceInfo.getDeviceCode());
			log.setTaskId(task.getId());
			log.setPushTime(time.substring(0, 8));	
			putTaskLogToMap(log);
			
			setnowPlayTaskId(deviceInfo.getDeviceCode(),""+task.getId());
		}
		
		delFile(temp);
		logger.info("������ʾ����,Task={},Resp={}",request.toString(),resp.toString());
				
	}
	
	private void delFile(String filePath){
		File delFile = new File(filePath);
		FileTools.rmDir(delFile);
	}
	private String  dealFile(String filePath){
		//������ʱ�ļ���
		//�����ļ�
		//lst������ play001.lst
		try{
			//��ɾ�� temp
			String temp ="";
			if(filePath.endsWith("/")){
				temp=filePath+"temp";
			}
			else{
				temp=filePath+"/temp";
			}
			delFile(temp);
			
		List<File> fileList = FileTools.listFile(new File(filePath));
		if(fileList==null||fileList.size()==0){
			logger.error("��ȡ��Դ�ļ��쳣 path:"+filePath);
			return null;
		}
		
		
		
		for(File file:fileList){
			if(file.getName().endsWith("lst")){
				logger.info("������Դ�ļ� path:"+filePath+"/"+file.getName()+" Ŀ��:"+temp+"/play001.lst");
				FileTools.copy(filePath+"/"+file.getName(), temp+"/play001.lst");
			}
			else{
				logger.info("������Դ�ļ� path:"+filePath+"/"+file.getName()+" Ŀ��:"+temp+"/"+file.getName());
				FileTools.copy(filePath+"/"+file.getName(), temp+"/"+file.getName());
			}
		}
		return temp;
		}
		catch(Exception e){
			logger.error("������Դ�ļ��쳣 path:"+filePath+" error:"+e);
		}
		return null;
	}

}

