package util.database.exception;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-14 10:31 下午
 */
public enum DataBasesErrorEnum {
    ConnectionError("和数据库建立连接失败"),
    ExecuteError("SQL执行错误"),
    ReflectError("反射处理错误")
    ;
    private String message;

    DataBasesErrorEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
