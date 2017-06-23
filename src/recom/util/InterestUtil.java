package recom.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import util.SQLUtil;
import util.WeixinUtil;

/**
 * �����û�������Ϊ�ķ���
 * @author mzy
 *
 */
public class InterestUtil {
	
	/**
	 * �û����ϲ��֮�󣬶��Ȿ����û�ϲ����+3
	 * @param weid �����weidΪ�ַ���
	 * @param book_id �����ͼ��id
	 * @param like ��־�����ϲ��������ȡ��ϲ��
	 */
	public static void clickLike(String weid, String book_id, boolean like){
		String user_id = SQLUtil.getUserId(weid);//��weid��ȡuser_id
		float preference = 0;
		if(like){
			preference = 3;
		}else{
			preference = -3;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://" + WeixinUtil.MYSQL_DN , WeixinUtil.MYSQL_NAME, WeixinUtil.MYSQL_PASSWORD);
			Statement s = con.createStatement();
			//���ݿ���Ϊint���Բ���Ҫ������
			String query = "update taste_interest set preference = (preference+" + preference +") " +
					"where user_id= " + user_id + 
					" and book_id= " + book_id;
			System.out.println(query);
			s.executeUpdate(query);
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * �������ͼ��ʱ���Ը�ͼ���ϲ����+0.5
	 * @param weid �����û���weid
	 * @param book_id ����ͼ��id
	 */
	public static void clickBook(String weid, String book_id){
		String user_id = SQLUtil.getUserId(weid);//��weid��ȡuser_id
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://" + WeixinUtil.MYSQL_DN , WeixinUtil.MYSQL_NAME, WeixinUtil.MYSQL_PASSWORD);
			Statement s = con.createStatement();
			//���ݿ���Ϊint���Բ���Ҫ������,�������򴴽�����������
			String query1 = "insert ignore into taste_interest (user_id, book_id, preference) values (" + 
			user_id +"," + book_id + ", 0 )";
			
			
			String query2 = "update taste_interest set preference = (preference+0.5) " +
					"where user_id= " + user_id + 
					" and book_id= " + book_id;
			System.out.println("query1:" + query1);
			System.out.println("query2:" + query2);
			s.executeUpdate(query1);
			s.executeUpdate(query2);
            con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		InterestUtil.clickLike("oDRhGwipEfLgIMjPB-ZywcisFVxk", "31", false);
		
		//InterestUtil.clickBook("oDRhGwipEfLgIMjPB-ZywcisFVxk", "60");
	}
}
