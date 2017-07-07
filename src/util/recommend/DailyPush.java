package util.recommend;

import po.book.BookDetailInfo;
import po.message.News;
import po.user.AccessToken;
import util.remind.MyTask;
import util.sql.SQLUtil;
import util.weixin.MessageUtil;
import util.weixin.WeixinUtil;

public class DailyPush {

	/**
	 * ���Ϳͷ�ͼ����Ϣ����
	 * @param weid
	 * @param bookno
	 * @param bookname
	 * @param bookimg
	 * @param url
	 * @param myabstract
	 */
	public static void sendImgTxtMsg(String weid, int bookno, String bookname, String bookimg, String url, String myabstract){
		AccessToken access_token = WeixinUtil.getAccessToken();
		News news = new News();
		news.setTitle("�����Ƽ���" + "��" + bookname + "��");
		news.setPicUrl(bookimg);
		news.setDescription(myabstract);
		news.setUrl(url);
		String JSONMessageImg2 = MessageUtil.generateServiceImgTxtMsg(weid, "news", news);
		int errcode = WeixinUtil.Cus_Service(access_token.getToken(), JSONMessageImg2);
		System.out.println("recommend_errcode:"+errcode);
	}
	
	
	/**
	 * ����ͼ��ţ�weid����ͼ����Ϣ
	 * @param bookno
	 * @param weid
	 */
	public static void recomBookDaily(int bookno, String weid){
		String bookNo = String.valueOf(bookno);
		BookDetailInfo book = SQLUtil.querySingleBookFromCat(bookNo);
		String bookname = book.getBookname(); // "���û��������";
		String bookimg = book.getBookimg(); // "http://apis.juhe.cn/goodbook/img/ab7f47d54f7cc742642c6cb058207acd.jpg";
		String url = "http://www.iotesta.com.cn/library/show_singleItem.action?bookno="+bookno;
		String myabstract = book.getBookAbstract();//"�������������������������������û�������㡷";
		DailyPush.sendImgTxtMsg(weid, bookno, bookname, bookimg, url, myabstract);
	}
	
	public static void main(String[] args) {
		int bookno = (int)(Math.random()*600);
		System.out.println(bookno);
		recomBookDaily(bookno,"otE_a1Ep3DV9r4kbFu7LDTTXhK6A");
	}
}
