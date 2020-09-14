package util.csv;

import util.common.Encoding;

import java.io.IOException;
import java.util.List;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-09 4:11 下午
 */
public interface CSVService {

    CSVWorker getWorker(String file, Encoding encoding);

    void closeWorker(String file);

    Object readValue(String file, String key);

    Object[] readLine(String file);

    void readRecord(String file);

    void setEncoding(String file, Encoding encoding);

    boolean writeLine(String file, Object[] data, String[] header, Encoding encoding, boolean isAppend);

    boolean writeLines(String file, List<Object[]> data, String[] header, Encoding encoding, boolean isAppend);

    Object[] getHead(String file) throws IOException;
}
