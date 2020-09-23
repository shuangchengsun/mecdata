package util.database.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.common.BeanUtil;
import util.database.ConnectPool;
import util.database.DataBaseService;
import util.database.Worker;
import util.database.exception.DataBaseException;
import util.database.exception.DataBasesErrorEnum;
import util.log.LoggerUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户调用其中的一个函数，以操作数据库，目前仅支持最简单的CRUD，涉及到的问题是，如何动态构造sql，
 * 如何传递用户参数的类型，
 *
 *
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-08 9:22 上午
 */
public class DataBaseServiceImpl implements DataBaseService {

    private ConnectPool connectPoolImpl = null;

    private static final Logger LOGGER = LoggerFactory.getLogger("dataBaseSqlLogger");


    public DataBaseServiceImpl(ConnectPool connectPoolImpl) {
        this.connectPoolImpl = connectPoolImpl;
    }

    /**
     * 插入一个对象到数据库中，如果反射执行错误或者获取数据库连接失败，则直接抛出错误
     * @param object    对象名
     * @param tableName 表名
     * @return
     */
    @Override
    public boolean insertObject(Object object, String tableName) {
        //获取到领域模型的class对象
        Class<?> klass = object.getClass();

        //获取属性
        Field[] fields = klass.getDeclaredFields();

        //构建sql
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("insert into ");
        sqlBuilder.append(tableName);
        sqlBuilder.append(" (");

        //构建Bean的get方法
        String methodName = null;
        Queue<Object> queue = new LinkedList<>();   //保存get方法的结果
        for (Field field : fields) {
            //构建sql语句
            String name = field.getName();
            if (name.toLowerCase().equals("id")) {
                continue;
            }

            sqlBuilder.append(name);

            sqlBuilder.append(",");

            String typeName = field.getGenericType().getTypeName();


            methodName = BeanUtil.getGetMethod(field.getName());

            //调用get方法，得到属性值
            try {
                Object invoke = klass.getMethod(methodName).invoke(object);

                if (typeName.equals("java.util.Date")) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    invoke = simpleDateFormat.format(invoke);
                }
                queue.add(invoke);

            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new DataBaseException(DataBasesErrorEnum.ReflectError, e);
            }
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);

        sqlBuilder.append(") values (");
        while (!queue.isEmpty()) {
            sqlBuilder.append("'");
            sqlBuilder.append(Objects.requireNonNull(queue.poll()).toString());
            sqlBuilder.append("'");
            sqlBuilder.append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        sqlBuilder.append(")");
        String sql = sqlBuilder.toString();
        LoggerUtil.info(LOGGER, sql);
        Worker worker = connectPoolImpl.getConnection();

        //若是获取连接失败
        if (worker == null) {
            //重试5次
            int i = 0;
            while (i < 5 && worker == null) {
                worker = connectPoolImpl.getConnection();
                i++;
            }
            if (worker == null) {
                throw new DataBaseException(DataBasesErrorEnum.ConnectionError);
            }
        }

        boolean result = false;
        try {

            result = worker.executeSql(sql);
            int index = 0;
            while(!result && index<5){
                connectPoolImpl.idleConnection(worker);
                worker = connectPoolImpl.getConnection();
                result = worker.executeSql(sql);
                if(!result){
                    connectPoolImpl.failed(worker);
                    worker = null;
                }
                index++;
            }
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            result = preparedStatement.execute();
//            preparedStatement.close();
        } catch (DataBaseException e) {
            connectPoolImpl.failed(worker);
            worker = null;
//            e.printStackTrace();
            System.out.println("DataBaseServiceImpl.insertObject sql执行错误");
            throw e;
        } finally {
            if (worker != null) {
                connectPoolImpl.idleConnection(worker);
            }
        }
        return result;
    }

    @Override
    public boolean insertObject(Object object) {
        String table = object.getClass().getName();
        return insertObject(object, table);

    }

    @Override
    public Object getObjectById(Class<?> kclss, Object id, String... tables) {
        return null;
    }


    //    @Override
//    public Object getObjectById(Class<?> kclss, Object id, String... tables) {
//        Object instance = null;
//        try {
//            instance = kclss.newInstance();
//
//            //获取class中的所有方法
//            Method[] methods = kclss.getMethods();
//            Map<String, Method> methodMap = new HashMap<>();
//            for (Method method : methods) {
//                methodMap.put(method.getName(), method);
//            }
//
//            StringBuilder sqlBuilder = new StringBuilder();
//            sqlBuilder.append("SELECT ");
//
//            Field[] declaredFields = kclss.getDeclaredFields();
//            Queue<String> fieldQueue = new LinkedList<>();
//            for (Field field : declaredFields) {
//                sqlBuilder.append(field.getName());
//                fieldQueue.add(field.getName());
//                sqlBuilder.append(",");
//            }
//
//            String table = kclss.getName();
//            if (tables != null && tables.length == 1) {
//                table = tables[0];
//            }
//            sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
//            sqlBuilder.append(" FROM ").append(table).append(" WHERE id=").append(id);
//
//            Worker worker = connectPoolImpl.getConnection();
//            if (worker != null) {
//                String SQL = sqlBuilder.toString();
//                Statement statement = worker.createStatement();
//                ResultSet resultSet = statement.executeQuery(SQL);
//                LoggerUtil.info(LOGGER, SQL);
//                String fieldName = null;
//                if (!resultSet.next()) {
//                    return null;
//                }
//                while (!fieldQueue.isEmpty()) {
//                    fieldName = fieldQueue.poll();
//                    if (fieldName == null) {
//                        throw new RuntimeException("属性为空");
//                    }
//                    Object object = resultSet.getObject(fieldName);
//                    String methodName = BeanUtil.getSetMethod(fieldName);
//                    methodMap.get(methodName).invoke(instance, object);
//                }
//                resultSet.close();
//                statement.close();
//
//            }
//
//        } catch (InstantiationException | IllegalAccessException |
//                SQLException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        return instance;
//    }

    @Override
    public Object getObjectByField(Class kclass, String fieldName, Object filedValue) {
        return null;
    }

    @Override
    public Object getObjectByFields(Class kclass, String[] fieldNames, Object[] filedValues) {
        return null;
    }

    public void setConnectPoolImpl(ConnectPool connectPoolImpl) {
        this.connectPoolImpl = connectPoolImpl;
    }
}
