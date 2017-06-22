package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import po.Book;
import po.BookInCategory;

public class SearchFirstUtil {

	static Map<String, String> firstMap = new HashMap<String, String>();
	private static String getValue(String start){
		firstMap.put("A", "߹");
		firstMap.put("B", "��");
		firstMap.put("C", "��");
		firstMap.put("D", "��");
		firstMap.put("E", "��");
		firstMap.put("F", "��");
		firstMap.put("G", "�");
		firstMap.put("H", "��");
		firstMap.put("J", "آ");
		firstMap.put("K", "��");
		firstMap.put("L", "��");
		firstMap.put("M", "�`");
		firstMap.put("N", "��");
		firstMap.put("O", "��");
		firstMap.put("P", "�r");
		firstMap.put("Q", "��");
		firstMap.put("R", "��");
		firstMap.put("S", "��");
		firstMap.put("T", "��");
		firstMap.put("W", "��");
		firstMap.put("X", "Ϧ");
		firstMap.put("Y", "Ѿ");
		firstMap.put("Z", "զ");
		
		//����Map
		Iterator<String> iter = firstMap.keySet().iterator();
		String key, value = null;
		while(iter.hasNext()){
			key = iter.next();
			value = firstMap.get(key);
			if(key.equals(start)){
				return value;
			}
		}
		return value;
	}
	
	public static Connection getConnection(){
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(
					"jdbc:mysql://" + WeixinUtil.MYSQL_DN , WeixinUtil.MYSQL_NAME, WeixinUtil.MYSQL_PASSWORD);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	/**
	 * ��������ĸ��ѯ
	 * @param start ��ʼ�ı�ʶ�������ģ����磺�ˣ�ba��
	 * @param end �����ı�ʶ�������ģ����磺����ca��
	 * @return
	 */
	public static ArrayList<BookInCategory> SearchFirstBooks(String start){
		//end �����ı�ʶ�������ģ����磺����ca��
		String end; 
		
		ArrayList<BookInCategory> books = new ArrayList<BookInCategory>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://" + WeixinUtil.MYSQL_DN , WeixinUtil.MYSQL_NAME, WeixinUtil.MYSQL_PASSWORD);
			Statement s = con.createStatement();
			String query;
			if(!start.equals(getValue("Z"))){
				//A����ΪB,ASCII��+1
				char endch = start.charAt(0);
				endch = (char) (endch + 1);
				end = String.valueOf(endch);
				end = getValue(end);
				start = getValue(start);
				
				query = "SELECT bookno, bookname, publisher, author FROM book WHERE " +
						"CONVERT( bookname USING gbk ) COLLATE gbk_chinese_ci >='" + start +"' " +
						"AND CONVERT( bookname USING gbk ) COLLATE gbk_chinese_ci < '" + end + "'";
			}else{
				query = "SELECT bookno, bookname, publisher, author FROM book WHERE " +
						"CONVERT( bookname USING gbk ) COLLATE gbk_chinese_ci >='" + start +"' " ;
				System.out.println(query);
			}
			
			ResultSet ret = s.executeQuery(query);
			// ��ȡ�鼮��Ϣ
			while (ret.next()) {  
				BookInCategory book = new BookInCategory();
				book.setBookno(ret.getString(1));
				book.setBookname(ret.getString(2));
				book.setPublisher(ret.getString(3));
				book.setAuthor(ret.getString(4));
				books.add(book);
	           }
	           con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return books;
		}
}
