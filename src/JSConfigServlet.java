import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.CheckUtil;
import util.PastUtil;


public class JSConfigServlet extends HttpServlet{
	private static final String APPID = "wxde3504dfb219fc20";
	private static final String APPSECRET = "1824588d88f3251162b7ba687776b855";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String,String> map = PastUtil.getParam(APPID, APPSECRET, req);		
		String noncestr = map.get("nonceStr");
		String jsapi_ticket = map.get("jsapi_ticket");
		String timestamp = map.get("timestamp");
		String url = map.get("url");
		System.out.println(map.toString());
		// ����ǩ��
		String signature = CheckUtil.generateSignature(noncestr, jsapi_ticket, timestamp, url);
		
		// ���ò���
		req.setAttribute("appId", APPID);
		req.setAttribute("timeStamp", timestamp);
		req.setAttribute("nonceStr", noncestr);
		req.setAttribute("signature", signature);
		req.setAttribute("url", url);
		req.getRequestDispatcher("/jssdkTest2.jsp").forward(req, resp);
		
	}
}
