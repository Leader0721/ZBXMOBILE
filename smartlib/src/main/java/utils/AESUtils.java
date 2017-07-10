package utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES+Base64加密
 * 
 * @author GISirFive
 * @since 2016-2-24 下午5:01:25
 */
public class AESUtils {

	private static final String AES = "AES";

	/** 秘钥 */
	private static String PASSWORD = "1234567890123456";

	/** 编码字符集 */
	private static final String CHARSET = "UTF-8";

	private AESUtils(String password) {
		AESUtils.PASSWORD = password;
	}

	/**
	 * 初始化DES加密
	 * 
	 * @param password
	 *            秘钥
	 * @author GISirFive
	 */
	public static void init(String password) {
		PASSWORD = password;
	}

	/**
	 * 加密
	 * 
	 * @param encryptStr
	 * @return
	 */
	public static byte[] encrypt(byte[] src, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
		cipher.init(Cipher.ENCRYPT_MODE, securekey);// 设置密钥和加密形式
		return cipher.doFinal(src);
	}

	/**
	 * 解密
	 * 
	 * @param decryptStr
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(AES);
		SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);// 设置加密Key
		cipher.init(Cipher.DECRYPT_MODE, securekey);// 设置密钥和解密形式
		return cipher.doFinal(src);
	}

	/**
	 * 二行制转十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String decode(String data) {
		try {
			return new String(decrypt(hex2byte(data.getBytes(CHARSET)), PASSWORD));
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public final static String encode(String data) {
		try {
			return byte2hex(encrypt(data.getBytes(CHARSET), PASSWORD));
		} catch (Exception e) {
		}
		return null;
	}
	

}