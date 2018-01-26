package com.cplatform.jx.induce.server.protocol.tools;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DTool {

    static DTool instance;
    static Key key;
    static Cipher encryptCipher;
    static Cipher decryptCipher;
 
 
    public  DTool(String strKey) {
        key = setKey(strKey);
        try {
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
 
    }
 
 
//  根据参数生成KEY
    public Key setKey(String strKey) {
    try {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
        DESKeySpec keySpec = new DESKeySpec(strKey.getBytes()); 
        keyFactory.generateSecret(keySpec); 
        return keyFactory.generateSecret(keySpec);
    } catch (Exception e) {
        e.printStackTrace();
    }
 
    return null;
}
 

 
    //加密以byte[]明文输入,byte[]密文输出
public byte[] getEncCode(byte[] byteS) {
        byte[] byteFina = null;
        try {
            byteFina = encryptCipher.doFinal(byteS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }
 
 
    // 解密以byte[]密文输入,以byte[]明文输出
    public byte[] getDesCode(byte[] byteD) {
        byte[] byteFina = null;
        try {
            byteFina = decryptCipher.doFinal(byteD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteFina;
    }
    
}
