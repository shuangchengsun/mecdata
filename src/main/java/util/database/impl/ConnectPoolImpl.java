package util.database.impl;


import util.database.ConnectPool;
import util.database.Worker;
import util.database.model.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Deque;
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
    private int idleWorkersNum = 0;         // 空闲的worker
    Deque<Worker> idleWorkers = new LinkedList<>(); //存储空闲的worker

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
                Worker worker = new DefaultWorker( connection);
                idleWorkers.add(worker);
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
                Worker worker = new DefaultWorker(connection);
                idleWorkers.add(worker);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Worker getConnection() {
        Worker worker = null;
        synchronized (this) {
            if (idleWorkersNum > 0) {
                worker = idleWorkers.pollLast();
                idleWorkersNum--;
            } else {
                if (currentWorkersNum < maxSize) {
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        for (int i = 0; i < 5 && currentWorkersNum < maxSize; i++) {
                            currentWorkersNum++;
                            idleWorkersNum++;
                            Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
                            Worker worker1 = new DefaultWorker(conn);
                            idleWorkers.addFirst(worker1);
                        }
                        worker = idleWorkers.pollLast();
                        idleWorkersNum--;
                    } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return worker;
    }

    @Override
    public void idleConnection(Worker worker) {
        synchronized (this) {
            idleWorkers.addFirst(worker);
            idleWorkersNum++;
        }
    }

    @Override
    public void close(Worker worker){
        synchronized (this) {
            try {
                if (worker != null) {
                    worker.close();
                }
                currentWorkersNum--;
                worker = null;
            }catch (SQLException exception){
                //System.out.println(exception.getMessage());
            }
        }
    }

    @Override
    public void failed(Worker worker) {
        synchronized (this){
            try{
                worker.close();
            }catch (SQLException exception){
                //System.out.println(exception.getMessage());
            }
            currentWorkersNum--;
            worker = null;
        }
    }

    public String getDatabase() {
        return database;
    }
}
