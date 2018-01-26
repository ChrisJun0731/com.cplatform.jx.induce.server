package com.cplatform.jx.induce.server.protocol.message.info;


public class SwitchTimeInfo {

	/**¿ªÊ¼*/
	private PTimeInfo beginTime;
	/**½áÊø*/
	private PTimeInfo endTime;
	
	 public PTimeInfo getBeginTime() {
	    	return beginTime;
	    }

		
	    public void setBeginTime(PTimeInfo beginTime) {
	    	this.beginTime = beginTime;
	    }

		
	    public PTimeInfo getEndTime() {
	    	return endTime;
	    }

		
	    public void setEndTime(PTimeInfo endTime) {
	    	this.endTime = endTime;
	    }
	    
	    @Override
	   	public String toString(){		
	       	StringBuffer buf = new StringBuffer(100);
	       	buf.append("beginTime=").append(beginTime.toString()).append(", ");
	    	buf.append("endTime=").append(endTime.toString());

	       	return buf.toString();
	   	}
}
