package util.database;

import java.sql.SQLException;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-15 4:35 下午
 */
public interface Worker {

    boolean executeSql(String sql);

    void close() throws SQLException;

}
