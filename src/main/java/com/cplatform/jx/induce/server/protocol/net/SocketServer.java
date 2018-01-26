package com.cplatform.jx.induce.server.protocol.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import com.cplatform.jx.induce.server.protocol.buffer.ScreenStatesInfoList;
import com.cplatform.jx.induce.server.protocol.constants.CommandID;
import com.cplatform.jx.induce.server.protocol.constants.ErrorCode;
import com.cplatform.jx.induce.server.protocol.message.Header;
import com.cplatform.jx.induce.server.protocol.message.QueryScreenStateRespMessage;
import com.cplatform.jx.induce.server.protocol.message.info.ScreenStatesInfo;
import com.cplatform.jx.induce.server.protocol.tools.ToolKits;


/**
 * 
 * socket �����. <br>
 * ����ϸ˵��.
 * <p>
 * Copyright: Copyright (c) 2015��3��20�� ����5:18:55
 * <p>
 * Company: ��������ʮ�����ּ������޹�˾
 * <p>
 * @author huajun@c-platform.com
 * @version 1.0.0
 */
public class SocketServer extends Thread  implements CommandID, ErrorCode{

	private final static Logger logmsg = Logger.getLogger("MessageCenterLogger");
	private final static Logger log = Logger.getLogger("MyServer");
	
	@Resource(name = "sysConfig")
	private PropertiesConfiguration sysConfig;
	
	private boolean isActive; // ���߳��Ƿ���ŵı�־
	
	private void socketService() {
		Selector selector = null;
		ServerSocketChannel serverSocketChannel = null;
		
		try {
			// Selector for incoming time requests
			selector = Selector.open();

			// Create a new server socket and set to non blocking mode
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			
			// Bind the server socket to the local host and port
			serverSocketChannel.socket().setReuseAddress(true);
			serverSocketChannel.socket().bind(new InetSocketAddress(sysConfig.getInt("port")));
			
			// Register accepts on the server socket with the selector. This
			// step tells the selector that the socket wants to be put on the
			// ready list when accept operations occur, so allowing multiplexed
			// non-blocking I/O to take place.
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	
			// Here's where everything happens. The select method will
			// return when any operations registered above have occurred, the
			// thread has been interrupted, etc.
			while (selector.select() > 0) {
				// Someone is ready for I/O, get the ready keys
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
	
				// Walk through the ready keys collection and process date requests.
				while (it.hasNext()) {
					SelectionKey readyKey = it.next();
					it.remove();
					
					// The key indexes into the selector so you
					// can retrieve the socket that's ready for I/O
					execute((ServerSocketChannel) readyKey.channel());
				}
			}
		} catch (ClosedChannelException ex) {
			log.error(ex);
		} catch (IOException ex) {
			log.error(ex);
		} finally {
			try {
				selector.close();
			} catch(Exception ex) {}
			try {
				serverSocketChannel.close();
			} catch(Exception ex) {}
		}
	}
	
	public void run() {
		System.out.println("socket�����߳�����...");
		while (!Thread.interrupted()) {
			try {
				if (isActive) {
					Thread.sleep(100);
				} else {
					socketService();
				}
			} catch (Exception ex) {
				log.error(ex);
			}
		}

	}

	/**
	 * ������� ����
	 * @param serverSocketChannel
	 * @throws IOException
	 */
	private static void execute(ServerSocketChannel serverSocketChannel) throws IOException {
		SocketChannel socketChannel = null;
		try {
			socketChannel = serverSocketChannel.accept();
//			receive(socketChannel);	
			byte [] bytes =receiveData(socketChannel);
			DealBytes(bytes, socketChannel);
		} finally {
			try {
				socketChannel.close();
			} catch(Exception ex) {}
		}
	}

	/**
	 * ���׽������ж�ȡ����
	 */
	private static void DealMessage(byte[] bytes,SocketAddress address)
	{
		try
        {        
			 int length = bytes.length;
			 //����ͷ
			 Header header = new Header();
			 byte[] headbytes =new byte[3];
			 System.arraycopy(bytes, 0, headbytes, 0, 3);
			 header.byteToMessage(headbytes);
			 
			 //������ �ӽ��ܣ�		 
			 byte[] contentbytes = new byte[length -3];
			 System.arraycopy(bytes, 3, contentbytes, 0, length -3);
			 
//			 byte[] byt =null;
//	        try {
//		        byt = DES.decrypt(contentbytes,"novamima");
//	        }
//	        catch (Exception e1) {
//		        // TODO Auto-generated catch block
//		        e1.printStackTrace();
//	        }

				 
			 ByteBuffer buffer = ByteBuffer.allocate(length-3);
			 buffer.put(contentbytes);
			 buffer.flip();
			 
			 System.out.print("CommandId"+header.getCommand());
		
                    /* ����ͬ����Ϣ�� */
                    switch (header.getCommand())
                    {                
                        case QUERY_SCREEN_STATE_RESP: // ������Ϣ��
                        	QueryScreenStateRespMessage dm = new QueryScreenStateRespMessage(header);
                            int result = dm.byteToMessage(buffer);
                            
                            //���سɹ�����״̬
                            if(result == ErrorCode.MESSAGE_SUCCESS)
        					{
                            	handleStateMessage(dm,address);
        					}
                            //�Ƿ���Ҫ������Ӧ
//                            QueryScreenStateRespMessage drm = new DeliverRespMessage(header);
//                            drm.setResult(0);
//                            socket.write(drm.byteBufferedMessage(drm.getHeader().getTotalLength()));                            
                            break;
                        default:
                        	log.error("��������Ϣ�����ʶ��"+header.getCommand());
                        	break;
                    }   
                    return; // ������һ����Ϣ��ֱ���˳���������ѭ��������
            
        }
        catch (Exception e)
        {        
            log.error("������Ϣʱ����", e);
        }
	}
	

