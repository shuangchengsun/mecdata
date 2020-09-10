package model;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-09 10:19 上午
 */
public class AppNameModel {
    private int id;
    private String appName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "AppNameModel{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                '}';
    }
}
