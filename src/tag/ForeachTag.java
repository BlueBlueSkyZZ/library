package tag;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ForeachTag extends SimpleTagSupport{
	
	private Object items;
	private String var;
	private Collection collection;

	//ͳһתΪ���м���
	public void setItems(Object items) {
		this.items = items;
		//�ж�items������
		if(items instanceof Collection){  //list set
			collection = (Collection) items;
		}
		
		if(items instanceof Map){
			Map map = (Map) items;
			collection = map.entrySet();  // set
		}
		/*
		if(items instanceof Object[]){
			Object obj[] = (Object[]) items;
			collection = Arrays.asList(obj);
		}
		
		if(items instanceof int[]){
			int arr[] = (int[])items;
			this.collection = new ArrayList();
			for(int i:arr){
				this.collection.add(i);
			}
		}
		*/
		
		// �ж��Ƿ�Ϊ����
		if(items.getClass().isArray()){
			this.collection = new ArrayList();
			// ����(��Array�ɲ����������͵�����) ***********************************************
			int length = Array.getLength(items);  
			for(int i=0;i<length;i++){
				Object value = Array.get(items, i);  // ��ȡ����items�е�i��λ�õ�Ԫ��
				this.collection.add(value);  
			}
			//******************************************************************************
		}
		
		
	}
	
	public void setVar(String var) {
		this.var = var;
	}
	
	@Override
	public void doTag() throws JspException, IOException {

		Iterator it = this.collection.iterator();
		while(it.hasNext()){
			Object value = it.next();
			
			// �����ݴ������к���jsp���
			this.getJspContext().setAttribute(var, value);
			this.getJspBody().invoke(null);
		}
		
	}
	
	
}
