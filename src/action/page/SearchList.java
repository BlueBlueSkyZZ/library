package action.page;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import po.Book;
import po.BookDetailInfo;
import po.BookInCategory;
import util.SQLUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class SearchList extends ActionSupport{
	private static final long serialVersionUID=1L;

	// 自动补全
	public void getKeywords() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String keyword = request.getParameter("search");
        
        StringBuffer sb = new StringBuffer();
//      格式：['百度1', '百度2', '百度3', '百度4', '百度5', '百度6', '百度7','a4','b1','b2','b3','b4' ]
        sb.append("['");
        try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select bookname from book " + "where bookname like '" + keyword +"%' limit " + " 0,5;";
			ResultSet ret = s.executeQuery(query);
			// 获取书籍信息
			while (ret.next()) {  
				sb.append(ret.getString(1));
				sb.append("','");
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        sb.deleteCharAt(sb.lastIndexOf(",")).deleteCharAt(sb.lastIndexOf("'"));
        sb.append("]");
        
        ActionContext context = ActionContext.getContext();
        context.put("var",sb.toString());
	}
	
	// 搜索
	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        // 获得搜索条件
        String pageNum = request.getParameter("pagenum");
        if(pageNum == null){
        	pageNum = "1";
        }
        String keyword = request.getParameter("search");
        if(keyword == null){
        	keyword = "历史";
        }
        
        ArrayList<BookInCategory> searchList = SQLUtil.querySingleBookFromSearch(keyword, pageNum);
        ActionContext context = ActionContext.getContext();
        context.put("keyword",keyword);
        context.put("pagenum", pageNum);
        context.put("booklist", searchList);
        
        return SUCCESS;
	}
}
