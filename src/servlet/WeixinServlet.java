package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import action.sidemenu.Borrow;

import po.AccessToken;
import po.ReturnRemindMes;
import util.CheckUtil;
import util.MessageUtil;
import util.WeixinUtil;

public class WeixinServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
	
		PrintWriter out = resp.getWriter();
		
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
		
		doPost(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();
		
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");//用户weid
			String toUserName = map.get("ToUserName");//我们的微信号
			System.out.println("fromusername:" + fromUserName);
			System.out.println("tousername:" + toUserName);
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			
			//***************************************************************
			AccessToken token = WeixinUtil.getAccessToken();  //获取access_token
			
			String message = null;
			
			// 判断消息类型
//			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
//				if("1".equals(content)){
//					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
//				}
//				else if ("2".equals(content)){
//					message = MessageUtil.initNewsMessage(toUserName, fromUserName);
//				}
//				else if ("3".equals(content)){
//					message = MessageUtil.initImageMessage(toUserName, fromUserName);
//				}
//				else if ("?".equals(content) || "？".equals(content)){
//					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
//				}else if("查询ID".equals(content) || "查询id".equals(content)){
//					message = MessageUtil.initText(toUserName, fromUserName, fromUserName);
//				}else{
//					message = MessageUtil.initText(toUserName, fromUserName,content);
//				}
//			}
			if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType  = map.get("Event");
				
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					// 显示欢迎界面
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					String key = map.get("EventKey");
					//显示主菜单
					if(key.equals("11")){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());		
					}
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
					String key = map.get("EventKey");
//					message = MessageUtil.initText(toUserName, fromUserName, key);
				}
			}
			else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
				String label = map.get("Label");
				message = MessageUtil.initText(toUserName, fromUserName, label);
			}
			
			// 客服接口
			ArrayList<ReturnRemindMes> needRemind =  Borrow.needRemind();
			AccessToken access_token = WeixinUtil.getAccessToken();
			for (ReturnRemindMes returnRemindMes : needRemind) {
				String weid = returnRemindMes.getWeid();
				String messages = returnRemindMes.getMessage();
				//String messageXML =  MessageUtil.initText(weid, "", message);
				String JSONMessage = MessageUtil.generateServiceMsg(weid, "text", messages);
				int errcode = WeixinUtil.Cus_Service(access_token.getToken(), JSONMessage);
				System.out.println(errcode);
			}
			
			out.print(message);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
		
	}
	
	
}
