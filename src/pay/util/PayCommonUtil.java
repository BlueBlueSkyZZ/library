package pay.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;


public class PayCommonUtil {
//	private static Logger log = LoggerFactory.getLogger(PayCommonUtil.class);
	public static String CreateNoncestr(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < length; i++) {
			Random rd = new Random();
			res += chars.indexOf(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}
	/**
	 * @author ������
	 * @date 2014-12-5����2:29:34
	 * @Description��signǩ��
	 * @param characterEncoding �����ʽ
	 * @param parameters �������
	 * @return
	 */
	
	public static String createSign(String characterEncoding,SortedMap<String,String> parameters){
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v) 
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + ConfigUtil.API_KEY);
		System.out.println("����֮ǰ: " + sb.toString());
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
		return sign;
	}
	
	/**
	 * @author ������
	 * @date 2014-12-5����2:32:05
	 * @Description�����������ת��Ϊxml��ʽ��string
	 * @param parameters  �������
	 * @return
	 */
	public static String getRequestXml(SortedMap<String,String> parameters){
//		System.out.println("ִ��PayCommonUtil��getRequestXML����");
//		StringBuffer sb = new StringBuffer();
//		sb.append("<xml>");
//		Set<Entry<Object, Object>> es = parameters.entrySet();
//		Iterator<Entry<Object, Object>> it = es.iterator();
//		System.out.println("ת�����");
//		while(it.hasNext()) {
//			Map.Entry<Object,Object> entry = it.next();
//			String k = (String)entry.getKey();
//			String v = (String)entry.getValue();
//			System.out.println(k + "--" + v);
//			if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
//				sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
//			}else {
//				sb.append("<"+k+">"+v+"</"+k+">");
//			}
//		}
//		sb.append("</xml>");
//		return sb.toString();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set<String> key = parameters.keySet();
		for(String k:key){
			String v = parameters.get(k);
			System.out.println(k + "--" + v);
			if ("attach".equalsIgnoreCase(k) || 
						"body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			}else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
	/**
	 * @author ������
	 * @date 2014-12-3����10:17:43
	 * @Description�����ظ�΢�ŵĲ���
	 * @param return_code ���ر���
	 * @param return_msg  ������Ϣ
	 * @return
	 */
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}
}
