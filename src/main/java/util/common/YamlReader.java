package util.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import util.log.LoggerUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-07 2:46 下午
 */
public class YamlReader {

    private static HashMap<String, LinkedHashMap> properties = new HashMap<>(64);

    public static void parseFile(String path) {
        Yaml yaml = new Yaml();
        try (InputStream stream = YamlReader.class.getClassLoader().getResourceAsStream(path)) {
            properties = yaml.loadAs(stream, HashMap.class);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void parseFile(String... strings) {
        Yaml yaml = new Yaml();
        for (String path : strings) {
            try (InputStream stream = YamlReader.class.getClassLoader().getResourceAsStream(path)) {
                properties.putAll(yaml.loadAs(stream, HashMap.class));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static Object getValueByKey(String key) {
        String separator = ".";
        String[] separatorKeys = null;
        if (key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            return properties.get(key);
        }
        Map<String, Map<String, Object>> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) properties.get(separatorKeys[i]);
                continue;
            }
            if (finalValue == null) {
                break;
            }
            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }
        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]);
    }

}
