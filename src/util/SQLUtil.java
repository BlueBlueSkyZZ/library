package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import po.Book;
import po.BookDetailInfo;
import po.BookInCategory;
import po.Comment;
import po.UserDetailInfo;

public class SQLUtil {
	// �ж��ظ�ע��
	public static boolean judgeReg(String database,String openID){
		boolean flag = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			String query = "select * from user";
			ResultSet ret = s.executeQuery(query);
			while (ret.next()) {  
            	String weid = ret.getString(1);  
                if(weid.equals(openID)){
                	flag = true;  // true �����û��Ѿ�ע�ᣬ�����ش������
                	break;
                }
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag; 
	}
	
	// ����û�΢����Ϣ
	public static void addUserWXInfo(String database,String openid,String nickname,String headimg){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			String query = "insert into user (weid,wename,weimg) values ('" + openid + "','" 
					+ nickname + "','" + headimg + "');";
			System.out.println(query);
			s.executeUpdate(query);
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ����û������Ϣ
	public static void addUserRealInfo(String database,String userName,String idCard,String phone,String weID){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			String query = "update user set username = '" + userName + "',idcard = '" + idCard + "',phone = '" 
						+ phone + "' where weid = '" + weID + "';";
//			System.out.println(query);
			s.executeUpdate(query);
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// ���������û�����ϸ��Ϣ
	public static ArrayList<UserDetailInfo> queryUserList(){
		ArrayList<UserDetailInfo> userList = new ArrayList<UserDetailInfo>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			String query = "select * from user";
			ResultSet ret = s.executeQuery(query);
			while (ret.next()) {  
            	String weid = ret.getString(1);  
            	String phone = ret.getString(2);
            	String wename  = ret.getString(3);
            	String weimg = ret.getString(4);
            	String idcard = ret.getString(5);
            	String username = ret.getString(6);
            	UserDetailInfo user = new UserDetailInfo();
            	user.setOpenid(weid);
            	user.setTel(phone);
            	user.setNickname(wename);
            	user.setHeadimgurl(weimg);
            	user.setIdCard(idcard);
            	user.setRealName(username);
            	userList.add(user);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userList;
	}
	
	// ���ص����û�����ϸ��Ϣ
	public static UserDetailInfo querySingleUser(String wename){
		UserDetailInfo user = new UserDetailInfo();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			String query = "select * from user where wename = '" + wename +"';";
//			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// ��ȡ�û���Ϣ
			while (ret.next()) {  
				user.setOpenid(ret.getString(1));
				user.setTel(ret.getString(2));
				user.setNickname(ret.getString(3));
				user.setHeadimgurl(ret.getString(4));
				user.setIdCard(ret.getString(5));
				user.setRealName(ret.getString(6));
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	
	// ���ҵ����鼮����ţ�������ͼƬ�����ߣ�ʣ�࣬�Ķ��������֣�
	public static ArrayList<BookInCategory> querySingleCat(String category,String pageNum,String target){
		ArrayList<BookInCategory> bookList = new ArrayList<BookInCategory>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select bookno,bookname,bookimg,author,leftnum,publisher,readingnum,score from book where category in " + category 
					+ " ORDER BY " + target + " DESC"
					+ " limit " + (5*((Integer.parseInt(pageNum))-1)) + ",5;";
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// ����������9�������ArrayList��
			while (ret.next()) {  
				BookInCategory book = new BookInCategory();
            	book.setBookno(ret.getString(1));
            	book.setBookname(ret.getString(2));
            	book.setBookimg(ret.getString(3));
            	book.setAuthor(ret.getString(4));
            	book.setLeftNum(ret.getString(5));
				book.setPublisher(ret.getString(6));
				book.setReadingnum(ret.getString(7));
            	book.setScore(ret.getString(8));
            	bookList.add(book);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}
	
	
	// ���ҵ����鼮�����У����������룩
	public static BookDetailInfo querySingleBookFromCat(String bookNo){
		BookDetailInfo book = new BookDetailInfo();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select * from book where bookno = " + bookNo +";";
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// ��ȡ�鼮��Ϣ
			while (ret.next()) {  
            	String bookno = ret.getString(1);
            	book.setBookno(bookno);
            	String isbn = ret.getString(2);
            	book.setIsbn(isbn);
            	String bookname = ret.getString(3);
            	book.setBookname(bookname);
            	book.setCategory(ret.getString(4));
            	book.setPublisher(ret.getString(5));
            	book.setVersion(ret.getString(6));
            	book.setBookimg(ret.getString(7));
            	book.setOutline(ret.getString(8));
            	book.setBookAbstract(ret.getString(9));
            	book.setGuide(ret.getString(10));
            	book.setLeftnum(ret.getString(11));
            	book.setPrice(ret.getString(12));
            	book.setAuthor(ret.getString(13));
            	book.setReadingnum(ret.getString(14));
            	book.setScore(ret.getString(15));
            }
			query = "select tag1,tag2,tag3 from booktag where bookno = " + bookNo +";";
			ret = s.executeQuery(query);
			while (ret.next()) {  
            	if(ret.getString(1)!=null)
            		book.setTag1(ret.getString(1));
            	if(ret.getString(2)!=null)
            		book.setTag2(ret.getString(2));
            	if(ret.getString(3)!=null)
            		book.setTag3(ret.getString(3));
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return book;
	}
	
	
	// ��ȡ��ǩ
	public static ArrayList<String> getBookTags(String bookno){
		ArrayList<String> tags = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			String query = "select tag1,tag2,tag3 from booktag where bookno = " + bookno +";";
			ResultSet ret = s.executeQuery(query);
			while (ret.next()) {  
            	if(ret.getString(1)!=null)
            		tags.add(ret.getString(1));
            	if(ret.getString(2)!=null)
            		tags.add(ret.getString(2));
            	if(ret.getString(3)!=null)
            		tags.add(ret.getString(3));
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tags;
	}
	
	
	// ��ȡ����
	public static ArrayList<Comment> getBookComments(String bookno){
		ArrayList<Comment> list = new ArrayList<Comment>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			String query = "select bookcomment.bookno,bookcomment.comment,bookcomment.commentid,bookcomment.time," +
					"user.weid,user.wename,user.weimg,bookcomment.goodnum " +
					"from bookcomment,user " +
//					"where bookcomment.weid = user.weid and bookcomment.bookno = " + bookno +";";
					"where bookcomment.bookno = " + bookno +";";
			ResultSet ret = s.executeQuery(query);
			while (ret.next()) {  
				Comment comment = new Comment();
				comment.setBookno(ret.getString(1));
				comment.setComment(ret.getString(2));
				comment.setCommentid(ret.getString(3));
				comment.setTime(ret.getString(4));
				comment.setGoodnum(ret.getString(8)); 	// ������
				
				comment.setWeid(ret.getString(5));
				comment.setWename(ret.getString(6));
				comment.setWeimg(ret.getString(7));
				list.add(comment);
			}
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	// ���ݹؼ��������鼮�б� (��ţ�������ͼƬ�������磬���ߣ�ʣ����, �Ķ���������)
 	public static ArrayList<BookInCategory> querySingleBookFromSearch(String keyword,String pageNum){
		ArrayList<BookInCategory> bookSearchList = new ArrayList<BookInCategory>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select bookno,bookname,bookimg,publisher,author,leftnum,readingnum,score from book " +
								"where bookname like '%" + keyword +"%' limit "
								+ (5*((Integer.parseInt(pageNum))-1)) + ",5;";
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// ��ȡ�鼮��Ϣ
			while (ret.next()) {  
				BookInCategory book = new BookInCategory();
            	book.setBookno(ret.getString(1));
            	book.setBookname(ret.getString(2));
            	book.setBookimg(ret.getString(3));
            	book.setPublisher(ret.getString(4));
            	book.setAuthor(ret.getString(5));
            	book.setLeftNum(ret.getString(6));
            	book.setReadingnum(ret.getString(7));
            	book.setScore(ret.getString(8));
            	bookSearchList.add(book);
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bookSearchList;
	}
	
	
	// ����bookname���ҵ����鼮  ��������Ϣ��
	public static BookDetailInfo querySingleBookByKeyword(String keyword){
		BookDetailInfo book = new BookDetailInfo();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library" , "root", "root");
			Statement s = con.createStatement();
			
			String query = "select * from book where bookname = '" + keyword +"';";
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// ��ȡ�鼮��Ϣ
			while (ret.next()) {  
            	String bookno = ret.getString(1);
            	book.setBookno(bookno);
            	String isbn = ret.getString(2);
            	book.setIsbn(isbn);
            	String bookname = ret.getString(3);
            	book.setBookname(bookname);
            	book.setCategory(ret.getString(4));
            	book.setPublisher(ret.getString(5));
            	book.setVersion(ret.getString(6));
            	book.setBookimg(ret.getString(7));
            	book.setOutline(ret.getString(8));
            	book.setBookAbstract(ret.getString(9));
            	book.setGuide(ret.getString(10));
            	book.setLeftnum(ret.getString(11));
            	book.setPrice(ret.getString(12));
            	book.setAuthor(ret.getString(13));
            	book.setReadingnum(ret.getString(14));
            	book.setScore(ret.getString(15));
            }
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return book;
	}
	
	
	// ���ҵ����鼮 ��������Ϣ��
	public static ArrayList<BookDetailInfo> querySingleCat3(String category,
			String pageNum) {
		ArrayList<BookDetailInfo> bookList = new ArrayList<BookDetailInfo>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/library", "root", "root");
			Statement s = con.createStatement();

			String query = "select * from book where category in "
					+ category
					+ " limit "
					+ (5 * ((Integer.parseInt(pageNum)) - 1)) + ",5;";
			System.out.println(query);
			ResultSet ret = s.executeQuery(query);
			// ����������9�������ArrayList��
			while (ret.next()) {
				BookDetailInfo book = new BookDetailInfo();
				String bookno = ret.getString(1);
            	book.setBookno(bookno);
            	String isbn = ret.getString(2);
            	book.setIsbn(isbn);
            	String bookname = ret.getString(3);
            	book.setBookname(bookname);
            	book.setCategory(ret.getString(4));
            	book.setPublisher(ret.getString(5));
            	book.setVersion(ret.getString(6));
            	book.setBookimg(ret.getString(7));
            	book.setOutline(ret.getString(8));
            	book.setBookAbstract(ret.getString(9));
            	book.setGuide(ret.getString(10));
            	book.setLeftnum(ret.getString(11));
            	book.setPrice(ret.getString(12));
            	book.setAuthor(ret.getString(13));
            	book.setReadingnum(ret.getString(14));
            	book.setScore(ret.getString(15));
				bookList.add(book);
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bookList;
	}
	
}
