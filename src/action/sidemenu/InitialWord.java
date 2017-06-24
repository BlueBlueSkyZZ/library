package action.sidemenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import util.SearchFirstUtil;
import util.sql.SQL4PersonalInfo;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import po.book.BookInCategory;
import po.book.BorrowedBook;

public class InitialWord extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ����ͼ����Ϣ
	 * @throws IOException
	 */
	public void SearchFirst() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String data = request.getParameter("word");
		List<BookInCategory> books = new ArrayList<BookInCategory>();
		books = SearchFirstUtil.SearchFirstBooks(data);
		
		List<String> name = new ArrayList<String>() ;
		//name.add("ֻ�Ǹ�����");
		//name.add("��һ��");
		for (BookInCategory book : books) {
			name.add(book.getBookimg());
		}
		
		out.write(JSONArray.fromObject(books).toString());
		out.flush();
		out.close();
	}
	
	/**
	 * ��ת����
	 */
	@Override 
	public String execute() throws Exception {
	
		return SUCCESS;
	}
}
