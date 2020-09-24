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
                config.setDatabase(System.getProperty("DATASOURCE_DATABASENAME"));
                config.setUrl(System.getProperty("DATASOURCE_URL"));
                config.setCoreSize(Integer.parseInt(System.getProperty("CONNECTPOOL_CORESIZE")) );
                config.setMaxSize(Integer.parseInt(System.getProperty("CONNECTPOOL_MAXSIZE")));

                String user = System.getProperty("DATASOURCE_USERNAME");
                String password = System.getProperty("DATASOURCE_PASSWORD");

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
