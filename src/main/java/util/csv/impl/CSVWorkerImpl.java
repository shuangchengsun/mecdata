package util.csv.impl;

import util.csv.CSVWorker;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-09 3:42 下午
 */
public class CSVWorkerImpl implements CSVWorker {

    private String file = null;

    private BufferedReader reader = null;

    private RandomAccessFile outputStream = null;

    private String[] header;

    private Object[] currentLine;

    private boolean isHeaderWrite = false;

    private String encoding = "UTF-8";

    public CSVWorkerImpl(String file, String encoding) throws IOException {
        this.file = file;
        File file1 = new File(file);
        if(!file1.exists()){
            file1.createNewFile();
        }
        this.encoding = encoding;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
        reader = new BufferedReader(new InputStreamReader(inputStream,encoding));
        outputStream = new RandomAccessFile(file, "rw");
    }

    public CSVWorkerImpl(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public String[] getHeader() throws IOException {
        if (header == null) {
            String s = reader.readLine();
            header = s.split(",");
        }
        return header;
    }

    @Override
    public Object[] readLine() throws IOException {
        if (header == null) {
            getHeader();
        }
        String s = reader.readLine();
        Object[] objects = s.split(",");
        currentLine = objects;
        return objects;
    }

    @Override
    public List<Object[]> readLines(int start, int len) throws IOException {
        if (header == null) {
            getHeader();
        }
        throw new RuntimeException("method not support yet");
    }

    @Override
    public Object getValue(String key, boolean readNext) throws IOException {
        if (readNext) {
            readLine();
        }
        if (currentLine == null) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < currentLine.length; i++) {
            if (header[i].equals(key)) {
                index = i;
                break;
            }
        }
        return currentLine[index];
    }

    @Override
    public boolean writeLine(Object[] data, String[] header, boolean isAppend) {
        if(!isHeaderWrite){
            writeHeader(header);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Object object : data) {
            stringBuilder.append(object);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("\n");
        String message = stringBuilder.toString();
        try {
            if (isAppend) {
                outputStream.seek(outputStream.length());
            }
            outputStream.write(message.getBytes(Charset.forName(encoding)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean writeLines(List<Object[]> data,String[] header, boolean isAppend) {
        boolean res = true;
        for (Object[] objects : data) {
            if (!writeLine(objects, header,isAppend)) {
                res = false;
                break;
            }
        }
        return res;
    }

    @Override
    public boolean writeHeader(String[] header) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String object : header) {
            stringBuilder.append(object);
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("\n");
        String message = stringBuilder.toString();
        try {
            outputStream.write(message.getBytes(Charset.forName(encoding)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isHeaderWrite = true;
        return true;

    }

    @Override
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
        if (outputStream != null) {
            outputStream.close();
            outputStream = null;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (reader != null) {
            reader.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        super.finalize();
    }
}
