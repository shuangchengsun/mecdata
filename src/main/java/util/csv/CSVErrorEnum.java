package util.csv;

public enum CSVErrorEnum {
    LENGTH_NOT_MATCH_ERROR("Length of data and head is not match", "标题的长度和数据的长度不匹配"),

    IO_FILED_ERROR("io exception", "io 操作遇到异常");


    String errorMessage;
    String describe;

    CSVErrorEnum(String errorMessage, String describe) {
        this.errorMessage = errorMessage;
        this.describe = describe;
    }
}
