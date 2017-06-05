package action.sidemenu;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import po.Book;
import util.SQL4PersonalInfo;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ShoppingCart extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	// ɾ�����ﳵ����
	public String delete(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		SQL4PersonalInfo.deleteBookFromCart(weid, bookno);
		return null;
	}
	
	
	@Override // ��ȡ�û��Ĺ��ﳵ�б�
	public String execute() throws Exception {
		ArrayList<Book> bookList = new ArrayList<Book>();
		HttpServletRequest request = ServletActionContext.getRequest();
		String weid = request.getParameter("weid");
		// ��ȡ���ﳵ����Ʒ�б�
		bookList = SQL4PersonalInfo.queryShoppingCart(weid);
		ActionContext context = ActionContext.getContext();
		context.put("booklist", bookList);
		return SUCCESS;
	}

}
