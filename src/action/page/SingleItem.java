package action.page;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import po.BookDetailInfo;
import po.Comment;
import util.SQL4PersonalInfo;
import util.SQLUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SingleItem extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	// �����嵥
	public String addToShoppingCart() throws Exception{
		System.out.println("ִ��SingleItem��addToShoppingCart����");
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		SQL4PersonalInfo.addToShoppingCart(bookno, weid);
		ActionContext context = ActionContext.getContext();
        context.put("weid", weid);
		 
		return "ok";
	}
	
	
	// Ԥ��
	public String addToReserve() throws Exception{
		System.out.println("ִ��SingleItem��addToReserve����");
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		ActionContext context = ActionContext.getContext();
        context.put("weid", weid);
		Boolean flag = SQL4PersonalInfo.addToReserve(weid, bookno);
		if(flag){
			return "ok";
		}
		return "fail";
	}
	
	
	// ����
	public String getBookComments() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		ActionContext context = ActionContext.getContext();
		context.put("weid", weid);
		ArrayList<Comment> list = SQLUtil.getBookComments(bookno);
		context.put("commentlist", list);
		
		String bookname = "";
		String score = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			String query = "select bookname,score from book where bookno = " + bookno +";";
			ResultSet ret = s.executeQuery(query);
			while (ret.next()) {  
				bookname = ret.getString(1);
				score = ret.getString(2);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		context.put("bookname", bookname);
		context.put("score",score);
		
		return "ok";
	}
	
	
	// ��ʾ�����鼮
	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String weid = request.getParameter("weid");
        
        String bookno = request.getParameter("bookno");
		if(bookno == null){ 
			bookno = "1";  // ������������ʾ���Ϊ1���鼮��Ϣ
		}
		// ��ȡ�鼮��Ϣ
        BookDetailInfo book = SQLUtil.querySingleBookFromCat(bookno);
        ActionContext context = ActionContext.getContext();
        context.put("weid", weid);
        context.put("book", book);
        // ��ȡ��ǩ
        ArrayList<String> tags = SQLUtil.getBookTags(bookno);
        context.put("tags", tags);
        
		return SUCCESS;
	}

}
