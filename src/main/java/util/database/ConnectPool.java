package util.database;

import java.sql.Connection;
import java.sql.SQLException;

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
    Worker getConnection();

    /**
     * 将连接归还
     * @param worker 连接
     */
    void idleConnection(Worker worker);

    void close(Worker worker);

    void failed(Worker worker);
}
