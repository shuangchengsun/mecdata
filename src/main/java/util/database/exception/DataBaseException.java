package util.database.exception;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-14 10:30 下午
 */
public class DataBaseException extends RuntimeException {

    public DataBaseException(DataBasesErrorEnum message) {
        super(message.getMessage());
    }

    public DataBaseException(DataBasesErrorEnum message, Throwable cause) {
        super(message.getMessage(), cause);
    }
}
