package util.array;

import java.util.Collection;
import java.util.List;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 10:16 上午
 */
public class ArrayUtil {


    /**
     * @param src       数据源
     * @param offsetSrc 偏移值
     * @param des       目标
     * @param offsetDec 目标偏移值
     * @param len       复制长度
     */
    public static void arraysCopy(Object[] src, int offsetSrc, String[] des, int offsetDec, int len) {
        for (int i = 0; i < len; i++) {
            des[offsetDec + i] = src[offsetSrc + i].toString();
        }
    }

    /**
     * @param src   list类型的数据
     * @param offsetSrc 偏移值
     * @param des
     * @param offsetDes
     * @param len
     */
    public static void listToArray(List src, int offsetSrc, String[] des, int offsetDes, int len) {
        for (int i = 0; i < len; i++) {
            des[offsetDes + i] = src.get(offsetSrc + i).toString();
        }
    }

    public static void listToArray(List src, String[] des, int offsetDes) {
        ArrayUtil.listToArray(src, 0, des, offsetDes, src.size());
    }

    public static void arrayToCollection(Collection<String> collection, String[] array, int offset, int len){
        if(collection == null || array == null){
            throw new IllegalArgumentException("参数不合法");
        }
        for(int i = 0; i<len;i++){
            collection.add(array[offset+1]);
        }
    }

    public static void arrayToCollection(Collection<Integer> collection, Integer[] array, int offset, int len){
        if(collection == null || array == null){
            throw new IllegalArgumentException("参数不合法");
        }
        for(int i = 0; i<len;i++){
            collection.add(array[offset+1]);
        }
    }
}
