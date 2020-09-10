package util.csv.impl;

import util.common.Encoding;
import util.csv.CSVService;
import util.csv.CSVWorker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-09 4:13 下午
 */
public class CSVServiceImpl implements CSVService {

    private final Map<String, CSVWorker> workerMap = new ConcurrentHashMap<>();

    @Override
    public CSVWorker getWorker(String file, Encoding encoding) {
        workerMap.computeIfAbsent(file, new Function<String, CSVWorker>() {
            @Override
            public CSVWorker apply(String s) {
                try {
                    return new CSVWorkerImpl(s,encoding.getEncoding());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        return workerMap.get(file);
    }

    @Override
    public void closeWorker(String file) {
        //线程安全问题
        CSVWorker worker = workerMap.get(file);
        if (worker != null) {
            try {
                worker.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object readValue(String file, String key) {
        CSVWorker worker = workerMap.get(file);
        if (worker == null) {
            worker = getWorker(file,Encoding.UTF_8);
        }
        try {
            return worker.getValue(key, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void readRecord(String file) {
        CSVWorker worker = workerMap.get(file);
        if (worker == null) {
            worker = getWorker(file,Encoding.UTF_8);
        }
        try {
            worker.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object[] readLine(String file) {
        CSVWorker worker = workerMap.get(file);
        Object[] data = null;
        if (worker == null) {
            worker = getWorker(file,Encoding.UTF_8);
        }
        try {
            data = worker.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public boolean writeLine(String file, Object[] data, String[] header, Encoding encoding, boolean isAppend) {
        CSVWorker worker = workerMap.get(file);
        if (worker == null) {
            worker = getWorker(file,encoding);
        }
        return worker.writeLine(data, header, isAppend);
    }

    @Override
    public boolean writeLines(String file, List<Object[]> data, String[] header, Encoding encoding, boolean isAppend) {
        CSVWorker worker = workerMap.get(file);
        if(worker == null){
            worker = getWorker(file,encoding);
        }
        for (Object[] objects : data) {
            if (!worker.writeLine(objects, header, isAppend)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setEncoding(String file, Encoding encoding) {

    }
}
