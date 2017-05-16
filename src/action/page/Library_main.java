package action.page;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import po.Book;

import util.SQLUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class Library_main extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	// ���뵥���鼮ҳ��
	public String enterWenxue() throws UnsupportedEncodingException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
		
        // ��ȡ��������
        String catName = request.getParameter("id");
        String pageNum = request.getParameter("pagenum");
        if(pageNum == null){
        	pageNum = "1";
        }
        System.out.println("pagenum:" + pageNum + " id:" + catName);
        
		ArrayList<Book> bookList = new ArrayList<Book>();
		ActionContext context = ActionContext.getContext();
		// ��ѧ���й���ѧ�������ѧ
       	if(catName.equals("wenxue")){
       		bookList = SQLUtil.querySingleCat("('�й���ѧ','�����ѧ')", pageNum);
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
       		context.put("booklist", bookList);
       		
       	}
       	// ����
       	else if(catName.equals("zhuanji")){
       		bookList = SQLUtil.querySingleCat("('���ﴫ��')", pageNum);
       		if (bookList.size() > 0) {
				context.put("booklist", bookList);
			}
//       		String str = SQLUtil.querySingleCat2("('���ﴫ��')", pageNum);
//       		String[] dataList = str.split("##");
//       		for(int i=0; i<dataList.length; i++){
//       			Book book = new Book();
//       			String[] bookdata = dataList[i].split(";;");
//       			book.setBookno(bookdata[0]);
//            	book.setBookname(bookdata[1]);
//            	book.setBookimg(bookdata[2]);
//            	bookList.add(book);
//       		}
       		context.put("booklist", bookList);
       	}
       	// ��ʷ
       	else if(catName.equals("lishi")){
//       		bookList = SQLUtil.querySingleCat("('��ʷ')", pageNum);
//       		if (bookList.size() > 0) {
//				context.put("booklist", bookList);
//			}
       		String str = SQLUtil.querySingleCat2("('��ʷ')", pageNum);
       		String[] dataList = str.split("##");
       		for(int i=0; i<dataList.length; i++){
       			Book book = new Book();
       			String[] bookdata = dataList[i].split(";;");
       			book.setBookno(bookdata[0]);
            	book.setBookname(bookdata[1]);
            	book.setBookimg(bookdata[2]);
            	bookList.add(book);
       		}
       		context.put("booklist", bookList);
       	}
       	// ��ѧ
       	else if(catName.equals("zhexue")){
//       		bookList = SQLUtil.querySingleCat("('��ѧ')", pageNum);
//       		if (bookList.size() > 0) {
//				context.put("booklist", bookList);
//			}
       		String str = SQLUtil.querySingleCat2("('��ѧ')", pageNum);
       		String[] dataList = str.split("##");
       		for(int i=0; i<dataList.length; i++){
       			Book book = new Book();
       			String[] bookdata = dataList[i].split(";;");
       			book.setBookno(bookdata[0]);
            	book.setBookname(bookdata[1]);
            	book.setBookimg(bookdata[2]);
            	bookList.add(book);
       		}
       		context.put("booklist", bookList);
       	}
       	// ��ͯ
       	else if(catName.equals("ertong")){
//       		bookList = SQLUtil.querySingleCat("('��ͯ��ѧ')", pageNum);
//       		if (bookList.size() > 0) {
//				context.put("booklist", bookList);
//			}
       		String str = SQLUtil.querySingleCat2("('��ͯ')", pageNum);
       		String[] dataList = str.split("##");
       		for(int i=0; i<dataList.length; i++){
       			Book book = new Book();
       			String[] bookdata = dataList[i].split(";;");
       			book.setBookno(bookdata[0]);
            	book.setBookname(bookdata[1]);
            	book.setBookimg(bookdata[2]);
            	bookList.add(book);
       		}
       		context.put("booklist", bookList);
       	}
       	// С˵
       	else if(catName.equals("xiaoshuo")){
//       		bookList = SQLUtil.querySingleCat("('С˵')", pageNum);
//       		if (bookList.size() > 0) {
//				context.put("booklist", bookList);
//			}
       		String str = SQLUtil.querySingleCat2("('С˵')", pageNum);
       		String[] dataList = str.split("##");
       		for(int i=0; i<dataList.length; i++){
       			Book book = new Book();
       			String[] bookdata = dataList[i].split(";;");
       			book.setBookno(bookdata[0]);
            	book.setBookname(bookdata[1]);
            	book.setBookimg(bookdata[2]);
            	bookList.add(book);
       		}
       		context.put("booklist", bookList);
       	}
       	// ��������������
       	else if(catName.equals("xinli")){
//       		bookList = SQLUtil.querySingleCat("('���鼦��','����ѧ')", pageNum);
//       		if (bookList.size() > 0) {
//				context.put("booklist", bookList);
//			}
       		String str = SQLUtil.querySingleCat2("('���鼦��','����ѧ')", pageNum);
       		String[] dataList = str.split("##");
       		for(int i=0; i<dataList.length; i++){
       			Book book = new Book();
       			String[] bookdata = dataList[i].split(";;");
       			book.setBookno(bookdata[0]);
            	book.setBookname(bookdata[1]);
            	book.setBookimg(bookdata[2]);
            	bookList.add(book);
       		}
       		context.put("booklist", bookList);
       	}
       	// ��᣺�ɹ���־������������
       	else if(catName.equals("zhexue")){
//       		bookList = SQLUtil.querySingleCat("('�ɹ���־','����','����')", pageNum);
//       		if (bookList.size() > 0) {
//				context.put("booklist", bookList);
//			}
       		String str = SQLUtil.querySingleCat2("('�ɹ���־','����','����')", pageNum);
       		String[] dataList = str.split("##");
       		for(int i=0; i<dataList.length; i++){
       			Book book = new Book();
       			String[] bookdata = dataList[i].split(";;");
       			book.setBookno(bookdata[0]);
            	book.setBookname(bookdata[1]);
            	book.setBookimg(bookdata[2]);
            	bookList.add(book);
       		}
       		context.put("booklist", bookList);
       	}
       	// �����
       	else if(catName.equals("jisuanji")){
//       		bookList = SQLUtil.querySingleCat("('�����')", pageNum);
//       		if (bookList.size() > 0) {
//				context.put("booklist", bookList);
//			}
       		String str = SQLUtil.querySingleCat2("('�����')", pageNum);
       		String[] dataList = str.split("##");
       		for(int i=0; i<dataList.length; i++){
       			Book book = new Book();
       			String[] bookdata = dataList[i].split(";;");
       			book.setBookno(bookdata[0]);
            	book.setBookname(bookdata[1]);
            	book.setBookimg(bookdata[2]);
            	bookList.add(book);
       		}
       		context.put("booklist", bookList);
       	}
		return "ok";
	}
	
	@Override // ������ҳ
	public String execute() throws Exception {
		
		return SUCCESS;
	}

}
