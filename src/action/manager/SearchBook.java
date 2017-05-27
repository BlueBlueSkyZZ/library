package action.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import po.Book;
import po.BookDetailInfo;
import util.SQLUtil;

import com.opensymphony.xwork2.ActionSupport;

public class SearchBook extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	// ����Ա�㿪һ���������bookno�����ظñ������ϸ��Ϣ
	public String searchSingle() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String bookNo = request.getParameter("bookno");
		BookDetailInfo book = SQLUtil.querySingleBookFromCat(bookNo);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(book.toString()); // ���ص�ǰ�鱾��Ϣ
		pw.flush();
		pw.close();
		
		return null;
	}
	
	// ����Ա����������֣�����bookname�����ؾ��йؼ��ֵ���ļ�����Ϣ
//	public String searchList() throws IOException{
//		HttpServletRequest request = ServletActionContext.getRequest();
//		String bookName = request.getParameter("bookname");
//		ArrayList<Book> bookSearchList = new ArrayList<Book>();
//		bookSearchList = SQLUtil.querySingleBookFromSearch(bookName, "1");
//		
//		StringBuffer sb = new StringBuffer();
//		Iterator<Book> iterator = bookSearchList.iterator();
//		while(iterator.hasNext()){
//			Book book = iterator.next();
//			sb.append(book.toString());
//		}
//		
//		HttpServletResponse response = ServletActionContext.getResponse();
//		response.setCharacterEncoding("UTF-8");
//		PrintWriter pw = response.getWriter();
//		pw.write(sb.toString());
//		pw.flush();
//		pw.close();
//		
//		return null;
//	}
	
	public String searchList() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		String bookName = request.getParameter("bookname");
		BookDetailInfo book = SQLUtil.querySingleBookByKeyword(bookName);

		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(book.toString());
		pw.flush();
		pw.close();

		return null;
	}
	
	// ��ȡ��ʼ�鼮�б�
	public String bookList() throws IOException{
		ArrayList<BookDetailInfo> bookList = new ArrayList<BookDetailInfo>();
		bookList = SQLUtil.querySingleCat3("('�ɹ���־','����','����')", "1");
		
		StringBuffer sb = new StringBuffer();
		Iterator<BookDetailInfo> iterator = bookList.iterator();
		while(iterator.hasNext()){
			BookDetailInfo book = iterator.next();
			sb.append(book.toString());
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(sb.toString());
		pw.flush();
		pw.close();
		
		return null;
	}

}
