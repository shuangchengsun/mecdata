package util.database;

import util.common.YamlReader;
import util.database.impl.ConnectPoolImpl;
import util.database.impl.DataBaseServiceImpl;
import util.database.model.DatabaseConfig;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-08 7:59 下午
 */
public class DataBaseServiceFactory {

    private static DataBaseService service = null;
    private static DatabaseConfig config = null;

    public static DataBaseService getDataBaseService(DatabaseConfig config){
        DataBaseService  dataBaseService = null;
        ConnectPool connectPool = new ConnectPoolImpl(config,true);
        dataBaseService = new DataBaseServiceImpl(connectPool);
        return dataBaseService;
    }

    public static DataBaseService getDataBaseService(){
        if(service != null) {
            return service;
        }else {
            synchronized (DataBaseServiceFactory.class){
                if(service != null){
                    return service;
                }
                config = new DatabaseConfig();
                config.setDatabase((String) YamlReader.getValueByKey("datasource.databaseName"));
                config.setUrl((String) YamlReader.getValueByKey("datasource.url"));
                config.setCoreSize((int) YamlReader.getValueByKey("connectPool.coreSize") );
                config.setMaxSize((int)YamlReader.getValueByKey("connectPool.maxSize"));

                String user = (String) YamlReader.getValueByKey("datasource.userName");
                String password = (String) YamlReader.getValueByKey("datasource.password");

                config.setUser(user);
                config.setPassword(password);

                ConnectPool connectPool = new ConnectPoolImpl(config,true);

                service = new DataBaseServiceImpl(connectPool);

                return service;
            }
        }
    }

    public static DatabaseConfig getDataBaseConfig() {
        return config;
    }
}
