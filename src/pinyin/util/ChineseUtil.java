package pinyin.util;

public class ChineseUtil {

	// �ж�һ���ַ��Ƿ�������  
    public static boolean isChinese(char c) {  
        return c >= 0x4E00 &&  c <= 0x9FA5;// �����ֽ����ж�  
    }  
    // �ж�һ���ַ����Ƿ�������  
    public static boolean isChinese(String str) {  
        if (str == null) return false;  
        for (char c : str.toCharArray()) {  
            if (isChinese(c)) return true;// ��һ�������ַ��ͷ���  
        }  
        return false;  
    }  
    
    public static void main(String[] args) {
		if(isChinese("a����")){
			System.out.println("�ж�������");
		}else{
			System.out.println("�жϲ�������");
		}
	}
}
