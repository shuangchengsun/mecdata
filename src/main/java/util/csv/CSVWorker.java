package util.csv;

import java.io.IOException;
import java.util.List;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-09 3:33 下午
 */
public interface CSVWorker {

    /**
     * 返回csv文件头
     *
     * @return
     */
    String[] getHeader() throws IOException;

    /**
     * 返回一行
     *
     * @return
     */
    Object[] readLine() throws IOException;

    /**
     * 读取从start开始的len行
     *
     * @param start 开始行
     * @param len   长度
     * @return
     */
    List<Object[]> readLines(int start, int len) throws IOException;

    /**
     * 返回此一行中的某个数据
     *
     * @param key      head
     * @param readNext 是否读取下一行
     * @return
     */
    Object getValue(String key, boolean readNext) throws IOException;

    /**
     * @param data     一行数据
     * @param isAppend 是否在文件末尾追加
     * @return
     */
    boolean writeLine(Object[] data, String[] header, boolean isAppend);

    /**
     * 写入多行数据
     *
     * @param data     多行数据
     * @param isAppend 是否在文件末尾追加
     * @return
     */
    boolean writeLines(List<Object[]> data, String[] header, boolean isAppend);

    boolean writeHeader(String[] header);

    /**
     * 显式的回收资源
     */
    void close() throws IOException;
}
