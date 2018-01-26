package com.cplatform.jx.induce.server.protocol.message;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

/**
 * 查询亮度参数
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2017年2月7日 上午11:52:30
 * <p>
 * Company: 北京宽连十方数字技术有限公司
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class QueryLightParamRespMessage extends AbstractMessage {

static Logger log = Logger.getLogger(QueryLightParamRespMessage.class);
	
	public QueryLightParamRespMessage(Header header)
    {
        super(header);
        getHeader().setTotalLength(31);
    }
    
    /**
     * 见接口
     */
    public ByteBuffer byteBufferedMessage(int length)
    {

         return null;
        
    }

    /**
     * 见接口
     */
    public int byteToMessage(ByteBuffer buffer)
    {
        if (buffer == null)
		{
			return MESSAGE_ERROR;
		}
		try
		{	    
			setLight1(buffer.get());// 
			setWidth1(buffer.get());// 
			buffer.get();
		       
			setLight2(buffer.get());// 
			setWidth2(buffer.get());// 
			buffer.get();
			
			setLight3(buffer.get());// 
			setWidth3(buffer.get());// 
			buffer.get();
			
			setLight4(buffer.get());// 
			setWidth4(buffer.get());// 
			buffer.get();
			
			setLight5(buffer.get());// 
			setWidth5(buffer.get());// 
			buffer.get();
			
			setLight6(buffer.get());// 
			setWidth6(buffer.get());// 
			buffer.get();
			
			setLight7(buffer.get());// 
			setWidth7(buffer.get());// 
			buffer.get();
			
			setLight8(buffer.get());// 
			setWidth8(buffer.get());// 
			buffer.get();		        
		}
		catch (Exception e)
		{
			log.error("解析数据异常",e);
			return MESSAGE_ERROR;
		}

		return MESSAGE_SUCCESS;
    }
  	
  	/**1 级环境亮度 */
  	private int light1;
  	/**1 级屏体度*/
  	private int width1;

  	/**1 级环境亮度 */
  	private int light2;
  	/**1 级屏体度*/
  	private int width2;
  	
  	/**1 级环境亮度 */
  	private int light3;
  	/**1 级屏体度*/
  	private int width3;
  	
  	/**1 级环境亮度 */
  	private int light4;
  	/**1 级屏体度*/
  	private int width4;
  	
  	/**1 级环境亮度 */
  	private int light5;
  	/**1 级屏体度*/
  	private int width5;
  	
  	/**1 级环境亮度 */
  	private int light6;
  	/**1 级屏体度*/
  	private int width6;
  	
  	/**1 级环境亮度 */
  	private int light7;
  	/**1 级屏体度*/
  	private int width7;
  	
  	/**1 级环境亮度 */
  	private int light8;
  	/**1 级屏体度*/
  	private int width8;


  	
      public int getLight1() {
      	return light1;
      }

  	
      public void setLight1(int light1) {
      	this.light1 = light1;
      }

  	
      public int getWidth1() {
      	return width1;
      }

  	
      public void setWidth1(int width1) {
      	this.width1 = width1;
      }

  	
      public int getLight2() {
      	return light2;
      }

  	
      public void setLight2(int light2) {
      	this.light2 = light2;
      }

  	
      public int getWidth2() {
      	return width2;
      }

  	
      public void setWidth2(int width2) {
      	this.width2 = width2;
      }

  	
      public int getLight3() {
      	return light3;
      }

  	
      public void setLight3(int light3) {
      	this.light3 = light3;
      }

  	
      public int getWidth3() {
      	return width3;
      }

  	
      public void setWidth3(int width3) {
      	this.width3 = width3;
      }

  	
      public int getLight4() {
      	return light4;
      }

  	
      public void setLight4(int light4) {
      	this.light4 = light4;
      }

  	
      public int getWidth4() {
      	return width4;
      }

  	
      public void setWidth4(int width4) {
      	this.width4 = width4;
      }

  	
      public int getLight5() {
      	return light5;
      }

  	
      public void setLight5(int light5) {
      	this.light5 = light5;
      }

  	
      public int getWidth5() {
      	return width5;
      }

  	
      public void setWidth5(int width5) {
      	this.width5 = width5;
      }

  	
      public int getLight6() {
      	return light6;
      }

  	
      public void setLight6(int light6) {
      	this.light6 = light6;
      }

  	
      public int getWidth6() {
      	return width6;
      }

  	
      public void setWidth6(int width6) {
      	this.width6 = width6;
      }

  	
      public int getLight7() {
      	return light7;
      }

  	
      public void setLight7(int light7) {
      	this.light7 = light7;
      }

  	
      public int getWidth7() {
      	return width7;
      }

  	
      public void setWidth7(int width7) {
      	this.width7 = width7;
      }

  	
      public int getLight8() {
      	return light8;
      }

  	
      public void setLight8(int light8) {
      	this.light8 = light8;
      }

  	
      public int getWidth8() {
      	return width8;
      }

  	
      public void setWidth8(int width8) {
      	this.width8 = width8;
      }

      @Override
     	public String toString(){
     		
         	StringBuffer buf = new StringBuffer(500);
         	buf.append("address=").append(getHeader().getAddress()).append(", ");
         	buf.append("light1=").append(light1).append(", ");
     		buf.append("width1=").append(width1).append(", ");
         	buf.append("light2=").append(light2).append(", ");
     		buf.append("width2=").append(width2).append(", ");
     		buf.append("light3=").append(light3).append(", ");
     		buf.append("width3=").append(width3).append(", ");
     		buf.append("light4=").append(light4).append(", ");
     		buf.append("width4=").append(width4).append(", ");
     		buf.append("light5=").append(light5).append(", ");
     		buf.append("width5=").append(width5).append(", ");
     		buf.append("light6=").append(light6).append(", ");
     		buf.append("width6=").append(width6).append(", ");
     		buf.append("light7=").append(light7).append(", ");
     		buf.append("width7=").append(width7).append(", ");
     		buf.append("light8=").append(light8).append(", ");
     		buf.append("width8=").append(width8);
         	return buf.toString();
     	}
}
