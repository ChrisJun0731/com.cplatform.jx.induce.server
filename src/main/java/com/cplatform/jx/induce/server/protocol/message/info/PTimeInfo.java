package com.cplatform.jx.induce.server.protocol.message.info;

/**
 * 
 * 时间段实体类. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月17日 上午10:14:43
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class PTimeInfo {

	private int day;
	
	private int month;
	
	private int year;
	
	private int hours;
	
	private int minutes;
	
	private int seconds;

    public int getDay() {
    	return day;
    }

	
    public void setDay(int day) {
    	this.day = day;
    }

	
    public int getMonth() {
    	return month;
    }

	
    public void setMonth(int month) {
    	this.month = month;
    }

	
    public int getYear() {
    	return year;
    }

	
    public void setYear(int year) {
    	this.year = year;
    }

	
    public int getHours() {
    	return hours;
    }

	
    public void setHours(int hours) {
    	this.hours = hours;
    }

	
    public int getMinutes() {
    	return minutes;
    }

	
    public void setMinutes(int minutes) {
    	this.minutes = minutes;
    }

	
    public int getSeconds() {
    	return seconds;
    }

	
    public void setSeconds(int seconds) {
    	this.seconds = seconds;
    }
	
    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(100);
       	buf.append("year=").append(year).append(", ");
    	buf.append("month=").append(month).append(", ");
    	buf.append("day=").append(day).append(", ");
    	buf.append("hours=").append(hours).append(", ");
    	buf.append("minutes=").append(minutes).append(", ");
   		buf.append("seconds=").append(seconds);
       	return buf.toString();
   	}
}
