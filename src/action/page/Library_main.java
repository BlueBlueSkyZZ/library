package action.page;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import po.Book;
import po.BookInCategory;

import util.SQLUtil;
import util.WeixinUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Library_main extends ActionSupport{
	private static final long serialVersionUID=1L;
	private static final String GetCode = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	
	// ���뵥���鼮ҳ��
	public String enterWenxue() throws UnsupportedEncodingException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
		
        // ��ȡ��������
        String catName = request.getParameter("id");
        String pageNum = request.getParameter("pagenum");
        String target = request.getParameter("target");
        if(pageNum == null || pageNum.equals("0")){
        	pageNum = "1";
        }
        if(target == null){
        	target = "bookno";
        }
        
        ActionContext context = ActionContext.getContext();
        context.put("pagenum", pageNum);
        ArrayList<BookInCategory> bookList = new ArrayList<BookInCategory>();
		// ��ѧ���й���ѧ�������ѧ
       	if(catName.equals("wenxue")){
       		bookList = SQLUtil.querySingleCat("('�й���ѧ','�����ѧ')", pageNum, target);
//       		String str = SQLUtil.querySingleCat2("('�й���ѧ','�����ѧ')", pageNum);
//       		String[] dataList = str.split("##");
//       		System.out.println("�� " + dataList.length + " ����");
//       		for(int i=0; i<dataList.length; i++){
//       			Book book = new Book();
//       			System.out.println("��ǰ�鱾��Ϣ: " + dataList[i]);
//       			String[] bookdata = dataList[i].split(";;");
//       			System.out.println("ÿ���� " + bookdata.length + " ������: ");
//       			book.setBookno(bookdata[0]);
//            	book.setBookname(bookdata[1]);
//            	book.setBookimg(bookdata[2]);
//            	bookList.add(book);
//       		}
       		context.put("catid","wenxue");
       		context.put("cat", "��ѧ");
       		context.put("booklist", bookList);
       		
       	}
       	// ����
       	else if(catName.equals("zhuanji")){
       		bookList = SQLUtil.querySingleCat("('���ﴫ��')", pageNum, target);
       		if (bookList.size() > 0) {
				context.put("booklist", bookList);
			}
       		context.put("catid","zhuanji");
       		context.put("cat", "����");
       		context.put("booklist", bookList);
       	}
       	// ��ʷ
       	else if(catName.equals("lishi")){
       		bookList = SQLUtil.querySingleCat("('��ʷ')", pageNum, target);
       		context.put("catid","lishi");
       		context.put("cat", "��ʷ");
       		context.put("booklist", bookList);
       	}
       	// ��ѧ
       	else if(catName.equals("zhexue")){
       		bookList = SQLUtil.querySingleCat("('��ѧ')", pageNum, target);
       		context.put("catid","zhexue");
       		context.put("cat", "��ѧ");
       		context.put("booklist", bookList);
       	}
       	// ��ͯ
       	else if(catName.equals("ertong")){
       		bookList = SQLUtil.querySingleCat("('��ͯ��ѧ')", pageNum, target);
       		context.put("catid","ertong");
       		context.put("cat", "�ٶ�");
       		context.put("booklist", bookList);
       	}
       	// С˵
       	else if(catName.equals("xiaoshuo")){
       		bookList = SQLUtil.querySingleCat("('С˵')", pageNum, target);
       		context.put("catid","xiaoshuo");
       		context.put("cat", "С˵");
       		context.put("booklist", bookList);
       	}
       	// ��������������
       	else if(catName.equals("xinli")){
       		bookList = SQLUtil.querySingleCat("('���鼦��','����ѧ')", pageNum, target);
       		context.put("catid","xinli");
       		context.put("cat", "����");
       		context.put("booklist", bookList);
       	}
       	// ��᣺�ɹ���־������������
       	else if(catName.equals("shehui")){
       		bookList = SQLUtil.querySingleCat("('�ɹ���־','����','����')", pageNum, target);
       		context.put("catid","shehui");
       		context.put("cat", "���");
       		context.put("booklist", bookList);
       	}
       	// �����
       	else if(catName.equals("keji")){
       		bookList = SQLUtil.querySingleCat("('�����')", pageNum, target);
       		context.put("catid","keji");
       		context.put("cat", "�Ƽ�");
       		context.put("booklist", bookList);
       	}
		return "ok";
	}
	
	@Override // ������ҳ
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        String weid = "";
        // ��ȡ�û�΢��id
//        String code = request.getParameter("code");	 
//		String url = GetCode.replace("APPID", WeixinUtil.APPID).replace("SECRET", WeixinUtil.APPSECRET).replace("CODE", code);
//		JSONObject jsonObject = WeixinUtil.doGetStr(url);
//		if(jsonObject != null){
//			weid = jsonObject.getString("openid");
//		}
		
		ActionContext context = ActionContext.getContext();
		if(weid == null){
			weid = "1";  // ��������
		}
		context.put("weid",weid);
		
		return SUCCESS;
	}

}
