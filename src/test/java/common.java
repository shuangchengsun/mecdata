import config.ConfigInit;
import model.AppNameModel;
import util.database.DataBaseService;
import util.database.DataBaseServiceFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-08 8:41 下午
 */
public class common {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
//        ConfigInit.init();
//        DataBaseService dataBaseService = DataBaseServiceFactory.getDataBaseService();
//        Object applist = dataBaseService.getObjectById(AppNameModel.class, 3, "applist");
//        System.out.println(applist.toString());
        String dat = "2018-9-10 23:22";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm");
        Date parse = simpleDateFormat.parse(dat);
        System.out.println(simpleDateFormat.format(parse));
    }
}
