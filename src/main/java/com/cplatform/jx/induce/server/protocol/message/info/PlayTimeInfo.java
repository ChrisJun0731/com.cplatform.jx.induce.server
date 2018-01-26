package com.cplatform.jx.induce.server.protocol.message.info;


public class PlayTimeInfo extends SwitchTimeInfo {


	/**播放列表号*/
	private int playlistnum;
	/**条目号  1-24 之间可选（一个播放列表文件包括 24 个条目） */
	private int playnum;
	
    public int getPlaylistnum() {
    	return playlistnum;
    }
	
    public void setPlaylistnum(int playlistnum) {
    	this.playlistnum = playlistnum;
    }
	
    public int getPlaynum() {
    	return playnum;
    }
	
    public void setPlaynum(int playnum) {
    	this.playnum = playnum;
    }

	
    @Override
   	public String toString(){		
       	StringBuffer buf = new StringBuffer(100);
       	buf.append("playlistnum=").append(playlistnum).append(", ");
   		buf.append("playnum=").append(playnum).append(", ");
   		buf.append("time=").append(super.toString());
       	return buf.toString();
   	}
	
	
}
