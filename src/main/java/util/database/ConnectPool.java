package util.database;

import java.sql.Connection;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-08 8:05 下午
 */
public interface ConnectPool {

    /**
     * @return 连接
     */
    Connection getConnection();

    /**
     * 将连接归还
     * @param connection 连接
     */
    void idleConnection(Connection connection);
}
