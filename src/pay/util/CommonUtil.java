package pay.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import po.AccessToken;

/**
 * ͨ�ù�����
 * @author ������
 * @date 2014-11-21����9:10:30
 */
public class CommonUtil {
//	private static Logger log = LoggerFactory.getLogger(CommonUtil.class);
	/**
	 * ����https����
	 * @param requestUrl �����ַ
	 * @param requestMethod ����ʽ��GET��POST��
	 * @param outputStr �ύ������
	 * @return ����΢�ŷ�������Ӧ����Ϣ
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {
			// ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// ������SSLContext�����еõ�SSLSocketFactory����
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// ��������ʽ��GET/POST��
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
			// ��outputStr��Ϊnullʱ�������д����
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// ע������ʽ
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// ����������ȡ��������
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// �ͷ���Դ
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
//			log.error("���ӳ�ʱ��{}", ce);
			System.out.println("���ӳ�ʱ" + ce);
		} catch (Exception e) {
//			log.error("https�����쳣��{}", e);
			System.out.println("https�����쳣" + e);
		}
		return null;
	}

	/**
	 * ��ȡ�ӿڷ���ƾ֤
	 * 
	 * @param appid ƾ֤
	 * @param appsecret ��Կ
	 * @return
	 */
	public static AccessToken getToken(String appid, String appsecret) {
		AccessToken token = null;
		String requestUrl = ConfigUtil.TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);
		// ����GET�����ȡƾ֤
		JSONObject jsonObject = JSONObject.fromObject(httpsRequest(requestUrl, "GET", null));

		if (null != jsonObject) {
			try {
				token = new AccessToken();
				token.setToken(jsonObject.getString("access_token"));
				token.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e) {
				token = null;
				// ��ȡtokenʧ��
//				log.error("��ȡtokenʧ�� errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
				System.out.println("��ȡtokenʧ�ܣ�errcode:" + jsonObject.getInt("errcode"));
			}
		}
		return token;
	}
	
	
	public static String urlEncodeUTF8(String source){
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}