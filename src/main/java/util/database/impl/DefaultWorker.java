package util.database.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.database.Worker;
import util.database.exception.DataBaseException;
import util.database.exception.DataBasesErrorEnum;
import util.log.LoggerUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-15 4:38 下午
 */
public class DefaultWorker implements Worker {

    private static final Logger LOGGER = LoggerFactory.getLogger("dataBaseSqlLogger");

    private Connection connection = null;

    public DefaultWorker(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean executeSql(String sql) {
        boolean res = false;
        try {
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             preparedStatement.execute();
            preparedStatement.close();
            res = true;
        } catch (SQLException e) {
            LoggerUtil.error(LOGGER,e);
            throw new DataBaseException(DataBasesErrorEnum.ExecuteError,e);
        }
        return res;
    }

    @Override
    public void close() throws SQLException {
        if(this.connection != null){
            connection.close();
        }
        this.connection = null;
    }
}
