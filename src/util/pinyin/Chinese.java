package util.pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class Chinese {

	private static HanyuPinyinOutputFormat format = null;
	private static String[] pinyin;

	public Chinese() {
		format = new HanyuPinyinOutputFormat();
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		pinyin = null;
	}

	// ת�����������ַ�
	public String getCharacterPinYin(char c) {
		try {
			pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		// ���c���Ǻ��֣�����null
		if (null == pinyin)
			return null;

		// ������ȡ��һ��ֵ
		return pinyin[0];
	}

	// ת��һ���ַ���
	public String getStringPinYin(String str) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < str.length(); ++i) {
			String tmp = getCharacterPinYin(str.charAt(i));
			if (null == tmp) {
				// ���str.charAt(i)���Ǻ��֣��򱣳�ԭ��
				sb.append(str.charAt(i));
			} else {
				sb.append(tmp);
				// �ִ�
				if (i < str.length() - 1
						&& null != getCharacterPinYin(str.charAt(i + 1))) {
					sb.append("\\\'");//������ݿⵥ��������
				}
			}
		}
		return sb.toString().trim();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Chinese chinese = new Chinese();
		String str = "�������ܳ���֮��";
		str = "1945�������";
		String pinYin = chinese.getStringPinYin(str);
		System.out.println(pinYin);
		
	}
}
