package action.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import po.user.UserDetailInfo;
import util.sql.SQLUtil;
import util.weixin.WeixinUtil;

import com.opensymphony.xwork2.ActionSupport;

public class Register extends ActionSupport{
	private static final long serialVersionUID=1L;
	private static final String GetWeixinInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	private static final String GetCode = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	
	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        //�û���Ȩ���ȡcode
        String code = request.getParameter("code");	        
        
        // ͨ��code��ȡ������Ϣ
		String userID = "";
		String access_token = "";
		String refresh_token = "";
		String url = GetCode.replace("APPID", WeixinUtil.APPID).replace("SECRET", WeixinUtil.APPSECRET).replace("CODE", code);
		JSONObject jsonObject = WeixinUtil.doGetStr(url);
		if(jsonObject != null){
			userID = jsonObject.getString("openid");
			access_token = jsonObject.getString("access_token");
			refresh_token = jsonObject.getString("refresh_token");
		}
		
		// ��ȡ�û�������Ϣ
		String url2 = GetWeixinInfo.replace("ACCESS_TOKEN", access_token).replace("OPENID", userID);
		JSONObject jsonObject2 = WeixinUtil.doGetStr(url2);
		UserDetailInfo user = new UserDetailInfo();
		String openID = "";
		String nickname = "";
		String headimgurl = "";
		if(jsonObject2 != null){
			openID = jsonObject2.getString("openid");
			nickname = jsonObject2.getString("nickname");
			headimgurl = jsonObject2.getString("headimgurl");
			// ��ȡ�û�΢���˻�
			user.setOpenid(openID);
			user.setNickname(nickname);
			user.setHeadimgurl(headimgurl);
		}
		
		// ����û���΢���˻���Ϣ�����ݿ�
		if(!SQLUtil.judgeReg("library",openID)){
			// �û�δע��
			SQLUtil.addUserWXInfo("library",openID,nickname,headimgurl);
		}else{
			// �û���ע��
			return "haveReg"; // ����ʧ��ҳ��
		}
		
		request.setAttribute("user", user);
			
//        UserDetailInfo user = new UserDetailInfo();
//        user.setOpenid("gyz123");
//        user.setNickname("��Ԫ��");
//        request.setAttribute("user", user);
        
		return SUCCESS;
	}
}
