package com.cplatform.jx.induce.server.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cplatform.jx.induce.DeviceInfo;
import com.cplatform.jx.induce.DevicePresetInfo;
import com.cplatform.jx.induce.PlayListTask;
import com.cplatform.jx.induce.server.protocol.dirty_words_filter.DirtyWordsBuffer;
/**
 * 
 * 数据操作类 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年3月3日 上午11:00:56
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
@Component
public class ActInduceDaoImpl implements ActInduceDao {

	private FastDateFormat dateFormat = FastDateFormat.getInstance("yyyyMMddHHmmss");
	
	@Autowired
	private JdbcTemplate jdbc;

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional(readOnly = true)
	public DirtyWordsBuffer loadDirtyWordsBuffer() throws Exception {

		StringBuilder sql = new StringBuilder();
		sql.append("select");
		sql.append("	coup.word");
		sql.append("	from t_sys_filter_word coup");
		 		
		return jdbc.query(sql.toString(), new Object[]{},
				new ResultSetExtractor<DirtyWordsBuffer>(){
			@Override
			public DirtyWordsBuffer extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				DirtyWordsBuffer list = null;
				try {
					list = new DirtyWordsBuffer(rs);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return list;
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public PlayListTask getPushPlayListTask(long taskId) throws Exception {
		Session session = sessionFactory.getCurrentSession();
		//
		PlayListTask info = (PlayListTask) session.get(PlayListTask.class, taskId);
		return info;
	}

	@Override
	@Transactional(readOnly = true)
	public PlayListTask getDevicePresetInfo(String deviceCode)
			throws Exception {	
		if(deviceCode.isEmpty()){
			return null;
		}	
		String nowTime = dateFormat.format(new Date());
		String timeplus = timeplus(1);
		String playTime = timeplus.substring(8, 12);
		String endTime = "2400";
		String updateTime = nowTime.substring(0, 8);
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DevicePresetInfo t where t.deviceCode=:deviceCode ";
		Query query = session.createQuery(hql);
		query.setParameter("deviceCode", deviceCode);
		//
		@SuppressWarnings("unchecked")
		List<DevicePresetInfo> list = query.list();
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			PlayListTask task = new PlayListTask();
			task.setPlaylistName("blackplay.lst");
			task.setPlaylistPath(list.get(0).getPlaylistPath());
			task.setPlayTime(playTime+"-"+endTime);
			task.setId(list.get(0).getId()*-1);
			task.setDeviceCode(deviceCode);
			return task;
		}	
	}
	@Override
	@Transactional(readOnly = true)
	public List<PlayListTask> getPushPlayListTaskId(String deviceCode) throws Exception {	
		if(deviceCode.isEmpty()){
			return null;
		}	
		String nowTime = dateFormat.format(new Date());
		String timeplus = timeplus(1);
		String playTime = timeplus.substring(8, 12);
		
		String updateTime = nowTime.substring(0, 8);
		Session session = sessionFactory.getCurrentSession();
		String hql = "from PlayListTask t where t.deviceCode=:deviceCode and t.playTime<:playTime"+
		" and t.status =:status and (t.isAuto =:notAuto or (t.isAuto =:isAuto and t.updateTime >:updateTime)) order by t.playTime desc ";
		Query query = session.createQuery(hql);
		query.setParameter("deviceCode", deviceCode);
		query.setParameter("playTime", playTime);		
		query.setParameter("status", "1");	
		query.setParameter("notAuto", 0);
		query.setParameter("isAuto", 1);
		query.setParameter("updateTime",updateTime);
		//
		@SuppressWarnings("unchecked")
		List<PlayListTask> list = query.list();
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list;
		}		
	}
	@Override
	@Transactional(readOnly = true)
	public Boolean isInvalid(int taskId) throws Exception {	
		String nowTime = dateFormat.format(new Date());
		String playTime = nowTime.substring(8, 12);
		Session session = sessionFactory.getCurrentSession();
		
		String hql = "from PlayListTask t where t.id=:id and SUBSTR(t.playTime,1,4)<=:playTime AND :playTime<=SUBSTR(t.playTime,6,9) and t.status =:status order by t.playTime desc ";
		Query query = session.createQuery(hql);
		query.setParameter("id", taskId);
		query.setParameter("playTime", playTime);		
		query.setParameter("status", "1");	
		
		@SuppressWarnings("unchecked")
		List<PlayListTask> list = query.list();
		if (list == null || list.isEmpty()) {
			return false;
		} else {
			return true;
		}		
	}

	@Override
	@Transactional
	public Boolean updatePushPlayListTask(long taskId, int status) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public DeviceInfo getDeviceInfo(String code) throws Exception {
		if(code.isEmpty()){
			return null;
		}			
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DeviceInfo t where t.deviceCode=:orderId and t.delFlag=:refundId";
		Query query = session.createQuery(hql);
		query.setParameter("orderId", code);
		query.setParameter("refundId", "1");			
		//
		@SuppressWarnings("unchecked")
		List<DeviceInfo> list = query.list();
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<DeviceInfo> getDeviceList() throws Exception {		
		Session session = sessionFactory.getCurrentSession();
		String hql = "from DeviceInfo t where t.delFlag=:refundId  ";
		Query query = session.createQuery(hql);
		query.setParameter("refundId", "1");//未删除的			
		//
		@SuppressWarnings("unchecked")
		List<DeviceInfo> list = query.list();
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list;
		}
	}
	private String timeplus(int min){  

		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");  
		   Calendar afterTime = Calendar.getInstance();   
		   afterTime.add(Calendar.MINUTE, min); //当前分钟+5  
		   Date afterDate = (Date) afterTime.getTime();   

//		   System.out.println("修改后的 时间"+afterDate);  
		 return  df.format(afterDate);
		  
		} 
	
	public static void main(String[] args) {
		
		 Calendar nowTime = Calendar.getInstance();   
		   Date nowDate = (Date) nowTime.getTime(); //得到当前时间  
		   
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");  
		   Calendar afterTime = Calendar.getInstance();   
		   afterTime.add(Calendar.MINUTE, -1); //当前分钟+5  
		   Date afterDate = (Date) afterTime.getTime();   
		   
		   System.out.println("今天时间"+nowDate);  
		   System.out.println("修改后的 时间"+afterDate);  
		   System.out.println("修改后的 时间"+df.format(afterDate).substring(8, 12));  
	}

	/* (non-Javadoc)
	 * @see com.cplatform.jx.induce.server.service.ActInduceDao#getDevicePresetInfo(java.lang.String)
	 */
	

}
