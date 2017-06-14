package recom.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.struts2.ServletActionContext;

import po.BookInShoppingcart;

import util.SQL4PersonalInfo;
import util.SQLUtil;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class RecomUtil extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override 
	public String execute() throws Exception {
		
		return SUCCESS;
	}
	
	public void recomOutput() throws ClassNotFoundException, TasteException, IOException{
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        int userid = 2;
       // if(request.getParameter("weid").isEmpty()){
        if(request.getParameter("weid") == null){
        	userid = 15;
        }else{
        	//String weid = request.getSession().getAttribute("weid").toString();
        	String weid = request.getParameter("weid");
        	userid = Integer.parseInt(SQLUtil.getUserId(weid));//stringתint
        }
        int bookid = recomResult(userid, 10);
        
        PrintWriter pw = response.getWriter();
        pw.write(String.valueOf(bookid));
        pw.flush();
        pw.close();
        
	}

	/**
	 * 
	 * @param user_id �Ƽ����û���
	 * @param number �����û����Ƽ�����
	 * @return ��number�����������Ƽ�һ��
	 * @throws ClassNotFoundException
	 * @throws TasteException
	 */
	private int recomResult(int user_id, int number) throws ClassNotFoundException, TasteException {
        
		// (1)----�������ݿⲿ��
		Class.forName("com.mysql.jdbc.Driver");
		MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource(); // ��Ҫ�������ݿ��maven�����ݿ����ӳض���
		String jdbcUrl = "jdbc:mysql://localhost:3306/library?serverTimezone=UTC";
		dataSource.setServerName("127.0.0.1");
		dataSource.setUser("root");
		dataSource.setPassword("root");
		dataSource.setDatabaseName("library");
		dataSource.setUrl(jdbcUrl);
		// (2)----ʹ��MySQLJDBCDataModel����Դ��ȡMySQL�������
		// JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource,
		// "interest", "userid", "bookid", "preference",null);
		JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource,
				"taste_interest", "user_id", "book_id", "preference", null);

		// (3)----����ģ�Ͳ���
		// ��MySQLJDBCDataModel����ֵ��DataModel
		// DataModel model = dataModel;

		// ����ReloadFromJDBCDataModel����jdbcDataModel,���԰���������ڴ���㣬�ӿ�����ٶȡ�
		ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(dataModel);

		// �û����ƶ�UserSimilarity:���������Զ������ھӲ���
		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		// �����û�UserNeighborhood
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(number,
				similarity, model);
		// һ��ȷ�������û�,һ����ͨ��user-based�Ƽ���������,����һ��GenericUserBasedRecommender�Ƽ�����Ҫ����ԴDataModel,�û�������UserSimilarity,�����û����ƶ�UserNeighborhood
		Recommender recommender = new GenericUserBasedRecommender(model,
				neighborhood, similarity);
		// ���û�userid�Ƽ�5����Ʒ//ע���õ��Ƽ��Ŵ�0��ʼ
		List<RecommendedItem> recommendations = recommender.recommend(user_id, number);

		System.out.println("userid:" + user_id);
		System.out.println("recommendNumber:" + number);
		int bookid = 2;//���ݿ���Ϊbigint
		int count = 0;
		List<Integer> books = new ArrayList<Integer>();
		for (RecommendedItem recommendation : recommendations) {
			// ����Ƽ����
			System.out.println(recommendation.getItemID());
			//bookid = (int) recommendation.getItemID();
			books.add((int) recommendation.getItemID());
			count++;
		}
		System.out.println("booksLength:" + books.size());
		int ran = (int)(Math.random() * count);
		bookid = books.get(ran-1);
		
		System.out.println("bookid:" + bookid);
		
		return  bookid;
		//return 0;
	}
}
