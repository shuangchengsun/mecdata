package util.common;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-08 9:47 下午
 */
public class BeanUtil {

    public static String getGetMethod(String field) {
        return "get" +
                field.substring(0, 1).toUpperCase() +
                field.substring(1);
    }

    public static String getSetMethod(String field) {
        return "set" +
                field.substring(0, 1).toUpperCase() +
                field.substring(1);

    }
}
