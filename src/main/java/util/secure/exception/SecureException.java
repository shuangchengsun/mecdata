package util.secure.exception;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 5:02 下午
 */
public class SecureException extends RuntimeException {

    public SecureException(String message) {
        super(message);
    }

    public SecureException(String message, Throwable cause) {
        super(message, cause);
    }
}
