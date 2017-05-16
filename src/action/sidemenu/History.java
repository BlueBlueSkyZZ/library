package action.sidemenu;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import util.SQL4PersonalInfo;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class History extends ActionSupport{
	private static final long serialVersionUID=1L;
	
	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String weid = request.getParameter("weid");
		
		Map<String, HashMap<String,Integer>> info = new HashMap<String, HashMap<String,Integer>>();
		info = SQL4PersonalInfo.getPersonalBookInfo(weid);
		
		Set<String> mapKey = info.keySet();
		for(String str:mapKey){
			// ��������ı�
			if(str.equals("cat")){
				createCat(info.get(str));
			}
			// ��ȷ����ı�
			else if(str.equals("year")){
				createYear(info.get(str));
			}
			// �¶ȷ����ı�
			else if(str.equals("month")){
				createMonth(info.get(str));
			}
		}
		
		return SUCCESS;
	}
	
	
	private void createCat(HashMap<String,Integer> bookcat) throws IOException{
		StringBuffer sb = new StringBuffer();
		// ����json��
		sb.append("{");
		sb.append("\"data\":[");
		sb.append("{\"value\":" + bookcat.get("a") + ",\"name\":\"��ѧ����ʷ\"},");
		sb.append("{\"value\":" + bookcat.get("b") + ",\"name\":\"��ѧ������\"},");
		sb.append("{\"value\":" + bookcat.get("c") + ",\"name\":\"��ᡢ��־\"},");
		sb.append("{\"value\":" + bookcat.get("d") + ",\"name\":\"�Ƽ�\"}");
		sb.append("]");
		sb.append("}");
		
		// ��json��д�뱾�ص�txt�У���ǰ̨��ȡ
		final String filename = "C:\\cat.txt";  
		FileWriter fw = new FileWriter(filename);
		BufferedWriter bw = new BufferedWriter(fw);
		// ��json��д���ı���
		bw.write(sb.toString());
		bw.newLine();
		bw.flush();
		bw.close();
		fw.close();
		
//		ActionContext context = ActionContext.getContext();
//		context.put("bookcat", sb.toString());
		
	}

	
	private void createYear(HashMap<String,Integer> bookYear){
		
	}
	
	
	private void createMonth(HashMap<String,Integer> bookMonth){
		
	}
	
	
}
