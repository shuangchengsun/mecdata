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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
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


    public static void getAppNameAndCount() {
        String file = "/Users/sunshuangcheng/source/python/MECPaper/resource/appNameAndContent.csv";
        CSVService csvService = new CSVServiceImpl(Encoding.GBK);
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Set<String>> map1 = new HashMap<>();

        while (csvService.readLine(file).length > 0) {
            Object app_name = csvService.readValue(file, "app_name");
            String userName = (String) csvService.readValue(file, "user_id");
            Integer num = map.get(app_name.toString());
            Set<String> set = map1.get(app_name.toString());
            if (num == null) {
                num = 0;
            }
            num++;

            if (set == null) {
                set = new HashSet<>();
            }
            set.add(userName);
            map1.put(app_name.toString(), set);
            map.put(app_name.toString(), num);
        }


        String savePath = "/Users/sunshuangcheng/source/java/mecdata/src/main/resources/data/appName.csv";
        String[] head = {"id", "appName", "count", "userCount"};
        Object[] data = new Object[4];
        long index = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            data[1] = entry.getKey();
            data[2] = entry.getValue();
            if ((int) data[2] < 100) {
                continue;
            }
            data[3] = map1.get(data[1]).size();
            if ((int) data[3] < 100) {
                continue;
            }
            data[0] = index++;
            csvService.writeLine(savePath, data, head, Encoding.UTF_8, true);
        }

    }


    public static void subFile() throws IOException {
        String filePath = "/Users/sunshuangcheng/source/python/MECPaper/resource/";
        String path = "/Users/sunshuangcheng/source/java/mecdata/src/main/resources/data/";
        CSVService service = new CSVServiceImpl();
        CSVWorker reader = service.getWorker(filePath + "appNameAndContent.csv", Encoding.GBK);
        String[] header = reader.getHeader();
        Object[] data = reader.readLine();
        while (data.length > 0) {
            Object user_id = reader.getValue("user_id", false);
            int i = user_id.hashCode();
            int slot = i & 15;
            String fileName = "dataSubFile_" + slot + ".csv";
            service.writeLine(path + fileName, data, header, Encoding.UTF_8, true);
            data = reader.readLine();
        }
        System.out.println("文件拆分完毕，----按照user_id哈希值拆分");
    }

    public static void uploadData() throws IOException {
        String path = "/Users/sunshuangcheng/source/java/mecdata/src/main/resources/data/";
        CSVService service = new CSVServiceImpl();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        AtomicInteger integer = new AtomicInteger(0);
        for (int i = 0; i < 16; i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    int num = integer.getAndAdd(1);
                    String fileName = "dataSubFile_" + num + ".csv";
                    System.out.println(fileName + " is uploading");
                    CSVWorker worker = service.getWorker(path + fileName, Encoding.UTF_8);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    DataBaseService dataBaseService = DataBaseServiceFactory.getDataBaseService();
                    try {
                        ItemModel model = new ItemModel();
                        String[] data = (String[]) worker.readLine();
                        while (data.length > 0) {
                            model.setUserid(data[0]);
                            model.setAppname(data[2]);
                            model.setStartTime(simpleDateFormat.parse(data[3]));
                            model.setLongitude(Double.parseDouble(data[8]));
                            model.setLatitude(Double.parseDouble(data[9]));
                            dataBaseService.insertObject(model, "oridata");
                            data = (String[]) worker.readLine();
                        }
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(task);
            System.out.println("task num:  " + i + " is running");
        }

    }

    public static void uploadSingleThread(){
        String file = "/Users/sunshuangcheng/source/python/MECPaper/resource/appNameAndContent.csv";
        BlockingQueue<ItemModel> queue = new LinkedBlockingDeque<>(256);

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        for (int i = 0; i < 32; i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    DataBaseService dataBaseService = DataBaseServiceFactory.getDataBaseService();
                    ItemModel model = queue.poll();
                    while(model != null) {
                        dataBaseService.insertObject(model, "oridata");
                        model = queue.poll();
                    }
                }
            };
            executorService.execute(task);
        }
        CSVService service = new CSVServiceImpl(Encoding.GBK);
        String[] data = (String[]) service.readLine(file);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        try {
            while (data.length > 0) {
                ItemModel model = new ItemModel();
                model.setUserid(data[0]);
                model.setAppname(data[2]);

                model.setStartTime(simpleDateFormat.parse(data[3]));

                model.setLongitude(Double.parseDouble(data[8]));
                model.setLatitude(Double.parseDouble(data[9]));

                queue.put(model);
                data = (String[]) service.readLine(file);
            }
        } catch (ParseException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finished");

    }
}
