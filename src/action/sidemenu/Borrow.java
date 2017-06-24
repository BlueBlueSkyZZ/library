package action.sidemenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import po.book.BorrowedBook;
import po.message.ReturnRemind;
import po.message.ReturnRemindMes;
import util.sql.SQL4PersonalInfo;
import util.sql.SQLUtil;
import util.weixin.WeixinUtil;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

// ���ڽ����
public class Borrow extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	public String getPastTime(){
		HttpServletRequest request = ServletActionContext.getRequest();
		String bookno = request.getParameter("bookno");
		String borrowtime = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select borrowtime from borrow where bookno = " + bookno + 
							" and returntime is null;"; 
			ResultSet ret = s.executeQuery(query);
			while (ret.next()) {  
				borrowtime = ret.getString(1);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String datas[];
		int month = 1,day = 1;
		if(!borrowtime.isEmpty()){
			datas = borrowtime.split("-");
			month = Integer.parseInt(datas[1]);
			day = Integer.parseInt(datas[2]);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(new Date());
		String datas1[] = nowDate.split("-");
		int nowMonth = Integer.parseInt(datas1[1]);
		int nowDay = Integer.parseInt(datas1[2]);
		
		int temp = 1;
		if(nowDay >= day){
			temp = nowDay - day + 1;
		}else{
			switch(month){
				case 2: temp = 28-day+1+nowDay; break;
				case 4:
				case 6:
				case 9:
				case 11:temp = 28-day+1+nowDay; break;
				default: temp = 31-day+1+nowDay; break;
			}
		}
		// �Ȿ���Ѿ����ĵ�ʱ��
		int rate = (int)(100*temp*1.0/30);
		ActionContext context = ActionContext.getContext();
		context.put("rate", rate);
		
		return "ok";
	}
	
	@Override 
	public String execute() throws Exception {
		ArrayList<BorrowedBook> bookList = new ArrayList<BorrowedBook>();
		
		// ��ȡǰ̨�����Ĳ���weid
		HttpServletRequest request = ServletActionContext.getRequest();
		String weid = request.getParameter("weid");
		if(weid == null){
			weid = "1";
		}
		// ��ȡ�û��������ĵ���
		bookList = SQL4PersonalInfo.queryMyBorrow2(weid);
		ActionContext context = ActionContext.getContext();
		context.put("booklist", bookList);
		
		return SUCCESS;
	}
	
	
	public static ArrayList<ReturnRemind> getAllBorrow(){
//		HttpServletRequest request = ServletActionContext.getRequest();
//		String bookno = request.getParameter("bookno");
		String borrowtime = "";
		String weid = "";
		int bookno;
		int counttime;
		ArrayList<ReturnRemind> borrows = new ArrayList<ReturnRemind>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://" + WeixinUtil.MYSQL_DN , WeixinUtil.MYSQL_NAME, WeixinUtil.MYSQL_PASSWORD);
			Statement s = con.createStatement();
			
			String query = "select weid,bookno,borrowtime from borrow"; 
			ResultSet ret = s.executeQuery(query);
			while (ret.next()) {  
				ReturnRemind borrow = new ReturnRemind();
				weid = ret.getString(1);
				bookno = ret.getInt(2);
				borrowtime = ret.getString(3);
				counttime = getCountTime(borrowtime);
				
				borrow.setWeid(weid);
				borrow.setBookno(bookno);
				borrow.setBorrowtime(borrowtime);
				borrow.setCounttime(counttime);
				borrow.setBookname(SQLUtil.getBookName(String.valueOf(bookno)));
				
				borrows.add(borrow);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return  borrows;
		
		
	}
	
	/**
	 * ������˶�����
	 * @param borrowtime ��ʼ���ʱ��
	 * @return int ���˶�����
	 */
	public static int getCountTime(String borrowtime){
		String datas[];
		int month = 1,day = 1;
		if(!borrowtime.isEmpty()){
			datas = borrowtime.split("-");
			month = Integer.parseInt(datas[1]);
			day = Integer.parseInt(datas[2]);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = sdf.format(new Date());
		//System.out.println(nowDate);
		String datas1[] = nowDate.split("-");
		int nowMonth = Integer.parseInt(datas1[1]);
		int nowDay = Integer.parseInt(datas1[2]);
		
		int temp = 1;
		if(nowDay >= day){
			temp = nowDay - day + 1;
		}else{
			switch(month){
				case 2: temp = 28-day+1+nowDay; break;
				case 4:
				case 6:
				case 9:
				case 11:temp = 30-day+1+nowDay; break;
				default: temp = 31-day+1+nowDay; break;
			}
		}
		// �Ȿ���Ѿ����ĵ�ʱ��
//		int rate = (int)(100*temp*1.0/30);
//		ActionContext context = ActionContext.getContext();
//		context.put("rate", rate);
//		return "ok";
		return temp;
	}

	/**
	 * ��װ������Ҫ���ѵĶ���
	 * @return
	 */
	public static ArrayList<ReturnRemindMes> needRemind(){
		//��ȡ���н������
		ArrayList<ReturnRemind> all =  Borrow.getAllBorrow();
		//������Ҫ���ѵ���
		ArrayList<ReturnRemindMes> needRemind = new ArrayList<ReturnRemindMes>();
		
		for (ReturnRemind returnRemind : all) {
			int time = returnRemind.getCounttime();
			String bookname;
			String weid;
			int lefttime;
			String message;
			if(time < 17){
				//return null;
			}else if(time <21 && time >= 17){
				ReturnRemindMes one = new ReturnRemindMes();
				bookname = returnRemind.getBookname();
				weid = returnRemind.getWeid();
				lefttime = 30 - time;
				message = "�װ����û����ã����ڽ�ġ�" + bookname + "������" + lefttime + "�պ���";
				
				one.setWeid(weid);
				one.setMessage(message);
				needRemind.add(one);
			}else{
				ReturnRemindMes one = new ReturnRemindMes();
				bookname = returnRemind.getBookname();
				weid = returnRemind.getWeid();
				message = "�װ����û����ã����ڽ�ġ�" + bookname + "���ѹ��ڣ��������黹";
				one.setWeid(weid);
				one.setMessage(message);
				needRemind.add(one);
			}
		}
		
		return needRemind;
		
	}
	public static void main(String[] args) {
		ArrayList<ReturnRemind> borrows =  Borrow.getAllBorrow();
		int count = 0;
		for (ReturnRemind borrowedBook : borrows) {
			System.out.println("���ǵ�" + count++ + "����");
			System.out.println(borrowedBook.getWeid());
			System.out.println(borrowedBook.getBookname());
			System.out.println(borrowedBook.getBorrowtime());
			System.out.println(borrowedBook.getCounttime());
		}
		
		ArrayList<ReturnRemindMes> remind = Borrow.needRemind();
		int count1 = 0;
		for (ReturnRemindMes returnRemindMes : remind) {
			System.out.println("���ǵ�" + count1++ + "����");
			System.out.println(returnRemindMes.getWeid());
			System.out.println(returnRemindMes.getMessage());
		}
	}
}
