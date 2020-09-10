import com.csvreader.CsvReader;
import config.ConfigInit;
import dataprocess.DataClean;
import model.ItemModel;
import util.csv.CSVService;
import util.csv.CSVUtil;
import util.common.Encoding;
import util.csv.CSVWorker;
import util.csv.impl.CSVServiceImpl;
import util.database.DataBaseService;
import util.database.DataBaseServiceFactory;
import util.database.model.DatabaseConfig;


import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MecData {
    public static void main(String[] args) throws IOException {

        ConfigInit.init();
        DataClean.uploadData();


    }








}
