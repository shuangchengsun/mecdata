package secure;

import util.common.Assert;
import util.secure.AlgorithmEnum;
import util.secure.SecureManager;
import util.secure.impl.SecureManagerImpl;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 8:50 下午
 */
public class SecureManagerTest {
    static String key = "sun196130";
    static String content = "Sun196130";

    static SecureManager manager = new SecureManagerImpl();


    public static void main(String[] args) {
        String encrypt = manager.encrypt(key,content, AlgorithmEnum.DES);
        String decrypt = manager.decrypt(key,encrypt,AlgorithmEnum.DES);
        System.out.println(encrypt);
        Assert.assertEquals(content,decrypt);
    }
}
