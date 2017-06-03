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

import org.apache.struts2.ServletActionContext;
import org.jdom.JDOMException;

import net.sf.json.JSONObject;

import pay.util.AddressUtil;
import pay.util.CommonUtil;
import pay.util.ConfigUtil;
import pay.util.PayCommonUtil;
import pay.util.XMLUtil;

import com.opensymphony.xwork2.ActionSupport;

public class PayAction extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	public void PostParam() throws Exception, IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
        request.setCharacterEncoding("UTF-8");
        
		SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
		parameters.put("appid", ConfigUtil.APPID);
		parameters.put("mch_id", ConfigUtil.MCH_ID);
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("body", "LEO����");
		parameters.put("out_trade_no", "201412051510");
		parameters.put("total_fee", "1");
		// ��ȡ�û���ַ
		parameters.put("spbill_create_ip", AddressUtil.getIpByRequest(request));  
		parameters.put("notify_url", ConfigUtil.NOTIFY_URL);  // �ĳ��Լ���֧��action
		parameters.put("trade_type", "JSAPI");
		parameters.put("openid", "o7W6yt9DUOBpjEYogs4by1hD_OQE");
		String sign = PayCommonUtil.createSign("UTF-8", parameters);
		parameters.put("sign", sign);
		// mapתString
		String requestXML = PayCommonUtil.getRequestXml(parameters);  
		// ���Ͳ�������ȡ���
		String result =CommonUtil.httpsRequest(ConfigUtil.UNIFIED_ORDER_URL, "POST", requestXML);
		
		
		// ����΢�ŷ��ص���Ϣ
		Map<String, String> map = XMLUtil.doXMLParse(result);
		// ��װ��Ϣ�������µ�map��
		SortedMap<Object,Object> params = new TreeMap<Object,Object>();
        params.put("appId", "wxde3504dfb219fc20");
        params.put("timeStamp", Long.toString(new Date().getTime()));
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
		
        // �����ǰ��
        HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();
        pw.write(json);
        pw.flush();
        pw.close();
		
	}
	
	// ֧���ɹ�ҳ��
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
        String result  = new String(outSteam.toByteArray(),"utf-8");//��ȡ΢�ŵ�������notify_url�ķ�����Ϣ
        Map<Object, Object> map = XMLUtil.doXMLParse(result);
        for(Object keyValue : map.keySet()){
            System.out.println(keyValue+"="+map.get(keyValue));
        }
        if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
            //TODO �����ݿ�Ĳ���
            response.getWriter().write(PayCommonUtil.setXML("SUCCESS", ""));   //����΢�ŷ����������յ���Ϣ�ˣ���Ҫ�ڵ��ûص�action��
            System.out.println("-------------"+PayCommonUtil.setXML("SUCCESS", ""));
        }
        
    }
	
}
