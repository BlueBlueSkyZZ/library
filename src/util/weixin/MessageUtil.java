package util.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import po.message.Image;
import po.message.ImageMessage;
import po.message.News;
import po.message.NewsMessage;
import po.message.TextMessage;
import util.recommend.DailyPush;
import util.sql.SQLUtil;

import com.thoughtworks.xstream.XStream;

public class MessageUtil {
	
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";   //CLICK��д��������
	public static final String MESSAGE_VIEW = "VIEW";   //VIEW��д��������
	public static final String MESSAGE_NEWS = "news";	
	public static final String MESSAGE_SCANCODE = "scancode_push";
	
	/**
	 * XMLתMAP
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		Map<String,String> map = new HashMap<String,String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = request.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		
		List<Element> list = root.elements();
		
		for(Element e:list){
			map.put(e.getName(), e.getText());
		}
		
		ins.close();
		return map;
	}
	
	/**
	 * �ı�תXML
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		XStream xstream = new XStream();
		// �滻��Ԫ�أ����������滻Ϊ"xml"��
		xstream.alias("xml", textMessage.getClass());   
		
		return xstream.toXML(textMessage);
		
	}
	
	/**
	 * ��װ�ı���Ϣ
	 * @param toUserName
	 * @param fromUserName
	 * @param content
	 * @return
	 */
	public static String initText(String toUserName,String fromUserName,String content){
		po.message.TextMessage text = new po.message.TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(String.valueOf(new Date().getTime()));
		text.setContent(content);
		
		return textMessageToXml(text);
	}
	
	
	// ���˵�(��ע���ںŷ���)
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("��ӭ��ע�������ƶ�ͼ���~\n\n");
		sb.append("������������Ȥ���鼮����");
		sb.append("�����ڲ˵��н�������ͼ��ݡ�");
		sb.append("���롰���������ߡ�help�����ٴβ鿴����ʾ��");
		return sb.toString();
	}
	
	// �Ӳ˵�1
	public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("����΢�Ź��ںŵĿ���");
		return sb.toString();
	}

	//�Ӳ˵�2
	public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("��ѹ�����");
		return sb.toString();
	}
	
	
	
	/**
	 * ͼ��תXML
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * ��װͼ����Ϣ
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initNewsMessage(String toUserName,String fromUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		
		News news = new News();
		news.setTitle("ÿ��һ��--" + "�����Ƽ�");
		news.setDescription("â�����ϵ�С��");
		news.setPicUrl("http://apis.juhe.cn/goodbook/img/06c4cdda88badf94acbbd5df889edf86.jpg");  
		news.setUrl("http://www.iotesta.com.cn/library/show_singleItem.action?bookno=647");
		
		newsList.add(news);
		
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setCreateTime(String.valueOf(new Date().getTime()));
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXml(newsMessage);
		return message;
	}
	
	/** 
	 * ��װͼ����Ϣ
	 * @param toUserName ���ں�
	 * @param fromUserName �û�id
	 * @param news �鱾��Ϣ
	 * @return
	 */
	public static String initNewsMessage(String toUserName,String fromUserName,News news){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		newsList.add(news);
		// ����ͼ����Ϣ����
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setCreateTime(String.valueOf(new Date().getTime()));
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXml(newsMessage);
		return message;
	}
	
	// ����ͼ����Ϣ
	public static News generateBookSearch(String bookno,String fromUserName){
		News news = new News();
		news.setTitle("��" + SQLUtil.getBookName(bookno) + "��");
		news.setDescription("�����������鼮��ϸ��Ϣ");
		news.setPicUrl(SQLUtil.getBookPicUrl(bookno));  
		news.setUrl("http://www.iotesta.com.cn/library/show_singleItem.action?bookno=" + bookno + "&weid=" + fromUserName);
		return news;
	}
	
	
	/**
	 * ͼƬתXML
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	
	
	/**
	 * ��װͼƬ��Ϣ
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	public static String initImageMessage(String toUserName,String fromUserName){
		String message = null;
		Image image = new Image();
		image.setMediaId("Q7QGASb0NanbxPqaPF__OkUHMEGcSOSD9qYfxL98Mww4tDTNa3inYGpNN_1vLKrX");
		
		ImageMessage imageMassage = new ImageMessage();
		imageMassage.setFromUserName(toUserName);
		imageMassage.setToUserName(fromUserName);
		imageMassage.setMsgType(MESSAGE_IMAGE);
		imageMassage.setCreateTime(String.valueOf(new Date().getTime()));
		imageMassage.setImage(image);
		
		message = imageMessageToXml(imageMassage);
		return message;
	}
	
	/**
	 * �ͷ��ӿڷ����ı���Ϣ
	 * @param openid �û�id
	 * @param type ��Ϣ����
	 * @param content ��Ϣ����
	 * @return
	 */
	public static String generateServiceMsg(String openid,String type,String content){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\"").append(openid).append("\",");
		sb.append("\"msgtype\":\"").append(type).append("\",");
		sb.append("\"text\":");
		sb.append("{");
		sb.append("\"content\":\"").append(content).append("\"");
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	
	/**
	 * �ͷ��ӿڷ���ͼ����Ϣ
	 * @param openid �û�id
	 * @param type ��Ϣ����
	 * @param news ͼ����Ϣʵ��
	 * @return
	 */
	public static String generateServiceImgTxtMsg(String openid,String type,News news){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"touser\":\"").append(openid).append("\",");
		sb.append("\"msgtype\":\"").append(type).append("\",");
		sb.append("\"news\":");
		sb.append("{");
		sb.append("\"articles\":[");
		sb.append("{");
		sb.append("\"title\":\"").append(news.getTitle()).append("\",");
		sb.append("\"description\":\"").append(news.getDescription()).append("\",");
		sb.append("\"url\":\"").append(news.getUrl()).append("\",");
		sb.append("\"picurl\":\"").append(news.getPicUrl()).append("\"");
		sb.append("}]");
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}
	
	
	/** 
	 * �Զ��ظ�
	 * @param content �û�����������Ϣ
	 * @param fromUserName ���ں�
	 * @param toUserName �û�id
	 * @return
	 */
	public static String handleUserMsg(String toUserName,String fromUserName,String content){
		if(content.equals("����") || content.equals("help")){
			return menuText();
		}else{
			String bookno = SQLUtil.getBooknoByBookname(content);
			System.out.println("�õ���content:" + content + ";booknoΪ��" + bookno);
			if(!bookno.isEmpty()){
				return bookno;
			}else{
				return "�������ָ���ݲ�֧�֣���л���Գ�����ͼ��ݵĹ�ע�����������롰���������ߡ�help���˽���ط���";
			}
		}
	}
	
	
	
	
}
