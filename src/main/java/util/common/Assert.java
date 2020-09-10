package util.common;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 9:01 下午
 */
public class Assert {

    public static void assertNotNull(Object str){
        if(str == null){
            throw new AssertException("assert not null error");
        }
    }

    public static void assertNull(Object str){
        if(str != null){
            throw new AssertException("assert null error");
        }
    }

    public static void assertEquals(Object o1,Object o2){
        if(!o1.equals(o2)){
            throw new AssertException("assert equals error");
        }
    }

    public static void assertNotEquals(Object o1,Object o2){
        if(o1.equals(o2)){
            throw new AssertException("assert not equals error");
        }
    }
}
