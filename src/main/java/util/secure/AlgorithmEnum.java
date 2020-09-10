package util.secure;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 4:46 下午
 */
public enum AlgorithmEnum {
    DES("DES"),
    AES("AES");
    private String name;

    AlgorithmEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
