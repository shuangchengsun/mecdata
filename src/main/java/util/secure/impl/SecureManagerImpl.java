package util.secure.impl;

import util.common.StringUtil;
import util.secure.AlgorithmEnum;
import util.secure.Encryptor;
import util.secure.SecureManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 4:41 下午
 */
public class SecureManagerImpl implements SecureManager {

    private static Map<String, Encryptor> map = new ConcurrentHashMap<>(16);
    private static String defaultPassword = "djfyuaksifgl";

    @Override
    public String encrypt(String password, String data, AlgorithmEnum algorithmEnum) {

        String key = password;
        if(passwordCheck(key)){
            key = defaultPassword;
        }
        Encryptor encryptor = getEncryptor(algorithmEnum.getName());
        return encryptor.encrypt(key,data);
    }

    @Override
    public String decrypt(String password, String data, AlgorithmEnum algorithmEnum) {
        String key = password;
        if(passwordCheck(key)){
            key = defaultPassword;
        }

        Encryptor encryptor = getEncryptor(algorithmEnum.getName());
        return encryptor.decrypt(key,data);
    }

    private Encryptor getEncryptor(String name){

        map.computeIfAbsent(name, new Function<String, Encryptor>() {
            @Override
            public Encryptor apply(String s) {
                Encryptor encryptor = null;
                if(s.equals(AlgorithmEnum.DES.getName())){
                    encryptor = new DESUtil();
                }

                if(encryptor == null){
                    encryptor = new DESUtil();
                }
                return encryptor;
            }
        });
        return map.get(name);
    }

    private boolean passwordCheck(String password){
        return (StringUtil.isNull(password) || StringUtil.isEmpty(password) || StringUtil.isBlank(password));
    }
}
