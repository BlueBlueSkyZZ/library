package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import po.Book;
import po.BookWithouImg;
import po.BorrowedBook;

// 定义了个人信息搜索的SQL函数
public class SQL4PersonalInfo {
	// 获取我的书架中收藏的书
	public static ArrayList<Book> queryMyBookshelf(String weid){
		ArrayList<Book> bookList = new ArrayList<Book>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select dist bookno,bookname,bookimg from bookshelf where weid = " + weid + ";"; 
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// 将搜索到的9本书放入ArrayList中
			while (ret.next()) {  
				Book book = new Book();
            	book.setBookno(ret.getString(1));
            	book.setBookname(ret.getString(2));
            	book.setBookimg(ret.getString(3));
            	bookList.add(book);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}
	
	
	// 获取个人借书情况（借过的书，现在已经还了）
	public static ArrayList<BorrowedBook> queryMyBorrow(String weid){
		ArrayList<BorrowedBook> bookList = new ArrayList<BorrowedBook>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select bookno,bookname,borrowtime,returntime from borrow where weid = " + weid + ";"; 
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// 将搜索到的9本书放入ArrayList中
			while (ret.next()) {  
				BorrowedBook book = new BorrowedBook();
            	book.setBookno(ret.getString(1));
            	book.setBookname(ret.getString(2));
            	book.setBorrowtime(ret.getString(3));
            	book.setReturntime(ret.getString(4));
            	bookList.add(book);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}
	
	
	// 获取个人购物车详情
	public static ArrayList<Book> queryShoppingCart(String weid){
		ArrayList<Book> bookList = new ArrayList<Book>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select bookno,bookname,bookimg from shoppingcart where weid = " + weid + ";"; 
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// 将搜索到的9本书放入ArrayList中
			while (ret.next()) {  
				Book book = new Book();
            	book.setBookno(ret.getString(1));
            	book.setBookname(ret.getString(2));
            	book.setBookimg(ret.getString(3));
            	bookList.add(book);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}
	
	
	// 处理借阅历史的相关数据
	public static Map<String, HashMap<String,Integer>> getPersonalBookInfo(String weid){
		Map<String, HashMap<String,Integer>> personalBookInfo = new HashMap<String, HashMap<String,Integer>>();
		int cnt1=0,cnt2=0,cnt3=0,cnt4=0,cnt5=0,cnt6=0,cnt7=0,cnt8=0,cnt9=0,
					cnt10=0,cnt11=0,cnt12=0,cnt13=0,cnt14=0,cnt15=0,cnt16=0,cnt17=0,cnt18=0,cnt19=0;
		// 种类分类
		HashMap<String,Integer> bookCat = new HashMap<String,Integer>();
		bookCat.put("a",cnt1);
		bookCat.put("b",cnt2);
		bookCat.put("c",cnt3);
		bookCat.put("d",cnt4);
		// 年份分类
		HashMap<String,Integer> yearCnt = new HashMap<String,Integer>();
		yearCnt.put("2015", cnt5);
		yearCnt.put("2016", cnt6);
		yearCnt.put("2017", cnt7);
		// 过去一年月度分类
		HashMap<String,Integer> monthCnt = new HashMap<String,Integer>();
		monthCnt.put("1", cnt8);
		monthCnt.put("2", cnt9);
		monthCnt.put("3", cnt10);
		monthCnt.put("4", cnt11);
		monthCnt.put("5", cnt12);
		monthCnt.put("6", cnt13);
		monthCnt.put("7", cnt14);
		monthCnt.put("8", cnt15);
		monthCnt.put("9", cnt16);
		monthCnt.put("10", cnt17);
		monthCnt.put("11", cnt18);
		monthCnt.put("12", cnt19);
		
		// 1.获取用户所有的借阅情况
		ArrayList<BorrowedBook> booklist = queryMyBorrow(weid);
		// 2.遍历每本书，在对应的分类中+1
		Iterator<BorrowedBook> iterator = booklist.iterator();
		while(iterator.hasNext()){
			BorrowedBook book = (BorrowedBook) iterator.next();
			String bookNo = book.getBookno();
			String cat = "";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = DriverManager.getConnection(
						"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
				Statement s = con.createStatement();
				
				String query = "select category from book where bookno = " + bookNo +  ";";
				ResultSet ret = s.executeQuery(query);
				while (ret.next()) {  
					cat = ret.getString(1);  // 获取借阅的书的类别
	            }
				// 种类分类计数
				if(cat.contains("文学") || cat.equals("小说") || cat.contains("儿童") || cat.equals("散文") || cat.equals("历史")){
					cnt1++;
					bookCat.remove("a");
					bookCat.put("a", cnt1);
				}else if(cat.equals("哲学") || cat.contains("心理") || cat.contains("鸡汤")){
					cnt2++;
					bookCat.remove("b");
					bookCat.put("b", cnt2);
				}else if(cat.contains("传记") || cat.equals("教育") || cat.equals("成功励志") || cat.equals("管理")){
					cnt3++;
					bookCat.remove("c");
					bookCat.put("c", cnt3);
				}else if(cat.contains("计算机")){
					cnt4++;
					bookCat.remove("d");
					bookCat.put("d", cnt4);
				}
				
	            con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 借阅日期格式：yyyy-mm-dd
			String borrowTime[] = book.getBorrowtime().split("-");
			String year = borrowTime[0];
			String month = borrowTime[1];
			
			// 年份分类计数  
			if(year.trim().equals("2015")){
				cnt5++;
				bookCat.remove("2015");
				bookCat.put("2015", cnt5);
			}else if(year.trim().equals("2016")){
				cnt6++;
				bookCat.remove("2016");
				bookCat.put("2016", cnt6);
			}else if(year.trim().equals("2017")){
				cnt7++;
				bookCat.remove("2017");
				bookCat.put("2017", cnt7);
			}
			
			
			// 月份分类计数
			if(month.trim().equals("01")){
				cnt8++;
				bookCat.remove("1");
				bookCat.put("1", cnt8);
			}else if(month.trim().equals("02")){
				cnt9++;
				bookCat.remove("2");
				bookCat.put("2", cnt9);
			}else if(month.trim().equals("03")){
				cnt10++;
				bookCat.remove("3");
				bookCat.put("3", cnt10);
			}else if(month.trim().equals("04")){
				cnt11++;
				bookCat.remove("4");
				bookCat.put("4", cnt11);
			}else if(month.trim().equals("05")){
				cnt12++;
				bookCat.remove("5");
				bookCat.put("5", cnt12);
			}else if(month.trim().equals("06")){
				cnt13++;
				bookCat.remove("6");
				bookCat.put("6", cnt13);
			}else if(month.trim().equals("07")){
				cnt14++;
				bookCat.remove("7");
				bookCat.put("7", cnt14);
			}else if(month.trim().equals("08")){
				cnt15++;
				bookCat.remove("8");
				bookCat.put("8", cnt15);
			}else if(month.trim().equals("09")){
				cnt16++;
				bookCat.remove("9");
				bookCat.put("9", cnt16);
			}else if(month.trim().equals("10")){
				cnt17++;
				bookCat.remove("10");
				bookCat.put("10", cnt17);
			}else if(month.trim().equals("11")){
				cnt18++;
				bookCat.remove("11");
				bookCat.put("11", cnt18);
			}else if(month.trim().equals("12")){
				cnt19++;
				bookCat.remove("12");
				bookCat.put("12", cnt19);
			}
		}
		
		// 将单个map存入大map中
		personalBookInfo.put("cat", bookCat);
		personalBookInfo.put("year", yearCnt);
		personalBookInfo.put("month", monthCnt);
		
		return personalBookInfo;
	}
	
	
	// 获取正在借阅的书（查找returntime为空的行）
	public static ArrayList<BorrowedBook> queryMyBorrow2(String weid){
		ArrayList<BorrowedBook> bookList = new ArrayList<BorrowedBook>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select bookno,bookname,borrowtime from borrow where weid = " + weid + 
							"and returntime is null;"; 
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// 将搜索到的9本书放入ArrayList中
			while (ret.next()) {  
				BorrowedBook book = new BorrowedBook();
            	book.setBookno(ret.getString(1));
            	book.setBookname(ret.getString(2));
            	book.setBorrowtime(ret.getString(3));
            	bookList.add(book);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}
	
	
	// 添加借阅
	public static void addToBorrow(String weid,BookWithouImg book1,BookWithouImg book2){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			// 标准化借书日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String time = df.format(new Date());
			// 添加到数据库中
			String query = "insert into borrow (weid,bookno,bookname,borrowtime) values ('" + weid + "','" 
					+ book1.getBookno() + "','" + book1.getBookname() + "','" + time + "');";
			// 输出命令
			System.out.println(query);  
			s.executeUpdate(query);
			
			// 如果用户借了两本书，则执行
			if(book2.getBookno() != null){
				query = "insert into borrow (weid,bookno,bookname,borrowtime) values ('" + weid + "','" 
						+ book2.getBookno() + "','" + book2.getBookname() + "','" + time + "');";
				s.executeUpdate(query);
			}
			
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
