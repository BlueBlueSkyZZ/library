package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import menu.Button;
import menu.ClickButton;
import menu.Menu;
import menu.ViewButton;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import po.AccessToken;
import po.User;

public class WeixinUtil {
	// ���ݿ�����
	public static final String MYSQL_DN = "127.0.0.1:3306/library";
	public static final String MYSQL_NAME = "root";
	public static final String MYSQL_PASSWORD = "root";
	
	// ���Ժŵ�����
//	public static final String APPID = "wx6b71cb3f69dd9a86";
//	public static final String APPSECRET = "f02adc4026d13796f35169b778b4e9ef";
//	public static final String DN = "http://www.iotesta.cn";
	
	// ���������
	public static final String APPID = "wxde3504dfb219fc20";
	public static final String APPSECRET = "1824588d88f3251162b7ba687776b855";
	public static final String DN = "http://www.iotesta.com.cn";

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	private static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	
	private static final String SCOPE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	
	private static final String registerAction = DN + "/library/show_register.action";
	private static final String openLibraryAction = DN + "/library/show_main.action";
	private static final String showSingleCatAction = DN + "/library/show_singleCat.action";
	private static final String payAction = DN + "/library/show_pay.action";
	
	private static final String GET_USER_ID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	private static final String GET_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN";
	
	private static final String POST_CusService_SEND = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	/**
	 * get����
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity, "UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	/*
	 * post����
	 */
	public static JSONObject doPostStr(String url, String outStr) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			jsonObject = JSONObject.fromObject(result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	/*
	 * ��ȡaccess_token
	 */
	//�����ڲ������л�ȡtoken
	public static AccessToken getAccessToken() {
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if (jsonObject != null) {
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}

		return token;
	}
	
	/**
     * ����΢��JS�ӿڵ���ʱƱ��
     * 
     * @param access_token �ӿڷ���ƾ֤
     * @return
     */
	public static String getJsApiTicket(String access_token) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
        String requestUrl = url.replace("ACCESS_TOKEN", access_token);
        // ����GET�����ȡƾ֤
        JSONObject jsonObject = doGetStr(requestUrl);
        String jsTicket = null;
        if (jsonObject != null) {
            try {
                jsTicket = jsonObject.getString("ticket");
            } catch (JSONException e) {
                // ��ȡtokenʧ��
                System.out.println("��ȡtokenʧ�� \n" +  jsonObject.getInt("errcode") + jsonObject.getString("errmsg"));
            }
        }
        return jsTicket;
    }
	

	/*
	 * �ļ��ϴ�
	 */
	public static String upLoad(String filePath, String accessToken, String type)
			throws IOException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("�ļ�������");
		}

		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace(
				"TYPE", type);

		URL urlObj = new URL(url);
		// ����
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST");
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);

		// ��������ͷ��Ϣ
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		// ���ñ߽�
		String BOUNDARY = "---------" + System.currentTimeMillis();
		con.setRequestProperty("Content-type", "multipart/form-data;boundary="
				+ BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition:form-data;name=\"file\";filename=\""
				+ file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte head[] = sb.toString().getBytes("utf-8");

		// ��������
		OutputStream out = new DataOutputStream(con.getOutputStream());
		// �����ͷ
		out.write(head);

		// �ļ����Ĳ���
		// ���ļ���������ʽ���뵽url��
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		// ��β
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");

		out.write(foot);
		out.flush();
		out.close();

		// ��BufferedReader����������ȡURL����Ӧ
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (reader != null) {
				reader.close();
			}
		}
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		System.out.println(jsonObject);
		String mediaId = jsonObject.getString("media_id");
		
		return mediaId;
	}

	
	/**
	 * ��װ�˵�
	 * @return
	 */
	public static Menu initMenu(){
		Menu menu = new Menu();
		ClickButton button11 = new ClickButton();
		button11.setName("����");
		button11.setType("click");
		button11.setKey("11");
		
		ViewButton button12 = new ViewButton();
		button12.setName("��ѧ���鼮");
		button12.setType("view");
		button12.setUrl(showSingleCatAction);
		
		String url13 = SCOPE.replace("APPID", APPID).replace("REDIRECT_URI", openLibraryAction)
				.replace("SCOPE", "snsapi_userinfo").replace("STATE", "123");
		ViewButton button13 = new ViewButton();
		button13.setName("����ͼ���");
		button13.setType("view");
		button13.setUrl(url13);
		
		//**********************���wifi
		ViewButton button21 = new ViewButton();
		button21.setName("���WIFI");
		button21.setType("view");
		button21.setUrl(DN + "/library/show_wifi.action");
		
		String url22 = SCOPE.replace("APPID", APPID).replace("REDIRECT_URI", payAction)
				.replace("SCOPE", "snsapi_userinfo").replace("STATE", "123");
		ViewButton button22 = new ViewButton();
		button22.setName("֧������");
		button22.setType("view");
		button22.setUrl(url22);
		
		// ��ȡ��ϸ�û���Ϣ
		String url23 = SCOPE.replace("APPID", APPID).replace("REDIRECT_URI", registerAction)
				.replace("SCOPE", "snsapi_userinfo").replace("STATE", "123");
		ViewButton button23 = new ViewButton();
		button23.setName("��ϸ��Ϣע��");
		button23.setType("view");
		button23.setUrl(url23);
		
		ViewButton button24 = new ViewButton();
		button24.setName("JSSDK����ҳ��");
		button24.setType("view");
		button24.setUrl(DN + "/library/start_scan.action");
		
		
		ClickButton button31 = new ClickButton();
		button31.setName("ɨ���¼�");
		button31.setType("scancode_push");
		button31.setKey("31");
		
		ClickButton button32 = new ClickButton();
		button32.setName("����λ��");
		button32.setType("location_select");
		button32.setKey("32");
		
		ClickButton button33 = new ClickButton();
		button33.setName("һ������");
		button33.setType("click");
		button33.setKey("33");
		
		
		Button button1 = new Button();
		button1.setName("���˵�");
		button1.setSub_button(new Button[]{button11,button12,button13});
		
		Button button2 = new Button();
		button2.setName("url��ת");
		button2.setSub_button(new Button[]{button21,button22,button23,button24});
		
		Button button3 = new Button();
		button3.setName("���๦��");
		button3.setSub_button(new Button[]{button31,button32,button33});
		
		menu.setButton(new Button[]{button1,button2,button3});		
		return menu;
	}
	
	public static int createMenu(String token,String menu){
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		
		return result;
	}
	
	// ��ҳ��Ȩ��ȡopenID
	public static String getUserID(String code){
		String userID = "";
		/*
		 * �����ҳ��Ȩ��������Ϊsnsapi_base��
		 * �򱾲����л�ȡ����ҳ��Ȩaccess_token��ͬʱ��Ҳ��ȡ����openid��
		 * ��snsapi_baseʽ����ҳ��Ȩ���̼�����Ϊֹ��
		 */
		String url = GET_USER_ID.replace("APPID", APPID).replace("SECRET", APPSECRET).replace("CODE", code);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject != null){
			userID = jsonObject.getString("openid");
		}
		
		return userID;
	}
	
	
	public static int Cus_Service(String access_token, String outStr){
		int result = 0;
		
		String url = POST_CusService_SEND.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject = doPostStr(url, outStr);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
}
