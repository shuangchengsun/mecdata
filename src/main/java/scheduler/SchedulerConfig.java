package scheduler;

/**
 * @ClassName SchedulerConfig
 * @Author sunshuangcheng
 * @Date 2020/9/23 8:47 下午
 * @Version -V1.0
 */
public class SchedulerConfig {

    /**
     * 需要调度的class
     */
    private String className;

    /**
     * class中所需要执行的方法
     */
    private String methodName;

    /**
     * 延迟启动
     */
    private int delayTime;

    /**
     * 持续工作时长
     */
    private int workTime;

    /**
     * 执行间断时长
     */
    private int idleTime;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
    }
}
