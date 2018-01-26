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
	
    private String testTable = ""; // ���������Ƿ���õĲ��Ա�����Ĭ��û�в��Ա�
    private int initialConnections = 1; // ���ӳصĳ�ʼ��С
    private int incrementalConnections = 1; // ���ӳ��Զ����ӵĴ�С
    private int maxConnections = 2; // ���ӳ����Ĵ�С
   
    private Map<String,Set<PooledConnection>> connections =null;
// ���д�ŵĶ���Ϊ PooledConnection ��

	/** ��־��¼�� */
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
        // ȷ�����ӳ�û�д���
        // �������ӳؼ��������ˣ��������ӵ����� connections ����Ϊ��
        if (connections != null) {
            return; // ���缺���������򷵻�
        }
        // �����������ӵ����� , ��ʼʱ�� 0 ��Ԫ��
        connections = new ConcurrentHashMap<String, Set<PooledConnection>>();
        System.out.println(" socket���ӳش����ɹ��� ");
    }

   
    private void createConnections(String host,int port,int numConnections) throws Exception {
        // ѭ������ָ����Ŀ�����ݿ�����
        for (int x = 0; x < numConnections; x++) {
            // �Ƿ����ӳ��е����ݿ����ӵ����������ﵽ������ֵ�����Ա maxConnections
            // ָ�������� maxConnections Ϊ 0 ��������ʾ��������û�����ơ�
            // ���������������ﵽ��󣬼��˳���
        	Set<PooledConnection> pools = connections.get(host);
            if (this.maxConnections > 0 &&( pools !=null&&
            		pools.size() >= this.maxConnections)) {
                break;
            }
            //add a new PooledConnection object to connections vector
            // ����һ�����ӵ����ӳ��У����� connections �У�
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
            	logger.error(host+":"+port+" ����socket����ʧ�ܣ� " + e.getMessage());
//                throw new Exception();
            }
        }
    }

   
    private SocketChannel newConnection(String host,int port) throws Exception {
        // ����һ��socket����
    	SocketChannel conn = socketClietnt.connect(host, port);
        return conn; // ���ش������µ����ݿ�����
    }

   

    public  SocketChannel getConnection(String host,int port) throws Exception {
        // ȷ�����ӳؼ�������
        if (connections == null) {
            return null; // ���ӳػ�û�������򷵻� null
        }
        SocketChannel conn = getFreeConnection( host, port); // ���һ�����õ�socket����
        // ����Ŀǰû�п���ʹ�õ����ӣ������е����Ӷ���ʹ����
        while (conn == null) {
            // ��һ������
            wait(250);
            conn = getFreeConnection( host, port); // �������ԣ�ֱ����ÿ��õ����ӣ�����
            //getFreeConnection() ���ص�Ϊ null
            // ���������һ�����Ӻ�Ҳ���ɻ�ÿ�������
            break;
        }
        return conn; // ���ػ�õĿ��õ�����
    }

   
    private SocketChannel getFreeConnection(String host,int port) throws Exception {
        // �����ӳ��л��һ�����õ����ݿ�����
    	SocketChannel conn = findFreeConnection(host, port);
        if (conn == null) {
            // ����Ŀǰ���ӳ���û�п��õ�����
            // ����һЩ����
            createConnections(host, port,incrementalConnections);
            // ���´ӳ��в����Ƿ��п�������
            conn = findFreeConnection(host, port);
            if (conn == null) {
                // ���紴�����Ӻ��Ի�ò������õ����ӣ��򷵻� null
                return null;
            }
        }
        return conn;
    }

   
    private SocketChannel findFreeConnection(String host,int port) throws Exception {
    	SocketChannel conn = null;
        // ������ӳ����������еĶ���
        Set<PooledConnection> poolConn= connections.get(host);
        // �������еĶ��󣬿��Ƿ��п��õ�����
        if(poolConn!=null&&poolConn.size()>0){
        	for(PooledConnection conns:poolConn){
        		  if (!conns.isBusy()) {
                      // ����˶���æ�������������Ӳ�������Ϊæ
        			  poolConn.remove(conns);
                      conn = conns.getConnection();
                      conns.setBusy(true);
                      // ���Դ������Ƿ����                    
                      poolConn.add(conns);
                      break; // �����ҵ�һ�����õ����ӣ��˳�
                  }
        	}
        }
        return conn; // �����ҵ����Ŀ�������
    }

   
  /*  private boolean testConnection(Connection conn) {
        try {
            // �ж����Ա��Ƿ����
            if (testTable.equals("")) {
                // ������Ա�Ϊ�գ�����ʹ�ô����ӵ� setAutoCommit() ����
                // ���ж����ӷ���ã��˷���ֻ�ڲ������ݿ���ã����粻���� ,
                // �׳��쳣����ע�أ�ʹ�ò��Ա�ķ������ɿ�
                conn.setAutoCommit(true);
            } else { // �в��Ա��ʱ��ʹ�ò��Ա����
                //check if this connection is valid
                Statement stmt = conn.createStatement();
                stmt.execute("select count(*) from " + testTable);
            }
        } catch (SQLException e) {
            // �����׳��쳣�������Ӽ������ã��ر����������� false;
            closeConnection(conn);
            return false;
        }
        // ���ӿ��ã����� true
        return true;
    }*/

   
    public void returnConnection(String host,int port,SocketChannel conn) {
        // ȷ�����ӳش��ڣ���������û�д����������ڣ���ֱ�ӷ���
        if (connections == null) {
            logger.error(" ���ӳز����ڣ��޷����ش����ӵ����ӳ��� !");
            return;
        }
        // ������ӳ����������еĶ���
        Set<PooledConnection> poolConn= connections.get(host);
        // �������еĶ��󣬿��Ƿ��п��õ�����
        if(poolConn!=null&&poolConn.size()>0){
        	for(PooledConnection conns:poolConn){
        		  if (conn == conns.getConnection()&&conns.isBusy()) {
                      // ����˶���æ�������������Ӳ�������Ϊæ
        			  poolConn.remove(conns);
                      conns.setBusy(false);
                      // ���Դ������Ƿ����                    
                      poolConn.add(conns);
                      logger.info("�ɹ��ͷ����ӵ�POOL:" + conns);
                      break; // �����ҵ�һ�����õ����ӣ��˳�
                  }
        	}
        }
    }

   

    public  void refreshConnections() throws Exception {
        // ȷ�����ӳؼ����´���
        if (connections == null) {
            System.out.println(" ���ӳز����ڣ��޷�ˢ�� !");
            return;
        }

		for (Map.Entry<String, Set<PooledConnection>> entry : connections.entrySet()) {

			System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			String key = entry.getKey();
			Set<PooledConnection> pConn = entry.getValue();
			if(pConn==null||pConn.isEmpty()){
				connections.remove(key);
				logger.info("�Ƴ����ӳ��е��豸����:" + key);
				continue;
			}
			SocketChannel socket = getConnection(key, 5000);
			if(socket != null){
           try{
			// ���Դ������Ƿ����
			// �·�����ָ��
			QueryVersionMessage info = new QueryVersionMessage(new Header());
			info.setAddress(BaseRequest.CLIENT_ADDRESS);
			
			QueryVersionRespMessage obj = (QueryVersionRespMessage) socketClietnt.sendCommond(info, socket,
					CommandID.QUERY_VERSION_RESP);

			if (obj != null && obj.getVersion() != null) {
				// �ͷ�
				returnConnection(key, 5000, socket);
			} else {
				// �رմ����ӣ���һ���µ����Ӵ�������

				for (PooledConnection p : pConn) {
					if (p.getConnection() == socket) {
						pConn.remove(p);
						closeConnection(socket);
						logger.info("�Ƴ����ӳ��е��豸����:" + key);
					}
				}

			}
			}
			catch(Exception e){
				//�����쳣 �ر����ӳ�����
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
        // ȷ�����ӳش��ڣ����粻���ڣ�����
        if (connections == null) {
            System.out.println(" ���ӳز����ڣ��޷��ر� !");
            return;
        }
        
        for (Set<PooledConnection> value : connections.values()) {  
        	for(PooledConnection pConn :value){
        		 if (pConn.isBusy()) {
                     wait(5000); // �� 5 ��
                 }
                 //5 ���ֱ�ӹر���
                 closeConnection(pConn.getConnection());
        	}
        }
        
        connections.clear();
        // �����ӳ�Ϊ��
        connections = null;
    }

   
    private void closeConnection(SocketChannel conn) {
        try {
            socketClietnt.stopSocket(conn);
        } catch (Exception e) {
            System.out.println(" �ر����ӳ��� " + e.getMessage());
        }
    }

   
    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
        }
    }

   

    class PooledConnection {
        SocketChannel connection = null; // socket����
        boolean busy = false; // �������Ƿ�����ʹ�õı�־��Ĭ��û������ʹ��
        // ���캯��������һ�� Connection ����һ�� PooledConnection ����
        public PooledConnection(SocketChannel connection) {
            this.connection = connection;
        }

        // ���ش˶����е�����
        public SocketChannel getConnection() {
            return connection;
        }

        // ���ô˶���ģ�����
        public void setConnection(SocketChannel connection) {
            this.connection = connection;
        }

        // ��ö��������Ƿ�æ
        public boolean isBusy() {
            return busy;
        }

        // ���ö������������æ
        public void setBusy(boolean busy) {
            this.busy = busy;
        }
    }

}