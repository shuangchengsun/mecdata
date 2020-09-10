package util.database.impl;


import util.database.ConnectPool;
import util.database.model.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 9:42 下午
 */
public class ConnectPoolImpl implements ConnectPool {

    private int coreSize;
    private int maxSize;
    private String database;
    private String url;
    private String user;
    private String password;

    private int currentWorkersNum = 0;      // 目前总共有多少worker
    private int idleWorkersNum = 0;
    Queue<Connection> idleWorkers = new LinkedList<>();

    public ConnectPoolImpl(int coreSize, int maxSize, String database, String user, String password, String url, boolean isLazy) {
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.database = database;
        this.user = user;
        this.password = password;
        this.user = url;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            int count = 0;
            if (isLazy) {
                count = Math.max(coreSize / 3, 1);
            } else {
                count = this.coreSize;
            }

            for (int i = 0; i < count; i++) {
                Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                idleWorkersNum++;
                currentWorkersNum++;
                idleWorkers.add(connection);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public ConnectPoolImpl(DatabaseConfig config, boolean isLazy) {
        this.coreSize = config.getCoreSize();
        this.maxSize = config.getMaxSize();
        this.user = config.getUser();
        this.database = config.getDatabase();
        this.password = config.getPassword();
        this.url = config.getUrl();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            int count = 0;
            if (isLazy) {
                count = Math.max(coreSize / 3, 1);
            } else {
                count = this.coreSize;
            }

            for (int i = 0; i < count; i++) {
                Connection connection = DriverManager.getConnection(this.url, this.user, this.password);
                idleWorkersNum++;
                currentWorkersNum++;
                idleWorkers.add(connection);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        synchronized (this) {
            if (idleWorkersNum > 0) {
                connection = idleWorkers.poll();
                idleWorkersNum--;
            } else {
                if (currentWorkersNum < maxSize) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        for (int i = 0; i < 5 && currentWorkersNum < maxSize; i++) {
                            currentWorkersNum++;
                            idleWorkersNum++;
                            Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
                            idleWorkers.add(conn);
                        }
                        connection = idleWorkers.poll();
                        idleWorkersNum--;
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }

    @Override
    public void idleConnection(Connection connection) {
        synchronized (this){
            idleWorkers.add(connection);
            idleWorkersNum++;
        }
    }

    public String getDatabase() {
        return database;
    }
}
