package dataprocess;

import com.csvreader.CsvReader;
import model.ItemModel;
import util.common.Encoding;
import util.csv.CSVService;
import util.csv.CSVUtil;
import util.csv.CSVWorker;
import util.csv.impl.CSVServiceImpl;
import util.database.DataBaseService;
import util.database.DataBaseServiceFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-10 2:57 下午
 */
public class DataClean {

    public static void getAppName() throws IOException {
        String filePath = "/Users/sunshuangcheng/source/python/MECPaper/resource/appNameAndContent.csv";
        CsvReader reader = new CsvReader(filePath, ',', Charset.forName("GBK"));
        reader.readHeaders();
        String[] headers = reader.getHeaders();
        reader.readRecord();
        Set<String> set = new HashSet<>();
        List<List<Object>> lists = new LinkedList<>();
        while (reader.readRecord()) {
            String name = reader.get("app_name");
            if (!set.contains(name)) {
                set.add(name);
                List<Object> list = new ArrayList<>();
                list.add(name);
                lists.add(list);
            }
        }

        String savePath = "/Users/sunshuangcheng/source/java/mecdata/src/main/resources/data/appName.csv";
        String[] head = {"appName"};
        try {
            CSVUtil.writeCSV(savePath, Encoding.GBK, lists, head);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("size: " + set.size());
    }



    public static void subFile() throws IOException {
        String filePath = "/Users/sunshuangcheng/source/python/MECPaper/resource/";
        String path = "/Users/sunshuangcheng/source/java/mecdata/src/main/resources/data/";
        CSVService service = new CSVServiceImpl();
        CSVWorker reader = service.getWorker(filePath + "appNameAndContent.csv",Encoding.GBK);
        String[] header = reader.getHeader();
        Object[] data = reader.readLine();
        while(data.length>0){
            Object user_id = reader.getValue("user_id", false);
            int i = user_id.hashCode();
            int slot = i & 15;
            String fileName = "dataSubFile_"+slot+".csv";
            service.writeLine(path+fileName,data,header,Encoding.UTF_8,true);
            data = reader.readLine();
        }
        System.out.println("文件拆分完毕，----按照user_id哈希值拆分");
    }

    public static void uploadData() throws IOException {
        String path = "/Users/sunshuangcheng/source/java/mecdata/src/main/resources/data/";
        CSVService service = new CSVServiceImpl();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        AtomicInteger integer = new AtomicInteger(0);
        for(int i =0;i<16;i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    int num = integer.getAndAdd(1);
                    String fileName  = "dataSubFile_"+num+".csv";
                    System.out.println(fileName+" is uploading");
                    CSVWorker worker = service.getWorker(path+fileName,Encoding.UTF_8);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    DataBaseService dataBaseService= DataBaseServiceFactory.getDataBaseService();
                    try {
                        ItemModel model = new ItemModel();
                        String[] data = (String[]) worker.readLine();
                        while(data.length>0){
                            model.setUserid(data[0]);
                            model.setAppname(data[2]);
                            model.setStartTime(simpleDateFormat.parse(data[3]));
                            model.setLongitude(Double.parseDouble(data[8]));
                            model.setLatitude(Double.parseDouble(data[9]));
                            dataBaseService.insertObject(model,"oridata");
                            data = (String[]) worker.readLine();
                        }
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(task);
            System.out.println("task num:  "+i+" is running");
        }

    }
}
