package config;

import util.array.ArrayUtil;
import util.common.StringUtil;
import util.common.YamlReader;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-08 3:03 下午
 */

public class ConfigInit {

    public static void init() {

        URL url = ConfigInit.class.getClassLoader().getResource("application.yml");
        assert url != null;
        String file1 = url.getFile();
        char[] chars = file1.toCharArray();
        int len = chars.length;
        int index = 0;
        for (int i = len - 1; i >= 0; i--) {
            if (chars[i] == '/') {
                index = i;
                break;
            }
        }
        String rootPath = String.valueOf(chars, 0, index);

        File file = new File(rootPath);
        File[] files = file.listFiles();

        List<String> fileList = new ArrayList<>(16);
        if (files != null) {
            for (File f : files) {
                if(f.isFile()){
                    String fileName = f.getName();
                    if(fileName.endsWith(".yml")){
                        fileList.add(fileName);
                    }
                }
            }
        } else {
        }
        String[] configFiles = new String[fileList.size()];
        ArrayUtil.listToArray(fileList,configFiles,0);
        YamlReader.parseFile(configFiles);
        addProperties();
    }


    private static void addProperties(){
        System.out.println("系统配置如下");
        String value = null;
        for(String name : ConfigResourceEnum.properties){
            value = (String) YamlReader.getValueByKey(name);
            if(!StringUtil.isNull(value) && !StringUtil.isEmpty(value)&&!StringUtil.isBlank(value)){
                String s = name.toUpperCase().replace(".", "_");
                System.setProperty(s,value);
                System.out.println(name+": "+value);
            }
        }
        setLogPath();
        setDatasource();
    }

    /**
     * 设定默认的日志目录。
     */
    private static void setLogPath(){
        String log_path = System.getProperty("LOG_PATH");
        if(log_path == null){
            System.setProperty("LOG_PATH","${user.home}/logs");
        }
        if(System.getProperty("LOG_LEVEL") == null){
            System.setProperty("LOG_LEVEL","info");
        }
    }
    private static void setDatasource(){
        if(System.getProperty("DATASOURCE_URL") == null){
            System.setProperty("DATASOURCE_URL","jdbc:mysql://rm-2ze6tlyn0l3812nsmqo.mysql.rds.aliyuncs.com:3306/mecdata");
        }
        if(System.getProperty("DATASOURCE_USERNAME") == null){
            System.setProperty("DATASOURCE_USERNAME","alan");
        }
        if(System.getProperty("DATASOURCE_PASSWORD") == null){
            System.setProperty("DATASOURCE_PASSWORD","Sun196130");
        }
        if(System.getProperty("DATASOURCE_DATABASENAME") == null){
            System.setProperty("DATASOURCE_DATABASENAME","mecdata");
        }
        if(System.getProperty("CONNECTPOOL_CORESIZE") == null){
            System.setProperty("CONNECTPOOL_CORESIZE","30");
        }
        if (System.getProperty("CONNECTPOOL_MAXSIZE") == null){
            System.setProperty("CONNECTPOOL_MAXSIZE","100");
        }
    }

}
