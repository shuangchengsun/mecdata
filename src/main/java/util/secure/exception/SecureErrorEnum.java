package util.secure.exception;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 5:02 下午
 */
public enum SecureErrorEnum {
    DATA_FORMAT_ERROR("数据格式错误，可能为空或者空白"),
    PASSWORD_FORMAT_ERROR("加密密码格式错误，可能为空、空白或者长度小于8"),
    ENCRYPT_ERROR("加密过程中出现错误"),
    DECRYPT_ERROR("解密过程出现错误")
    ;
    private String message;

    SecureErrorEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
