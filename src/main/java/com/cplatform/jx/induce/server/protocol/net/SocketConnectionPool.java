package com.cplatform.jx.induce.server.protocol.net;

import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cplatform.jx.induce.BaseRequest;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.message.Header;
import com.cplatform.jx.induce.server.protocol.message.QueryVersionMessage;
import com.cplatform.jx.induce.server.protocol.message.QueryVersionRespMessage;

@Component
public class SocketConnectionPool {

	@Autowired
	private SocketClient socketClietnt;
	
    private String testTable = ""; // 测试连接是否可用的测试表名，默认没有测试表
    private int initialConnections = 1; // 连接池的初始大小
    private int incrementalConnections = 1; // 连接池自动增加的大小
    private int maxConnections = 2; // 连接池最大的大小
   
    private Map<String,Set<PooledConnection>> connections =null;
// 它中存放的对象为 PooledConnection 型

	/** 日志记录器 */
	private Logger logger = Logger.getLogger(getClass());
	
    public SocketConnectionPool() {

    }

   
    public int getInitialConnections() {
        return this.initialConnections;
    }

   
    public void setInitialConnections(int initialConnections) {
        this.initialConnections = initialConnections;
    }

   
    public int getIncrementalConnections() {
        return this.incrementalConnections;
    }

   
    public void setIncrementalConnections(int incrementalConnections) {
        this.incrementalConnections = incrementalConnections;
    }

   
    public int getMaxConnections() {
        return this.maxConnections;
    }

   
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

   
    public String getTestTable() {
        return this.testTable;
    }

   
    public void setTestTable(String testTable) {
        this.testTable = testTable;
    }

   
    public synchronized void createPool() throws Exception {
        // 确保连接池没有创建
        // 假如连接池己经创建了，保存连接的向量 connections 不会为空
        if (connections != null) {
            return; // 假如己经创建，则返回
        }
        // 创建保存连接的向量 , 初始时有 0 个元素
        connections = new ConcurrentHashMap<String, Set<PooledConnection>>();
        System.out.println(" socket连接池创建成功！ ");
    }

   
    private void createConnections(String host,int port,int numConnections) throws Exception {
        // 循环创建指定数目的数据库连接
        for (int x = 0; x < numConnections; x++) {
            // 是否连接池中的数据库连接的数量己经达到最大？最大值由类成员 maxConnections
            // 指出，假如 maxConnections 为 0 或负数，表示连接数量没有限制。
            // 假如连接数己经达到最大，即退出。
        	Set<PooledConnection> pools = connections.get(host);
            if (this.maxConnections > 0 &&( pools !=null&&
            		pools.size() >= this.maxConnections)) {
                break;
            }
            //add a new PooledConnection object to connections vector
            // 增加一个连接到连接池中（向量 connections 中）
            try {
            	SocketChannel socket = newConnection( host, port);
            	if(socket != null){
            	if(pools == null){
            		pools =  new HashSet<PooledConnection>();
            		connections.put(host, pools);
            	}
            	pools.add(new PooledConnection(socket));
            	}
            } catch (Exception e) {
            	logger.error(host+":"+port+" 创建socket连接失败！ " + e.getMessage());
//                throw new Exception();
            }
        }
    }

   
    private SocketChannel newConnection(String host,int port) throws Exception {
        // 创建一个socket连接
    	SocketChannel conn = socketClietnt.connect(host, port);
        return conn; // 返回创建的新的数据库连接
    }

   

