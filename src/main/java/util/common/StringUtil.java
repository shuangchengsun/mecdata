package util.common;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 4:02 下午
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        return str.length() <=0;
    }

    public static boolean isNull(String str) {
        return str == null;
    }

    public static boolean isBlank(String str){
        char[] chars = str.toCharArray();
        for(char ch:chars){
            if(ch !=' '){
                return false;
            }
        }
        return true;
    }
}
