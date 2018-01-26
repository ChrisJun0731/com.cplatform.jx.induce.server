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
 * 推送播放内容服务. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月16日 上午9:47:30
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Component
public class PushPlayListTaskService {

	/**黑屏任务id*/
	private final int BLACKPLAY_TASKID = -200;
	@Autowired
	private PushDeviceContentService pushContentService;

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	@Autowired
	private ActInduceDao dao;

	/** 日志记录器 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** sysConfig */
	@Resource(name = "sysConfig")
	private PropertiesConfiguration sysConfig;


	private Map<String,Set<PlayTaskLogInfo>> taskRecord = new ConcurrentHashMap<String,Set<PlayTaskLogInfo>>();
	
	/**当前屏幕播放的任务ID*/
	private Map<String,String> nowPlayTaskId = new HashMap<String, String>();
	
	/**
	 * 获取当前播放任务ID
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
	 * 设置当前播放任务ID
	 * @param deviceCode
	 * @param taskId
	 */
	private void setnowPlayTaskId(String deviceCode,String taskId){
		nowPlayTaskId.put(deviceCode, taskId);
	}
	/**
	 * @return 任务是否已经执行
	 */
	private PlayTaskLogInfo getTaskExe(String deviceCode,int taskId) {
		Set<PlayTaskLogInfo> info = taskRecord.get(deviceCode);
		String nowTime = dateFormat.format(new Date()).substring(0, 8);
		
		if(info != null){
			for(PlayTaskLogInfo log:info){
				//黑屏 可以多次推送，任务多次推送问题？
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
	 * 保存已处理记录
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
	 * 清除所有记录
	 */
	public void removeAll(){
		taskRecord.clear();
	}

	/**
	 * 判断 任务是否在有效期内
	 * 
	 * @param playListTask
	 *            任务信息
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
			logger.error("计算任务有效期异常", ex);
			return false;
		}
	}

	/**
	 * 获取节目列表中 最新一个有效的节目
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
	 * 获取黑屏播放节目
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
	 * 处理需要推送的任务<br>
	 * 
	 * @param actOrderId
	 *            订单ID
	 * @throws Exception
	 *             异常
	 */
	public void process(DeviceInfo deviceInfo) throws Exception {
		logger.info("开始处理待推送任务,device={}" , deviceInfo.toString());
		//获取当前播放任务ID
		String currentTaskId=getnowPlayTaskId(deviceInfo.getDeviceCode());
		//自动播放列表
		
		//手动播放列表
		List<PlayListTask> info = dao.getPushPlayListTaskId(deviceInfo.getDeviceCode());
		//预置内容
		PlayListTask devicePresetInfo=dao.getDevicePresetInfo(deviceInfo.getDeviceCode());
		PlayListTask task = null;
		if(info == null||info.isEmpty()){
			logger.info("符合条件处理的播放列表任务为空");
			if(StringUtils.isNotBlank(currentTaskId)){
				logger.info("当前任务currentTaskId不为空");
				if(currentTaskId.contains("-")){//当前任务为预置内容或黑屏
					logger.info("当前任务为预置内容或黑屏");
					if(StringUtils.equals(currentTaskId, BLACKPLAY_TASKID+"")&&devicePresetInfo!=null){//当前任务为黑屏并且预置内容不为空
						//增加预置内容
						logger.info("增加预置内容播放列表1, deviceCode={}", deviceInfo.getDeviceCode());
						task=devicePresetInfo;
						info = new ArrayList<PlayListTask>();
						info.add(task);
					}else{
						//当前任务为预置内容
						logger.info("当前没有需要推送任务, deviceCode={}", deviceInfo.getDeviceCode());
						return;
					}
					
				}else{
					logger.info("当前任务不是预置内容也不是黑屏");
					Boolean isInvalid=dao.isInvalid(Integer.parseInt(currentTaskId));
					if(!isInvalid){//失效
						logger.info("当前任务currentTaskId"+currentTaskId+"已经失效");
						if(devicePresetInfo!=null){
							//增加预置内容
							logger.info("增加预置内容播放列表1, deviceCode={}", deviceInfo.getDeviceCode());
							task=devicePresetInfo;
							info = new ArrayList<PlayListTask>();
							info.add(task);
							
						}else{
							
							//赋于黑屏节目
							logger.info("增加黑屏播放列表1, deviceCode={}", deviceInfo.getDeviceCode());
							task = getBlankPlayList(deviceInfo);
							info = new ArrayList<PlayListTask>();
							info.add(task);
						}
					}else{
						logger.info("当前没有需要推送任务1, deviceCode={}", deviceInfo.getDeviceCode());
						return;
					}
				}
				
			}else{
				logger.info("当前任务currentTaskId为空");
				if(devicePresetInfo!=null){
					//增加预置内容
					logger.info("增加预置内容播放列表1, deviceCode={}", deviceInfo.getDeviceCode());
					task=devicePresetInfo;
					info = new ArrayList<PlayListTask>();
					info.add(task);
					
				}else{
					
					//赋于黑屏节目
					logger.info("增加黑屏播放列表1, deviceCode={}", deviceInfo.getDeviceCode());
					task = getBlankPlayList(deviceInfo);
					info = new ArrayList<PlayListTask>();
					info.add(task);
				}
			}
			
//			if(!currentTaskId.contains("-")){
//					
//					
//					if(devicePresetInfo!=null){
//						//增加预置内容
//						logger.info("增加预置内容播放列表1, deviceCode={}", deviceInfo.getDeviceCode());
//						task=devicePresetInfo;
//						info = new ArrayList<PlayListTask>();
//						info.add(task);
//						
//					}else{
//						
//						//赋于黑屏节目
//						logger.info("增加黑屏播放列表1, deviceCode={}", deviceInfo.getDeviceCode());
//						task = getBlankPlayList(deviceInfo);
//						info = new ArrayList<PlayListTask>();
//						info.add(task);
//					}
//					
//			}
//			else{
//			logger.info("当前没有需要推送任务, deviceCode={}", deviceInfo.getDeviceCode());
//			return;
//			}
		}else{
			logger.info("符合条件处理的播放列表任务不为空，size为"+info.size());
		}

		//比对当前时间，当前任务 
		 task = info.get(0);
		
		logger.info("推送任务,playTask={}",task.toString());
		PlayTaskLogInfo logs = getTaskExe(deviceInfo.getDeviceCode(),task.getId());
		if(logs != null){
			//判断 当前是否为 播放任务ID 是  判断是否已经失效，如果失效寻找最近一个没有失效的节目 推送到平上，如果没有 直接推 黑屏
//			if(getnowPlayTaskId(deviceInfo.getDeviceCode()).compareTo(""+task.getId())==0){
//				String nowTime = dateFormat.format(new Date());
//				String nowSub = nowTime.substring(8, 12);
//				String [] playtiem = task.getPlayTime().split("-");
//				if(playtiem[1].compareTo(nowSub)<=0){
//					//推送 黑屏，查询最近一条 没有到失效时间的记录，进行推送 如果都没有直接推送黑屏
//					PlayListTask temp = getLastEffective(info);
//					if(temp == null){
//						//赋于黑屏节目
//						task = getBlankPlayList(deviceInfo);
//						logger.info("增加黑屏播放列表2, deviceCode={}", deviceInfo.getDeviceCode());
//					}
//					else{
//						task = temp;
//						logger.info("获取到最近一个未失效任务2,playTask={}",task.toString());
//					}
//				}
//				else{
//					logger.info("当前屏幕播放的任务,playTask={},logTask={}",task.toString(),logs.toString());
//					return;
//				}
//				
//			}
//			else{
				//获取当前的播放列表
//				String taskId = getnowPlayTaskId(deviceInfo.getDeviceCode());
//				if(currentTaskId.equals(BLACKPLAY_TASKID)){
				if(currentTaskId.contains("-")){
					PlayListTask temp = getLastEffective(info);
					if(temp == null){
						
						if(devicePresetInfo!=null){
							//增加预置内容
							task=devicePresetInfo;
							logger.info("增加预置内容播放列表,playTask={},logTask={}", task.toString(),logs.toString());
						}else{
							
							//赋于黑屏节目
							task = getBlankPlayList(deviceInfo);
							logger.info("当前屏幕播放的任务,playTask={},logTask={}",task.toString(),logs.toString());
//							return;
						}
					}
					else{
						task = temp;
						logger.info("获取到最近一个未失效任务3,playTask={}",task.toString());
					}
				}
				else{
					
					String nowTime = dateFormat.format(new Date());
					String nowSub = nowTime.substring(8, 12);
					String [] playtiem = task.getPlayTime().split("-");
					if(playtiem[1].compareTo(nowSub)<=0){
						//推送 黑屏，查询最近一条 没有到失效时间的记录，进行推送 如果都没有直接推送黑屏
						PlayListTask temp = getLastEffective(info);
						if(temp == null){
//							//赋于黑屏节目
//							task = getBlankPlayList(deviceInfo);
//							logger.info("增加黑屏播放列表3, deviceCode={}", deviceInfo.getDeviceCode());
							
							if(devicePresetInfo!=null){
								//增加预置内容
								task=devicePresetInfo;
								logger.info("增加预置播放列表3, deviceCode={}", deviceInfo.getDeviceCode());
							}else{
								
								//赋于黑屏节目
								task = getBlankPlayList(deviceInfo);
								logger.info("增加黑屏播放列表3, deviceCode={}", deviceInfo.getDeviceCode());
//								return;
							}
						}
						else{
							task = temp;
							logger.info("获取到最近一个未失效任务4,playTask={}",task.toString());
						}
					}
					else{
						logger.info("当前屏幕播放的任务1,playTask={},logTask={}",task.toString(),logs.toString());
						return;
					}
				}
				
			}
//		}
		
		if(!getTaskTimeout(task)){
			//判断 当前是否为 播放任务ID 是  判断是否已经失效，如果失效寻找最近一个没有失效的节目 推送到平上，如果没有 直接推 黑屏
//			if(currentTaskId.compareTo(""+BLACKPLAY_TASKID)!=0){
				if(!currentTaskId.contains("-")){
				//推送 黑屏，查询最近一条 没有到失效时间的记录，进行推送 如果都没有直接推送黑屏
				PlayListTask temp = getLastEffective(info);
				if(temp == null){
				
				
					if(devicePresetInfo!=null){
						//增加预置内容
						task=devicePresetInfo;
						logger.info("增加预置播放列表4, deviceCode={}", deviceInfo.getDeviceCode());
					}else{
						
						//赋于黑屏节目
						task = getBlankPlayList(deviceInfo);
						logger.info("增加黑屏播放列表4, deviceCode={}", deviceInfo.getDeviceCode());
//						return;
					}	
					
					
//				//赋于黑屏节目
//				task = getBlankPlayList(deviceInfo);
//				logger.info("增加黑屏播放列表4, deviceCode={}", deviceInfo.getDeviceCode());
			  }	
				else{
					task = temp;
					logger.info("获取到最近一个未失效任务4,playTask={}",task.toString());
				}
			}
			else{
		      logger.info("任务不在有效期内,无需再次发送,playTask={}",task.toString());
		      return;
			}
		}
		
		//过滤 当前已经播放内容
		if(currentTaskId.compareTo(""+task.getId())==0){
			logger.info("目前屏幕播放的任务,playTask={},logTask={}",task.toString(),logs);
			return;
		}
		
		//重新获取设备信息，防止设备已经失效
		DeviceInfo device = dao.getDeviceInfo(deviceInfo.getDeviceCode());
		
		if(device == null){
			logger.info("获取设备信息失败,无法推送当前任务。 deviceCode={},playTaskId={}", deviceInfo.getDeviceCode(),task.getId());
			return;
		}

		//处理PLAY001.LST 
		String temp = dealFile(task.getPlaylistPath());
		if(temp == null) {
			logger.info("处理资源文件失败,无法推送当前任务。 deviceCode={},playTaskId={}", deviceInfo.getDeviceCode(),task.getId());
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
		
		//更新 TASK 状态
		if(resp.getStatus() == BaseResponse.STATUS_OK){		
			//记录当前已下发任务
			PlayTaskLogInfo log = new PlayTaskLogInfo();
			log.setDeviceCode(deviceInfo.getDeviceCode());
			log.setTaskId(task.getId());
			log.setPushTime(time.substring(0, 8));	
			putTaskLogToMap(log);
			
			setnowPlayTaskId(deviceInfo.getDeviceCode(),""+task.getId());
		}
		
		delFile(temp);
		logger.info("发布显示任务,Task={},Resp={}",request.toString(),resp.toString());
				
	}
	
	private void delFile(String filePath){
		File delFile = new File(filePath);
		FileTools.rmDir(delFile);
	}
	private String  dealFile(String filePath){
		//创建临时文件夹
		//拷贝文件
		//lst改名成 play001.lst
		try{
			//先删除 temp
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
			logger.error("获取资源文件异常 path:"+filePath);
			return null;
		}
		
		
		
		for(File file:fileList){
			if(file.getName().endsWith("lst")){
				logger.info("拷贝资源文件 path:"+filePath+"/"+file.getName()+" 目的:"+temp+"/play001.lst");
				FileTools.copy(filePath+"/"+file.getName(), temp+"/play001.lst");
			}
			else{
				logger.info("拷贝资源文件 path:"+filePath+"/"+file.getName()+" 目的:"+temp+"/"+file.getName());
				FileTools.copy(filePath+"/"+file.getName(), temp+"/"+file.getName());
			}
		}
		return temp;
		}
		catch(Exception e){
			logger.error("拷贝资源文件异常 path:"+filePath+" error:"+e);
		}
		return null;
	}

}