    public  SocketChannel getConnection(String host,int port) throws Exception {
        // 确保连接池己被创建
        if (connections == null) {
            return null; // 连接池还没创建，则返回 null
        }
        SocketChannel conn = getFreeConnection( host, port); // 获得一个可用的socket连接
        // 假如目前没有可以使用的连接，即所有的连接都在使用中
        while (conn == null) {
            // 等一会再试
            wait(250);
            conn = getFreeConnection( host, port); // 重新再试，直到获得可用的连接，假如
            //getFreeConnection() 返回的为 null
            // 则表明创建一批连接后也不可获得可用连接
            break;
        }
        return conn; // 返回获得的可用的连接
    }

   
    private SocketChannel getFreeConnection(String host,int port) throws Exception {
        // 从连接池中获得一个可用的数据库连接
    	SocketChannel conn = findFreeConnection(host, port);
        if (conn == null) {
            // 假如目前连接池中没有可用的连接
            // 创建一些连接
            createConnections(host, port,incrementalConnections);
            // 重新从池中查找是否有可用连接
            conn = findFreeConnection(host, port);
            if (conn == null) {
                // 假如创建连接后仍获得不到可用的连接，则返回 null
                return null;
            }
        }
        return conn;
    }

   
    private SocketChannel findFreeConnection(String host,int port) throws Exception {
    	SocketChannel conn = null;
        // 获得连接池向量中所有的对象
        Set<PooledConnection> poolConn= connections.get(host);
        // 遍历所有的对象，看是否有可用的连接
        if(poolConn!=null&&poolConn.size()>0){
        	for(PooledConnection conns:poolConn){
        		  if (!conns.isBusy()) {
                      // 假如此对象不忙，则获得它的连接并把它设为忙
        			  poolConn.remove(conns);
                      conn = conns.getConnection();
                      conns.setBusy(true);
                      // 测试此连接是否可用                    
                      poolConn.add(conns);
                      break; // 己经找到一个可用的连接，退出
                  }
        	}
        }
        return conn; // 返回找到到的可用连接
    }

   
  /*  private boolean testConnection(Connection conn) {
        try {
            // 判定测试表是否存在
            if (testTable.equals("")) {
                // 假如测试表为空，试着使用此连接的 setAutoCommit() 方法
                // 来判定连接否可用（此方法只在部分数据库可用，假如不可用 ,
                // 抛出异常）。注重：使用测试表的方法更可靠
                conn.setAutoCommit(true);
            } else { // 有测试表的时候使用测试表测试
                //check if this connection is valid
                Statement stmt = conn.createStatement();
                stmt.execute("select count(*) from " + testTable);
            }
        } catch (SQLException e) {
            // 上面抛出异常，此连接己不可用，关闭它，并返回 false;
            closeConnection(conn);
            return false;
        }
        // 连接可用，返回 true
        return true;
    }*/

   
    public void returnConnection(String host,int port,SocketChannel conn) {
        // 确保连接池存在，假如连接没有创建（不存在），直接返回
        if (connections == null) {
            logger.error(" 连接池不存在，无法返回此连接到连接池中 !");
            return;
        }
        // 获得连接池向量中所有的对象
        Set<PooledConnection> poolConn= connections.get(host);
        // 遍历所有的对象，看是否有可用的连接
        if(poolConn!=null&&poolConn.size()>0){
        	for(PooledConnection conns:poolConn){
        		  if (conn == conns.getConnection()&&conns.isBusy()) {
                      // 假如此对象不忙，则获得它的连接并把它设为忙
        			  poolConn.remove(conns);
                      conns.setBusy(false);
                      // 测试此连接是否可用                    
                      poolConn.add(conns);
                      logger.info("成功释放链接到POOL:" + conns);
                      break; // 己经找到一个可用的连接，退出
                  }
        	}
        }
    }

   

    public  void refreshConnections() throws Exception {
        // 确保连接池己创新存在
        if (connections == null) {
            System.out.println(" 连接池不存在，无法刷新 !");
            return;
        }

		for (Map.Entry<String, Set<PooledConnection>> entry : connections.entrySet()) {

			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			String key = entry.getKey();
			Set<PooledConnection> pConn = entry.getValue();
			if(pConn==null||pConn.isEmpty()){
				connections.remove(key);
				logger.info("移除连接池中的设备链接:" + key);
				continue;
			}
			SocketChannel socket = getConnection(key, 5000);
			if(socket != null){
           try{
			// 测试此连接是否可用
			// 下发测试指令
			QueryVersionMessage info = new QueryVersionMessage(new Header());
			info.setAddress(BaseRequest.CLIENT_ADDRESS);
			
			QueryVersionRespMessage obj = (QueryVersionRespMessage) socketClietnt.sendCommond(info, socket,
					CommandID.QUERY_VERSION_RESP);

			if (obj != null && obj.getVersion() != null) {
				// 释放
				returnConnection(key, 5000, socket);
			} else {
				// 关闭此连接，用一个新的连接代替它。

				for (PooledConnection p : pConn) {
					if (p.getConnection() == socket) {
						pConn.remove(p);
						closeConnection(socket);
						logger.info("移除连接池中的设备链接:" + key);
					}
				}

			}
			}
			catch(Exception e){
				//链接异常 关闭链接池链接
				for (PooledConnection p : pConn) {
					if (p.getConnection() == socket) {
						pConn.remove(p);
						closeConnection(socket);
					}
				}
			}

		}
		}

    }

   
    public synchronized void closeConnectionPool() throws Exception {
        // 确保连接池存在，假如不存在，返回
        if (connections == null) {
            System.out.println(" 连接池不存在，无法关闭 !");
            return;
        }
        
        for (Set<PooledConnection> value : connections.values()) {  
        	for(PooledConnection pConn :value){
        		 if (pConn.isBusy()) {
                     wait(5000); // 等 5 秒
                 }
                 //5 秒后直接关闭它
                 closeConnection(pConn.getConnection());
        	}
        }
        
        connections.clear();
        // 置连接池为空
        connections = null;
    }

   
    private void closeConnection(SocketChannel conn) {
        try {
            socketClietnt.stopSocket(conn);
        } catch (Exception e) {
            System.out.println(" 关闭连接出错： " + e.getMessage());
        }
    }

   
    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
        }
    }

   

    class PooledConnection {
        SocketChannel connection = null; // socket连接
        boolean busy = false; // 此连接是否正在使用的标志，默认没有正在使用
        // 构造函数，根据一个 Connection 构告一个 PooledConnection 对象
        public PooledConnection(SocketChannel connection) {
            this.connection = connection;
        }

        // 返回此对象中的连接
        public SocketChannel getConnection() {
            return connection;
        }

        // 设置此对象的，连接
        public void setConnection(SocketChannel connection) {
            this.connection = connection;
        }

        // 获得对象连接是否忙
        public boolean isBusy() {
            return busy;
        }

        // 设置对象的连接正在忙
        public void setBusy(boolean busy) {
            this.busy = busy;
        }
    }

}