package model;

import java.util.Date;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-09 2:18 下午
 */
public class ItemModel {
    private String userid;

    private String appname;

    private double longitude;

    private Date StartTime;

    private double latitude;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "userid='" + userid + '\'' +
                ", appname='" + appname + '\'' +
                ", longitude=" + longitude +
                ", StartTime=" + StartTime +
                ", latitude=" + latitude +
                '}';
    }
}
