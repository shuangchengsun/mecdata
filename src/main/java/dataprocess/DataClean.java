package dataprocess;

import com.csvreader.CsvReader;
import model.ItemModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.common.Encoding;
import util.csv.CSVService;
import util.csv.CSVUtil;
import util.csv.CSVWorker;
import util.csv.impl.CSVServiceImpl;
import util.database.DataBaseService;
import util.database.DataBaseServiceFactory;
import util.database.exception.DataBaseException;
import util.log.LoggerUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-10 2:57 下午
 */
public class DataClean {
    private static final AtomicInteger signal = new AtomicInteger(0);

    private static final Logger LOGGER = LoggerFactory.getLogger("defaultFileLogger");

    private static final int idleTime = 5 * 60 * 1000;

    private static final int busyTime = 20 * 60 * 1000;

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
                            boolean res = dataBaseService.insertObject(model, "oridata");
                            if (!res) {
                                return;
                            }
                            data = (String[]) worker.readLine();
                        }
                    } catch (IOException | ParseException | DataBaseException e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(task);
            System.out.println("task num:  " + i + " is running");
        }

    }

    /**
     * 单线程读文件，多线程写数据库.
     */
    public static void uploadSingleThread() {
//        String file = "E:\\source\\java\\mecdata\\src\\main\\resources\\ori.csv";
        String file = "ori.csv";
        BlockingQueue<ItemModel> queue = new LinkedBlockingDeque<>(256);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(50 * 1000);
                        while (!busySignal()) ;
                        LoggerUtil.info(LOGGER, "变更标志状态");
                    }
                } catch (InterruptedException e) {
                    LoggerUtil.error(LOGGER, e);
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 10; i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    DataBaseService dataBaseService = DataBaseServiceFactory.getDataBaseService();
                    ItemModel model = queue.poll();
                    try {
                        while (model != null) {
                            dataBaseService.insertObject(model, "oridata");
                            model = queue.poll(busyTime, TimeUnit.MILLISECONDS);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
                if (idleSignal()) {
                    LoggerUtil.info(LOGGER, "sleep 5min");
                    Thread.sleep(10 * 1000);
                }
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
            LoggerUtil.error(LOGGER, e);
            e.printStackTrace();
        }

        System.out.println("finished");

    }

    private static boolean idleSignal() {
        if (signal.get() == 1) {
            synchronized (signal) {
                if (signal.get() == 1) {
                    signal.set(0);
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean busySignal() {
        if (signal.get() == 0) {
            synchronized (signal) {
                if (signal.get() == 0) {
                    signal.set(1);
                } else {
                    return false;
                }
                return true;
            }
        } else {
            return false;
        }
    }
}
