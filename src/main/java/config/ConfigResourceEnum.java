package config;


import java.util.HashSet;
import java.util.Set;

public class ConfigResourceEnum {

    public static final Set<String> properties = new HashSet<>(16);

    static {
        properties.add("log.level");
        properties.add("log.path");
    }
}
