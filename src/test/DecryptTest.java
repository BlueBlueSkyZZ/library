package test;

import util.EncryptUtil;

public class DecryptTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String content = "test";  
		String password = "12345678";  
		//����  
		System.out.println("����ǰ��" + content);  
		byte[] encryptResult = EncryptUtil.encrypt(content);  
		String encryptResultStr = EncryptUtil.parseByte2HexStr(encryptResult);  
		System.out.println("���ܺ�" + encryptResultStr);
		
		
		//����  
		byte[] decryptFrom = EncryptUtil.parseHexStr2Byte(encryptResultStr);  
		byte[] decryptResult = EncryptUtil.decrypt(decryptFrom);  
		System.out.println("���ܺ�" + new String(decryptResult));
		
		if(new String(decryptResult).equals(content)){
			System.out.println("���ܳɹ�");
		}
	}

}
