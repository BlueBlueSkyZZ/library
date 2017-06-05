package pay.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import pay.util.AddressUtil;
import pay.util.CommonUtil;
import pay.util.ConfigUtil;
import pay.util.PayCommonUtil;
import pay.util.XMLUtil;

import com.opensymphony.xwork2.ActionSupport;

public class PayAction extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	public void postParam() throws Exception, IOException{
		System.out.println("ִ��postParam����");
		HttpServletRequest request = ServletActionContext.getRequest();
        request.setCharacterEncoding("UTF-8");
        
		SortedMap<String,String> parameters = new TreeMap<String,String>();
		parameters.put("appid", ConfigUtil.APPID);
		parameters.put("mch_id", ConfigUtil.MCH_ID);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("body", "test");
		String str = "" + (System.currentTimeMillis() + (int)(Math.random()*100) + 100);
		parameters.put("out_trade_no", str);
		parameters.put("total_fee", "1");
		// ��ȡ�û���ַ
		parameters.put("spbill_create_ip", AddressUtil.getIpByRequest(request));  
		parameters.put("notify_url", ConfigUtil.NOTIFY_URL);  // �ĳ��Լ���֧��action
		parameters.put("trade_type", "JSAPI");
		// �˴���Ҫ��ȡ�û���openid *****************************************************************************************
		String openid = request.getParameter("openid");
		if(openid == null){
			openid = "otE_a1Ep3DV9r4kbFu7LDTTXhK6A";  // ��Ԫ�ܵ�id�����ڲⶨʱʹ��
		}
		parameters.put("openid", openid);
		// ***************************************************************************************************************
		String sign = PayCommonUtil.createSign("UTF-8", parameters);
		parameters.put("sign", sign);
		System.out.println("map " + parameters.toString());
		// mapתString
		String requestXML = PayCommonUtil.getRequestXml(parameters);  
		System.out.println("requestXML: " + requestXML);

		// ���Ͳ�������ȡ���
		String result = CommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", requestXML);
		System.out.println("result: " + result);
		
		// ����΢�ŷ��ص���Ϣ
		Map<String, String> map = XMLUtil.doXMLParse(result);
		// ��װ��Ϣ�������µ�map��
		SortedMap<String,String> params = new TreeMap<String,String>();
        params.put("appId", ConfigUtil.APPID);
        params.put("timeStamp", "" + Long.toString(new Date().getTime()));
        params.put("nonceStr", PayCommonUtil.CreateNoncestr());
        params.put("package", "prepay_id="+map.get("prepay_id"));
        params.put("signType", ConfigUtil.SIGN_TYPE);
        String paySign =  PayCommonUtil.createSign("UTF-8", params);
        params.put("packageValue", "prepay_id="+map.get("prepay_id"));    //������packageValue��Ԥ��package�ǹؼ�����js��ȡֵ����
        params.put("paySign", paySign);                             //paySign�����ɹ����Sign�����ɹ���һ��
        //����ɹ�����ת��ҳ��
        params.put("sendUrl", ConfigUtil.SUCCESS_URL);          

        String userAgent = request.getHeader("user-agent");
        char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger")+15);
        params.put("agent", new String(new char[]{agent}));//΢�Ű汾�ţ�����ǰ���ᵽ���ж��û��ֻ�΢�ŵİ汾�Ƿ���5.0���ϰ汾��
        String json = JSONObject.fromObject(params).toString();
        System.out.println("json: " + json);
		
        // �����ǰ��
        HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();
        pw.write(json);
        pw.flush();
        pw.close();
		
	}
	
	// �첽����΢��֧�����
	public void paySuccess() throws Exception{
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        System.out.println("~~~~~~~~~~~~~~~~����ɹ�~~~~~~~~~");
        outSteam.close();
        inStream.close();
        String result  = new String(outSteam.toByteArray(),"utf-8"); // ��ȡ΢�ŵ�������notify_url�ķ�����Ϣ
        Map<Object, Object> map = XMLUtil.doXMLParse(result);
        for(Object keyValue : map.keySet()){
            System.out.println(keyValue+"="+map.get(keyValue));
        }
        if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
            //TODO �����ݿ�Ĳ���
            response.getWriter().write(PayCommonUtil.setXML("SUCCESS", ""));   //����΢�ŷ����������յ���Ϣ�ˣ���Ҫ�ڵ��ûص�action��
            System.out.println("-------------"+PayCommonUtil.setXML("SUCCESS", ""));

            
            System.out.println("΢��֧��������:��" + map.get("transaction_id"));
            System.out.println("֧�����ʱ��: " + map.get("time_end"));
        }
        
        
    }
	
}
