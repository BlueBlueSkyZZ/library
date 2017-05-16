package test;

import net.sf.json.JSONObject;
import po.AccessToken;
import util.WeixinUtil;

public class WeixinTest {
	//������
	public static void main(String[] args) {
		try{
				AccessToken token = WeixinUtil.getAccessToken();  //��ȡaccess_token
				System.out.println("Ʊ��  " + token.getToken());
				System.out.println("��Чʱ��  " + token.getExpiresIn());
		
				
//				String path = "E:/JAVA������/3A.jpg";
//				String mediaId = WeixinUtil.upLoad(path, token.getToken(), "image");
//				System.out.println(mediaId);
				
				
				String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
				int result = WeixinUtil.createMenu(token.getToken(), menu);
				if(result == 0){
					System.out.println("�����˵��ɹ�");
				}else{
					System.out.println("������: " + result);
				}
				
				
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
