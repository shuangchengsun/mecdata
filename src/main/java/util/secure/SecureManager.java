package util.secure;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 4:42 下午
 */
public interface SecureManager {


    /**
     * @param password  加密密码
     * @param data      待加密的明文
     * @param algorithmEnum 加密算法
     * @return 密文
     */
    String encrypt(String password, String data, AlgorithmEnum algorithmEnum);

    /**
     * @param password  解密密码
     * @param data      待解密的密文
     * @param algorithmEnum 解密算法
     * @return 明文
     */
    String decrypt(String password, String data, AlgorithmEnum algorithmEnum);
}
