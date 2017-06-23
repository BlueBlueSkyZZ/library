package action.manager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import util.EncryptUtil;
import util.SQL4PersonalInfo;

import com.opensymphony.xwork2.ActionSupport;

public class ConfirmPay extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	public void setStatus() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String subscribenum = request.getParameter("num");	// ��׿�˴���������ά������Ϣ
		// ����
		byte[] decryptFrom = EncryptUtil.parseHexStr2Byte(subscribenum);
		byte[] decryptResult = EncryptUtil.decrypt(decryptFrom); 
		String result = new String(decryptResult);
		String[] datas = result.split(",");
		
		String type = datas[0];
		if(type.equals("borrow")){
			SQL4PersonalInfo.setBorrowConfirm(datas[1]);	// ��ȡ�����Ų�����
		}else if(type.equals("return")){
			SQL4PersonalInfo.setReturnConfirm(datas[1], datas[2]);  // ��ȡweid��bookno
		}
			
	}
	
}
