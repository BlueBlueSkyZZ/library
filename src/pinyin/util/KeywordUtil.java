package pinyin.util;

public class KeywordUtil {

	public static String keywordToPinyin(String keyword) {
		if (ChineseUtil.isChinese(keyword)) {
			// ����תƴ���ִ�
			Chinese chinese = new Chinese();
			
			keyword = chinese.getStringPinYin(keyword);
		} else {
			// ƴ���ִ�
			keyword = PinyinUtils.split(keyword);
		}
		return keyword;
	}

	public static void main(String[] args) {
		System.out.println(keywordToPinyin("xiaogouqianqian"));
	}
}
