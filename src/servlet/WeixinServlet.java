package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import po.AccessToken;
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
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			
			
			//***************************************************************
			AccessToken token = WeixinUtil.getAccessToken();  //��ȡaccess_token
			
			String message = null;
			
			// �ж���Ϣ����
			if(MessageUtil.MESSAGE_TEXT.equals(msgType)){
				if("1".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
				}
				else if ("2".equals(content)){
					message = MessageUtil.initNewsMessage(toUserName, fromUserName);
				}
				else if ("3".equals(content)){
					message = MessageUtil.initImageMessage(toUserName, fromUserName);
				}
				else if ("?".equals(content) || "��".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if("��ѯID".equals(content) || "��ѯid".equals(content)){
					message = MessageUtil.initText(toUserName, fromUserName, fromUserName);
				}else{
					message = MessageUtil.initText(toUserName, fromUserName,content);
				}
			}
			else if(MessageUtil.MESSAGE_EVENT.equals(msgType)){
				String eventType  = map.get("Event");
				
				if(MessageUtil.MESSAGE_SUBSCRIBE.equals(eventType)){
					// ��ʾ��ӭ����
					message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());
				}else if(MessageUtil.MESSAGE_CLICK.equals(eventType)){
					
					String key = map.get("EventKey");
					//����
					if(key.equals("33")){
						boolean result1 = WeixinUtil.checkSubscribe(fromUserName);
						
						if(result1 == false){ // δע��
							message = MessageUtil.initText(toUserName, fromUserName, "�㻹δע�ᣬ�޷�����" );
						}else{
							boolean result2 = WeixinUtil.checkBaoming(fromUserName);
							
							if(result2 == false){ //δ����
								message = MessageUtil.initText(toUserName, fromUserName, "�����ɹ�~");
							}else{
								message = MessageUtil.initText(toUserName, fromUserName, "���Ѿ��������������ظ�����");
							}
						}
					}
					
					//��ʾ���˵�
					else if(key.equals("11")){
						message = MessageUtil.initText(toUserName, fromUserName, MessageUtil.menuText());		
					}
							
				}else if(MessageUtil.MESSAGE_VIEW.equals(eventType)){
					// VIEW�¼���ͬʱ����servlet��url 
					String url = map.get("EventKey");
//					message = MessageUtil.initText(toUserName, fromUserName, url);
				}else if(MessageUtil.MESSAGE_SCANCODE.equals(eventType)){
					String key = map.get("EventKey");
//					message = MessageUtil.initText(toUserName, fromUserName, key);
				}
			}
			else if(MessageUtil.MESSAGE_LOCATION.equals(msgType)){
				String label = map.get("Label");
				message = MessageUtil.initText(toUserName, fromUserName, label);
			}
			out.print(message);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}finally{
			out.close();
		}
		
	}
	
	
}
