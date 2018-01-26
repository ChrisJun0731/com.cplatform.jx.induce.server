package com.cplatform.jx.induce.server.protocol.tools;


public class ToolKits {

	
	/** 
     * @���� ���������ֽڵ�ת�� 
     * @param ������ 
     * @return ��λ���ֽ����� 
     */  
    public static byte[] shortToByte(short number) {  
        int temp = number;  
        byte[] b = new byte[2];  
        for (int i = 0; i < b.length; i++) {  
            b[i] = new Integer(temp & 0xff).byteValue();// �����λ���������λ  
            temp = temp >> 8; // ������8λ  
        }  
        return b;  
    }  
  
    /** 
     * @���� �ֽڵ�ת��������� 
     * @param ��λ���ֽ����� 
     * @return ������ 
     */  
    public static short byteToShort(byte[] b) {  
        short s = 0;  
        short s0 = (short) (b[0] & 0xff);// ���λ  
        short s1 = (short) (b[1] & 0xff);  
        s1 <<= 8;  
        s = (short) (s0 | s1);  
        return s;  
    }  
  
    /** 
     * @�������� �������ֽ������ת�� 
     * @param ���� 
     * @return ��λ���ֽ����� 
     */  
    public static byte[] intToByte(int i) {  
        byte[] bt = new byte[4];  
        bt[0] = (byte) (0xff & i);  
        bt[1] = (byte) ((0xff00 & i) >> 8);  
        bt[2] = (byte) ((0xff0000 & i) >> 16);  
        bt[3] = (byte) ((0xff000000 & i) >> 24);  
        return bt;  
    }  
  
    /** 
     * @�������� �ֽ���������͵�ת�� 
     * @param �ֽ����� 
     * @return ���� 
     */  
    public static int bytesToInt(byte[] bytes) {  
        int num = bytes[0] & 0xFF;  
        num |= ((bytes[1] << 8) & 0xFF00);  
        num |= ((bytes[2] << 16) & 0xFF0000);  
        num |= ((bytes[3] << 24) & 0xFF000000);  
        return num;  
    }  
  
    /** 
     * @�������� �ֽ�����ͳ����͵�ת�� 
     * @param �ֽ����� 
     * @return ������ 
     */  
    public static byte[] longToByte(long number) {  
        long temp = number;  
        byte[] b = new byte[8];  
        for (int i = 0; i < b.length; i++) {  
            b[i] = new Long(temp & 0xff).byteValue();  
            // �����λ���������λ  
            temp = temp >> 8;  
            // ������8λ  
        }  
        return b;  
    }  
  
    /** 
     * @�������� �ֽ�����ͳ����͵�ת�� 
     * @param �ֽ����� 
     * @return ������ 
     */  
    public static long byteToLong(byte[] b) {  
        long s = 0;  
        long s0 = b[0] & 0xff;// ���λ  
        long s1 = b[1] & 0xff;  
        long s2 = b[2] & 0xff;  
        long s3 = b[3] & 0xff;  
        long s4 = b[4] & 0xff;// ���λ  
        long s5 = b[5] & 0xff;  
        long s6 = b[6] & 0xff;  
        long s7 = b[7] & 0xff; // s0����  
        s1 <<= 8;  
        s2 <<= 16;  
        s3 <<= 24;  
        s4 <<= 8 * 4;  
        s5 <<= 8 * 5;  
        s6 <<= 8 * 6;  
        s7 <<= 8 * 7;  
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;  
        return s;  
    }  	
	  /**
     * ת��  0xAA �� 0xCC �� 0xEE
     * 0xEE 0x0A 
     */ 
    public static byte[] covertChar (byte[] srcData){
    	int num = 0;

    	for(int i = 0; i < srcData.length; i++){
    		if(srcData[i] == (byte)0xAA ||
    		   srcData[i] == (byte)0xCC ||
    		   srcData[i] == (byte)0xEE){
    			num += 1;
    		}
    	}

    	byte[] desData = new byte[srcData.length + num];

    	int indexEnd = 0;
    	 int total = 0;
    	 int index =0;
    	for(; index < srcData.length; index++){
    		if(srcData[index] == (byte)0xAA ||
    		   srcData[index] == (byte)0xCC ||
    		   srcData[index] == (byte)0xEE){
    			//�ȿ���֮ǰ�ģ�
    			if(index >indexEnd){
    				System.arraycopy(srcData, indexEnd, desData, indexEnd+total, index-indexEnd);
    			}
    			desData[index+total] = (byte)0xEE;
    			desData[index+total+1] = (byte)(srcData[index]&0x0F);
    			
    			//��¼֮ǰ��ֵ
    			total +=1;
    			//������ǰֵ
    			
    			indexEnd = index+1;			
    		}
    	}
    	//����ʣ�µ�
    	if(index > indexEnd){
    		System.arraycopy(srcData, indexEnd, desData, indexEnd+total, index-indexEnd);
    	}
    	return desData;
    }

