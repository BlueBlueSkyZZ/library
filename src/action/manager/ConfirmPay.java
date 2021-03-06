package action.manager;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import util.EncryptUtil;
import util.sql.SQL4PersonalInfo;

import com.opensymphony.xwork2.ActionSupport;

public class ConfirmPay extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	public void setStatus() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		String subscribenum = request.getParameter("num");	// 安卓端传来解析二维码后的信息
		// 解密
		byte[] decryptFrom = EncryptUtil.parseHexStr2Byte(subscribenum);
		byte[] decryptResult = EncryptUtil.decrypt(decryptFrom); 
		String result = new String(decryptResult);
		String[] datas = result.split(",");
		
		String type = datas[0];
		if(type.equals("borrow")){
			SQL4PersonalInfo.setBorrowConfirm(datas[1]);	// 提取订单号并设置
		}else if(type.equals("return")){
			SQL4PersonalInfo.setReturnConfirm(datas[1], datas[2]);  // 提取weid和bookno
		}
			
	}
	
}
