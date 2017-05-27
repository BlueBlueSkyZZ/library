package action.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import po.UserDetailInfo;
import util.SQLUtil;

import com.opensymphony.xwork2.ActionSupport;

public class SearchUser extends ActionSupport{
	private static final long serialVersionUID=1L;

	// ��ȡ�����û��ĵ���ϸ��Ϣ
	public String searchList() throws IOException{
		ArrayList<UserDetailInfo> userList = new ArrayList<UserDetailInfo>();
		userList = SQLUtil.queryUserList();
		
		StringBuffer sb = new StringBuffer();
		Iterator<UserDetailInfo> iterator = userList.iterator();
		while(iterator.hasNext()){
			UserDetailInfo user = iterator.next();
			sb.append(user.toString());
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(sb.toString());
		pw.flush();
		pw.close();
		
		return null;
	}
	
	// ͨ��΢���ǳ������û�
	public String searchSingle() throws IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		String wename = request.getParameter("wename");
		UserDetailInfo user = SQLUtil.querySingleUser(wename); // ��ȡ�û���Ϣ
		// ��������
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw = response.getWriter();
		pw.write(user.toString());
		pw.flush();
		pw.close();
		return null;
	}
	
}
