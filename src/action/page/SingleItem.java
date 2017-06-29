package action.page;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import po.Comment;
import po.book.Book;
import po.book.BookDetailInfo;
import po.user.UserDetailInfo;
import util.recommend.InterestUtil;
import util.sql.SQL4PersonalInfo;
import util.sql.SQLUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SingleItem extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	// ��������嵥
	public String addToShoppingCart() throws Exception{
		System.out.println("ִ��SingleItem��addToShoppingCart����");
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		ActionContext context = ActionContext.getContext();
		context.put("weid", weid);
		if(weid == null){
			return "reg";	// ��Ҫ��ע��
		}
		Boolean flag = SQL4PersonalInfo.addToShoppingCart(bookno, weid);
		if(flag){
			return "ok";
		}
		return "fail";
	}
	
	
	// Ԥ��
	public String addToReserve() throws Exception{
		System.out.println("ִ��SingleItem��addToReserve����");
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		String orderFlag = request.getParameter("orderFlag");
		ActionContext context = ActionContext.getContext();
		context.put("weid", weid);
		context.put("bookno", bookno);
		if(orderFlag.equals("yes")){
			UserDetailInfo user = SQLUtil.querySingleUser("weid", weid);
			context.put("user", user);
			return "order";
		}else{
			if(weid.isEmpty() || weid.equals("null")){
				return "reg";	
			}
			Boolean flag = SQL4PersonalInfo.addToReserve(weid, bookno);
			if(flag){
				return "ok";
			}
			return "fail";
		}
	}
	
	// Ԥ���ɹ���ת
	public String setOrderSuccess(){
		return "ok";
	}
	
	
	// ����
	public String getBookComments() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		HttpSession session = request.getSession();
		session.setAttribute("weid", weid);
		session.setAttribute("commentbookno", bookno);
		
		ActionContext context = ActionContext.getContext();
		context.put("weid", weid);
		context.put("bookno", bookno);
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
	
	
	// �û��������
	public void createNewComment() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String weid = request.getParameter("weid");
		String bookno = (String) request.getSession().getAttribute("commentbookno");
		String comment = request.getParameter("comment");
		SQLUtil.handleNewComment(weid, bookno, comment);
		
//		PrintWriter pw = response.getWriter();
//		pw.write("success");
//		pw.flush();
//		pw.close();
		
		String url = response.encodeURL("/library/get_book_comments?weid=" + weid + "&bookno=" + bookno);  
		System.out.println("url:" + url);
		response.sendRedirect(url);
	}
	
	
	// Ŀ¼
	public String getBookOutline() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		HashMap<String,String> bookInfo = SQLUtil.getBookOutline(bookno);
		
		ActionContext context = ActionContext.getContext();
		context.put("weid", weid);
		context.put("bookno",bookno);
		context.put("outline", bookInfo.get("outline"));
		context.put("bookname",bookInfo.get("bookname"));
		
		return "ok";
	}
	
	
	// �鿴�鼮����
	public String getBookXu() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String weid = request.getParameter("weid");
		String bookno = request.getParameter("bookno");
		HashMap<String,String> bookInfo = SQLUtil.getBookXu(bookno);
		
		ActionContext context = ActionContext.getContext();
		context.put("weid", weid);
		context.put("bookno",bookno);
		context.put("xu", bookInfo.get("xu"));
		context.put("bookname",bookInfo.get("bookname"));
		
		return "ok";
	}
	
	
	// ��ӡ�ȡ���ղ�,�����ӻ����ϲ����
	public void addToLike() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String flag = request.getParameter("flag");
		String bookno = request.getParameter("bookno");
		String weid = request.getParameter("weid");
		if(flag.equals("Y")){
			SQL4PersonalInfo.addToLike(weid, bookno);
			InterestUtil.clickLike(weid, bookno, true);
		}else if(flag.equals("N")){
			SQL4PersonalInfo.deleteLike(weid, bookno);
			InterestUtil.clickLike(weid, bookno, false);
		}
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
        //����¼������û�ϲ����
        InterestUtil.clickBook(weid, bookno);
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
        // ��ȡ����鼮��Ϣ
        ArrayList<Book> relativeBooks = SQLUtil.relativeBooks(bookno, book.getCategory());
        int size = relativeBooks.size();
        int a = (int)(Math.random()*size);
        int b = (int)(Math.random()*size);
        context.put("book1", relativeBooks.get(a));
        context.put("book2", relativeBooks.get(b));
        // �Ƿ��ѱ��ղ�
        String likeFlag = SQL4PersonalInfo.judgeLike(weid, bookno);
        System.out.println(likeFlag);
        HttpSession session = request.getSession();
        session.setAttribute("weid", weid);
        session.setAttribute("likeFlag", likeFlag);
        
		return SUCCESS;
	}
	
}
