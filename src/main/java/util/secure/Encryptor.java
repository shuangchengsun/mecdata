package util.secure;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 4:50 下午
 */
public interface Encryptor {

    /**
     * @param password 加密密码
     * @param data     待加密的明文
     * @return 密文
     */
    public String encrypt(String password, String data) throws SecurityException;

    /**
     * @param password 解密密码
     * @param data     待解密的密文
     * @return 明文
     */
    public String decrypt(String password, String data) throws SecurityException;

}
