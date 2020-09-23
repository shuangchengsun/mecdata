package scheduler.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scheduler.Scheduler;
import scheduler.SchedulerConfig;
import util.log.LoggerUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName DefaultScheduler
 * @Author sunshuangcheng
 * @Date 2020/9/23 8:48 下午
 * @Version -V1.0
 */
public class DefaultScheduler implements Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger("defaultFileLogger");

    @Override
    public void scheduling(SchedulerConfig config) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            // 加载class
            Class<?> klass = classLoader.loadClass(config.getClassName());
            // 构造实例
            Object obj = klass.newInstance();

            // 获取方法
            Method method = klass.getMethod(config.getMethodName());

            int del = config.getDelayTime();
            if(del>0) {
                Thread.sleep(del);
            }
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        method.invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            };
            Thread thread = new Thread(runnable);

            thread.start();

            Signal signal = new Signal();


            // 反射调用实例
            Object invoke = method.invoke(obj);


        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | InterruptedException e) {
            LoggerUtil.error(LOGGER, e);
        }
    }

    static class Signal{

        private static AtomicInteger num = new AtomicInteger(-1);
        Map<String, AtomicInteger> task = new HashMap<>(16);


        String register(){
            String s = String.valueOf(num.getAndIncrement());
            AtomicInteger atomicInteger = new AtomicInteger(0);
            task.put(s,atomicInteger);
            return s;
        }
    }
}
