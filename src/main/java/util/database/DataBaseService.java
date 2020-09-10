package util.database;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 10:01 下午
 */
public interface DataBaseService {

    /**
     * 插入一个对象
     *
     * @param object    对象名
     * @param tableName 表名
     * @return 是否成功
     */
    boolean insertObject(Object object, String tableName);

    /**
     * 插入一个对象，默认对象名就是表名
     *
     * @param object 对象
     * @return 是否成功
     */
    boolean insertObject(Object object);


    Object getObjectById(Class<?> kclss, Object id,String... tables);

    Object getObjectByField(Class kclass, String fieldName, Object filedValue);

    Object getObjectByFields(Class kclass, String[] fieldNames, Object[] filedValues);

}
