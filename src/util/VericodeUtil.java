package util;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class VericodeUtil {

	/**
	 * ������֤��
	 * @param tel �����û��ֻ���
	 * @return ���ɵ���λ�������String��
	 */
	public static String sendSMS(String tel) {
		// ����ģ��
		String url = "http://gw.api.taobao.com/router/rest";
		String appkey = "24487251";
		String secret = "7b411904fa227f20bf5243e255205599";
		String vericode = String.valueOf(generateRandomArray(6));//������֤��
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("");
		req.setSmsType("normal");
		req.setSmsFreeSignName("����������ͼ���");
		req.setSmsParamString("{'number':'" + vericode + "'}");
		req.setRecNum(tel);
		req.setSmsTemplateCode("SMS_71805105");
		AlibabaAliqinFcSmsNumSendResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(rsp.getBody());
		System.out.println(vericode);
		return vericode;
	}
	
	/** 
     * ������� numλ�����ַ����� 
     * @param num 
     * @return 
     */  
    public static char[] generateRandomArray(int num) {  
        String chars = "0123456789";  
        char[] rands = new char[num];  
        for (int i = 0; i < num; i++) {  
            int rand = (int) (Math.random() * 10);  
            rands[i] = chars.charAt(rand);  
        }  
        return rands;  
    }  
  
    public static void main(String[] args) {  
  
        // �������6λ����  
       // System.out.println(String.valueOf(generateRandomArray(6)));  
        sendSMS("15995085680");
    }  
	
}
