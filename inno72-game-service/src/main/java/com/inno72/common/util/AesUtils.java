package com.inno72.common.util;

import com.inno72.plugin.http.HttpClient;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;


/**
 * @ClassName: AESUtil
 * @Description: 对cookie进行加密解密
 * @date 2015-9-23 上午9:07:18
 */
public class AesUtils {

    public static final String logalrithm = "AES/CBC/PKCS5Padding";
    public static final String bm = "utf-8";
    private static byte[] keyValue = "02069578fbc54618".getBytes();
    private static byte[] iv = "ad62dfa9dede426e".getBytes();

    private static Key keySpec;
    private static IvParameterSpec ivSpec;

    static {
        keySpec = new SecretKeySpec(keyValue, "AES");
        ivSpec = new IvParameterSpec(iv);
    }

    /**
     * @param msg 加密的数据
     * @return
     * @throws
     * @Title: encrypt
     * @Description: 加密，使用指定数据源生成密钥，使用用户数据作为算法参数进行AES加密
     * @date 2015-9-23 上午9:09:20
     */
    public static String encrypt(String msg) {
        byte[] encryptedData = null;
        try {
            Cipher ecipher = Cipher.getInstance(logalrithm);
            ecipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            encryptedData = ecipher.doFinal(msg.getBytes(bm));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return asHex(encryptedData);
    }


    /**
     * @param value
     * @return
     * @throws
     * @Title: decrypt
     * @Description: 解密，对生成的16进制的字符串进行解密
     * @date 2015-9-23 上午9:10:01
     */
    public static String decrypt(String value) {
        try {
            Cipher ecipher = Cipher.getInstance(logalrithm);
            ecipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return new String(ecipher.doFinal(asBin(value)));
        } catch (BadPaddingException e) {
            System.out.println("解密错误：" + value);
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("解密错误：" + value);
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * @param buf
     * @return
     * @throws
     * @Title: asHex
     * @Description: 将字节数组转换成16进制字符串
     * @date 2015-9-23 上午9:10:25
     */
    private static String asHex(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10) {
                strbuf.append("0");
            }
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }


    /**
     * @param src
     * @return
     * @throws
     * @Title: asBin
     * @Description: 将16进制字符串转换成字节数组
     * @date 2015-9-23 上午9:10:52
     */
    private static byte[] asBin(String src) {
        if (src.length() < 1) {
            return null;
        }
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }

    public static void main(String[] args) {
       /* String userid = "897807300@qq.com";
        String token = "8aa8690f65f080aee595d8781e7044a7eacda7a86520786db0838136554920b6";
        System.out.println(encrypt(token));
        System.out.println(decrypt(encrypt(token)));*/
        String result = HttpClient.post("http://localhost:8881//sendMsgToClient/sendEvent/sendEvent", "");
        System.out.println("调用发送结果是" + result);
    }
}