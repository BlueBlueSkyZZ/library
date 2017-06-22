package action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import po.UserDetailInfo;
import util.SQLUtil;
import util.VericodeUtil;

import com.opensymphony.xwork2.ActionSupport;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class FinishReg extends ActionSupport {
	private static final long serialVersionUID = 1L;

	public String GetVeriCode() throws IOException {
		System.out.println("ִ��getvericode����");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");
		PrintWriter pw = response.getWriter();

		String weID = request.getParameter("weID");
		String tel = request.getParameter("tel");

		String str = "ǰ̨�����˲�����weID=" + weID + "��tel=" + tel;

		System.out.println(str);
		
		String vericode = VericodeUtil.sendSMS(tel);
		
		pw.write(vericode);
		System.out.println(vericode);
		pw.flush();
		pw.close();
		return SUCCESS;
	}

	public String CompleteReg() throws IOException {
		// System.out.println("ִ��completereg����");
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		HttpServletRequest request = ServletActionContext.getRequest();
		request.setCharacterEncoding("utf-8");

		// ��ȡ�û������Ϣ
		String weID = request.getParameter("weID"); // ΢��ID
		String name = request.getParameter("name");
		String IDNumber = request.getParameter("IDNumber");
		String tel = request.getParameter("tel");
		String veriCode = request.getParameter("veriCode");
		// ����û��������Ϣ�����ݿ�
		SQLUtil.addUserRealInfo("library", name, IDNumber, tel, weID);

		// String str = "ǰ̨�����˲�����weID=" + weID + "��name=" + name + "��IDNumber="
		// + IDNumber + "��tel=" + tel + "��veriCode=" + veriCode;
		// PrintWriter pw = response.getWriter();
		// pw.print(str);
		// System.out.println(str);
		// pw.flush();
		// pw.close();
		return SUCCESS; // ע��ɹ����棨�������첽�������Դ˴�����ת��Ч��
	}

	public String RegSuccess() {
		return SUCCESS;
	}

	public String RegFailure() {
		return SUCCESS;
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

}
