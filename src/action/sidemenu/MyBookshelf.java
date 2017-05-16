package action.sidemenu;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import po.Book;
import util.SQL4PersonalInfo;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MyBookshelf extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	@Override 
	public String execute() throws Exception {
		ArrayList<Book> bookList = new ArrayList<Book>();
		
		// ��ȡǰ̨�����Ĳ���weid
		HttpServletRequest request = ServletActionContext.getRequest();
		String weid = request.getParameter("weid");
		// ��ȡ�û����ղ��б�
		bookList = SQL4PersonalInfo.queryMyBookshelf(weid);
		ActionContext context = ActionContext.getContext();
		context.put("booklist", bookList);
		
		return SUCCESS;
	}
	
}
