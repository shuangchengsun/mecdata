package util.csv;

import com.csvreader.CsvWriter;
import util.common.Encoding;
import util.array.ArrayUtil;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class CSVUtil {
    /**
     * @param filePath 写入的文件名
     * @param encoding 编码方式
     * @param data     待写入的数据（不包含id）
     * @param head     标题头(不包含id)
     */
    public static void writeCSV(String filePath, Encoding encoding, List<List<Object>> data, String[] head) throws CSVException {
        Charset charset = Charset.forName(encoding.getEncoding());
        CsvWriter writer = new CsvWriter(filePath, ',', charset);

        int len = head.length;

        try {
            String[] writeData = new String[len + 1];
            writeData[0] = "id";
            System.arraycopy(head, 0, writeData, 1, len);
            writer.writeRecord(writeData);
            int index = 0;
            for (List<Object> list : data) {
                if (len != list.size()) {
                    throw new CSVException(CSVErrorEnum.LENGTH_NOT_MATCH_ERROR);
                }
                writeData[0] = String.valueOf(++index);
                ArrayUtil.listToArray(list, 0, writeData, 1, len);

                writer.writeRecord(writeData);
            }

        } catch (IOException exception) {
            throw new CSVException(CSVErrorEnum.IO_FILED_ERROR, exception);
        } finally {
            writer.close();
        }


    }

    public static void writeCSV(String filePath, Encoding encoding, Object[] data, String[] head) {
        Charset charset = Charset.forName(encoding.getEncoding());
        CsvWriter writer = new CsvWriter(filePath, ',', charset);

        int len = head.length;

        if (len != data.length) {
            throw new CSVException(CSVErrorEnum.LENGTH_NOT_MATCH_ERROR);
        }


        try {
            writer.writeRecord(head);

            String[] writeData = new String[len];
            ArrayUtil.arraysCopy(data,0,writeData,0,data.length);

            writer.writeRecord(writeData);


        } catch (IOException exception) {
            throw new CSVException(CSVErrorEnum.IO_FILED_ERROR, exception);
        } finally {
            writer.close();
        }
    }
}