    //��ת��BUFFER
    public static byte[]uncovertChar(byte[] srcData){

    	byte[] desData = new byte[srcData.length];
    	int indexStart = 0;
    	for(int index = 0; index < srcData.length; index++){
    		if(srcData[index] == (byte)0xEE){
    			 if(srcData[index+1] == (byte)0x0A){
    				 desData[indexStart] = (byte)0xAA;   	    			
        			 index +=1;
    			 }else if(srcData[index+1] == (byte)0x0C){
    			     desData[indexStart] = (byte)0xCC;   	    			
        			 index +=1;
    			 }else if(srcData[index+1] == (byte)0x0E){
    			     desData[indexStart] = (byte)0xEE;    	    			
        			 index +=1;
    			 }
    		}
    		else{
    			desData[indexStart] = srcData[index];
    		}
    		indexStart++;
    	}
    	byte[] temp =new byte[indexStart];
    	System.arraycopy(desData, 0, temp, 0, indexStart);
    	return temp;
    }


    public static int buf2int (byte[] buf, int index){
    	return buf[index]+buf[index]*256;
    }

    public static byte[] int2buf(int num,byte[]buf,int index){
    	buf[index] = (byte)(num%256);
    	buf[index+1]=(byte)(num/256);
    	return buf;
    }
    
    
    private static final long PART1 = 0xff000000;  
    private static final long PART2 = 0xff0000;  
    private static final long PART3 = 0xff00;  
    private static final long PART4 = 0xff;  
  
    /** ��IP��ַ��������ֵת��ΪIPv4�ַ��� */  
    public static String ip2Str(long ip) { 
        String ipStr = String.valueOf(((ip & PART1) >> 24)< 0 ? ((ip & PART1) >> 24)+256 :((ip & PART1) >> 24));  
        ipStr += "." + ((ip & PART2) >> 16);  
        ipStr += "." + ((ip & PART3) >> 8);  
        ipStr += "." + (ip & PART4);  
        return ipStr;  
    }  
  
    /** ��IPv4�ַ���ת��Ϊ��Ӧ�ĳ��������� */  
    public static long ip2Long(String ip) {  
        String[] p4 = ip.split("\\.");  
        long ipInt = 0;  
        long part = Long.valueOf(p4[0]);  
        ipInt = ipInt | (part << 24);  
        part = Long.valueOf(p4[1]);  
        ipInt = ipInt | (part << 16);  
        part = Long.valueOf(p4[2]);  
        ipInt = ipInt | (part << 8);  
        part = Long.valueOf(p4[3]);  
        ipInt = ipInt | (part);  
        return ipInt;  
    } 
    
    public static byte[] ip2byte(String ip,byte[] bytes,int index){
    	String [] ip_split = ip.split("\\.");
    	bytes[index] = (byte) Integer.parseInt(ip_split[0]);
    	bytes[index+1] = (byte) Integer.parseInt(ip_split[1]);
    	bytes[index+2] = (byte) Integer.parseInt(ip_split[2]);
    	bytes[index+3] = (byte) Integer.parseInt(ip_split[3]);
    	return bytes;
    }
}
