package pay.util;

public class ConfigUtil {
	/**
	 * ����������Ϣ
	 */
	 public final static String APPID = "wxde3504dfb219fc20";//����ŵ�Ӧ�ú�
	 public final static String APP_SECRECT = "1824588d88f3251162b7ba687776b855";//����ŵ�Ӧ������
	 public final static String TOKEN = "imooc";//����ŵ�����token
	 public final static String MCH_ID = "1480520822";//�̻���
	 public final static String API_KEY = "YesYouToldMeAboutBlueBlueSky1234";//API��Կ
	 public final static String SIGN_TYPE = "MD5";//ǩ�����ܷ�ʽ
	 
	 public final static String CERT_PATH = "C:/apiclient_cert.p12";//΢��֧��֤����·����ַ
	 
	// ΢��֧��ͳһ�ӿڵĻص�action
	 public final static String NOTIFY_URL = "http://www.iotesta.com.cn/library/post_param.action";
	// ֧���ɹ���������ҳ
	 public final static String SUCCESS_URL = "http://www.iotesta.com.cn/library/show_main.action";
	 // oauth2��Ȩʱ�ص�action
	 public final static String REDIRECT_URI = "http://14.117.25.80:8016/GoMyTrip/a.jsp?port=8016";
	 
	 
	/**
	 * ΢�Ż����ӿڵ�ַ
	 */
	 //��ȡtoken�ӿ�(GET)
	 public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	 //oauth2��Ȩ�ӿ�(GET)
	 public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	 //ˢ��access_token�ӿڣ�GET��
	 public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	// �˵������ӿڣ�POST��
	 public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// �˵���ѯ��GET��
	 public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// �˵�ɾ����GET��
	public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	/**
	 * ΢��֧���ӿڵ�ַ
	 */
	//΢��֧��ͳһ�ӿ�(POST)
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//΢���˿�ӿ�(POST)
	public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	//������ѯ�ӿ�(POST)
	public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	//�رն����ӿ�(POST)
	public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	//�˿��ѯ�ӿ�(POST)
	public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	//���˵��ӿ�(POST)
	public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	//������ת���ӿ�(POST)
	public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
	//�ӿڵ����ϱ��ӿ�(POST)
	public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
}

