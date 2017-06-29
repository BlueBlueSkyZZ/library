package action.sidemenu;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import util.sql.SQLUtil;
import util.weixin.CheckUtil;
import util.weixin.PastUtil;
import util.weixin.WeixinUtil;

import com.opensymphony.xwork2.ActionSupport;

public class Scan extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	// ʶ���ά��
	public void handleCodeScan() throws Exception{
		System.out.println("ִ����Scan��handleCodeScan����");
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("utf-8");
        
		String weid = request.getSession(false).getAttribute("weid").toString();
		String QRCodeTxt = request.getParameter("QRCodetxt");
		System.out.println("ɨ����Ϊ��" + QRCodeTxt);
		
		String str = decodeJSON(QRCodeTxt);
		String bookno = "";
		String regexISBN = "[0-9]{8,}";
		String regexBookno = "[0-9]{1,4}";
		
		if(str.matches(regexBookno)){
			bookno = str;
			System.out.println("�û�ɨ���鼮��ά�룺" + bookno);
			String url = response.encodeURL("/library/add_to_shoppingcart.action?weid=" + weid + "&bookno=" + bookno);  
			response.sendRedirect(url);
		}else{
			String[] datas = str.split(",");
			if(datas[1].matches(regexISBN)){
				System.out.println("�û�ɨ��ISBN��" + datas[1]);
				bookno = SQLUtil.getBooknoByISBN(datas[1]);
				String url = response.encodeURL("/library/show_singleItem.action?weid=" + weid + "&bookno=" + bookno);  
				response.sendRedirect(url);
			}else{
				return ;
			}
		}
		return ;
	}
	
	
	// ��ʾɨһɨ
	@Override
	public String execute() throws Exception {
		HttpServletRequest req = ServletActionContext.getRequest();
		req.setCharacterEncoding("utf-8");
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		
		Map<String,String> map = PastUtil.getParam(WeixinUtil.APPID, WeixinUtil.APPSECRET, req);		
		String noncestr = map.get("nonceStr");
		String jsapi_ticket = map.get("jsapi_ticket");
		String timestamp = map.get("timestamp");
		String url = map.get("url");
		System.out.println(map.toString());
		// ����ǩ��
		String signature = CheckUtil.generateSignature(noncestr, jsapi_ticket, timestamp, url);
		
		// ���ò���
		req.setAttribute("appId", WeixinUtil.APPID);
		req.setAttribute("timeStamp", timestamp);
		req.setAttribute("nonceStr", noncestr);
		req.setAttribute("signature", signature);
		req.setAttribute("url", url);
		
		return SUCCESS;
	}

	

	// ����json
	private String decodeJSON(String json){
		JSONObject jsonObject = JSONObject.fromObject(json);
		return jsonObject.getString("resultStr");
	}
	
}
