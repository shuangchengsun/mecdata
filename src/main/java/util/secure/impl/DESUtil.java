package util.secure.impl;

import org.bouncycastle.util.encoders.Hex;
import util.common.Encoding;
import util.common.StringUtil;
import util.secure.AlgorithmEnum;
import util.secure.Encryptor;
import util.secure.exception.SecureErrorEnum;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import static javax.crypto.Cipher.getInstance;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 3:05 下午
 */
class DESUtil implements Encryptor {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    static final String Algorithm = AlgorithmEnum.DES.getName();

    private byte[] key = null;

    public DESUtil(String password){
        if(!StringUtil.isNull(password) && !StringUtil.isEmpty(password) && !StringUtil.isBlank(password)
                && password.length()>8){
            try {
                key = password.getBytes(Encoding.UTF_8.getEncoding());
            } catch (UnsupportedEncodingException e) {
                key = DESUtil.getRandomKey();
                e.printStackTrace();
            }
        }else {
            key = DESUtil.getRandomKey();
        }
    }

    public DESUtil(){
        key = DESUtil.getRandomKey();
    }


    /**
     * 在用户未制定加密密码时，随机生成一个key
     * @return 密钥
     */
    private static byte[] getRandomKey(){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(Algorithm,"BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        keyGenerator.getProvider();

        keyGenerator.init(56);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();
        return key;
    }


    @Override
    public String encrypt(String password, String data) throws SecurityException{
        try {
            key = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory factory= SecretKeyFactory.getInstance(Algorithm);
            SecretKey key2 = factory.generateSecret(desKeySpec);      //转换后的密钥

            //加密
            Cipher cipher= getInstance("DES/ECB/PKCS5Padding");  //算法类型/工作方式/填充方式
            cipher.init(Cipher.ENCRYPT_MODE, key2);
            byte[] result=cipher.doFinal(data.getBytes());
            return Hex.toHexString(result);

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException |
                NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            throw new SecurityException(SecureErrorEnum.ENCRYPT_ERROR.getMessage(),e);
        }
    }

    @Override
    public String decrypt(String password, String data) throws SecurityException{
        if(StringUtil.isNull(data) || StringUtil.isEmpty(data) ||StringUtil.isBlank(data)){
            throw new SecurityException(SecureErrorEnum.DATA_FORMAT_ERROR.getMessage());
        }
        if(StringUtil.isNull(password) || StringUtil.isEmpty(password) || StringUtil.isBlank(password) || password.length()<8){
            throw new SecurityException(SecureErrorEnum.PASSWORD_FORMAT_ERROR.getMessage());
        }
        try {
            key = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory factory= SecretKeyFactory.getInstance(Algorithm);
            SecretKey key2 = factory.generateSecret(desKeySpec);      //转换后的密钥

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,key2);
            byte[] decode = Hex.decode(data.getBytes());
            byte[] bytes = cipher.doFinal(decode);
            return new String(bytes);
        }catch (Exception e){
            throw new SecurityException(SecureErrorEnum.DECRYPT_ERROR.getMessage(),e);
        }
    }
}