	/**
	 * �����ϱ��豸״̬��Ϣ
	 * 
	 * @param message, �ϱ��豸״̬��Ϣ��
	 */
	private static void handleStateMessage(QueryScreenStateRespMessage message,SocketAddress address)
	{
//			 �����ϱ��豸״̬��Ϣ
		    ScreenStatesInfo stateInfo = new ScreenStatesInfo();
		    stateInfo.setAddress(address.toString());
		    stateInfo.setControlLightType(message.getControlLightType());
		    stateInfo.setDate(message.getDate());
		    stateInfo.setDoorState(message.getDoorState());
		    stateInfo.setLightLevel(message.getLightLevel());
		    stateInfo.setLightValue(message.getLightValue());
		    stateInfo.setScreenPower(message.getScreenPower());
		    stateInfo.setSwitchState(message.getSwitchState());
		    stateInfo.setSymbol(message.getSymbol());
		    stateInfo.setTime(message.getTime());
		    stateInfo.setValue(message.getValue());
			
		    ScreenStatesInfoList.getInstance().add(stateInfo); // ��ӵ����д��洢
			
			logmsg.debug("�ɹ��յ���Ϣ:"+stateInfo.toString() +" result:0");
		
	}
	
	/**
	 * ���� ��Ӧ�ֽ�
	 * @param socketChannel
	 * @return
	 * @throws IOException
	 */
	 private static byte[] receiveData(SocketChannel socketChannel) throws IOException {  
		 byte[] bytes = null;  
         ByteArrayOutputStream baos = new ByteArrayOutputStream();            
         try {  
             ByteBuffer buffer = ByteBuffer.allocateDirect(1024);   
             int count = 0;  
             int i=0;
             while(true){
             if ((count = socketChannel.read(buffer)) >= 0) {  
//            	 System.out.println("count:"+count);
                 buffer.flip();  
                 bytes = new byte[count];  
                 buffer.get(bytes);  
                 baos.write(bytes);  
                 buffer.clear();  
             }   
             bytes = null;
             bytes = baos.toByteArray();               
             
             i++;
             if(i>300){
            	 break;
             }
             try {
	            Thread.sleep(5);
            }
            catch (InterruptedException e) {
	         
	            e.printStackTrace();
            }
           }
                         
             System.out.println("count:"+bytes.length);
             
         } finally {  
             try {  
            	//�ر� ����
//                 socketChannel.socket().shutdownInput();  
                 
                 baos.close();  
             } catch(Exception ex) {}  
         }  
         return bytes;  
     }  
   
	 /**
	  * ���� ��ϴ����
	  * @param bytes
	  */
	private static void DealBytes(byte[] bytes,SocketChannel socketChannel) {
		// ��������
		int isbegin = 0;
		int isend = 0;
		byte[] tempbytes = null;
		int tempcount = 0;
        if(bytes == null) return;
		for (int i = 0; i < bytes.length; i++) {
			// ��ʼ
			if (bytes[i] == (byte) MESSAGE_BEGIN && isbegin == 0) {
				// ������ʼ
				isbegin = i + 1;
			}
			// ����
			else if (bytes[i] == (byte) MESSAGE_END) {
				// ������ ��β
				isend = i;
			}
			// ������
			if (isbegin > 0 && isend > 0) {

				tempcount = isend - isbegin;
				tempbytes = new byte[tempcount];
				if (tempcount > 0) {
					System.arraycopy(bytes, isbegin, tempbytes, 0, isend - isbegin);

					//des ����
					
					// ��ת��
					byte[] unchar = ToolKits.uncovertChar(tempbytes);
					// ����ʵ�ʰ���
					DealMessage(unchar,socketChannel.socket().getRemoteSocketAddress());
				}
				isbegin = 0;
				isend = 0;
			} else {
				// ��Ч����	
			}
		}

	}
}
