package de.salzfrei.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import javax.naming.ConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

@SuppressWarnings("unused")
public class Config {

    private static final ThreadLocal<Yaml> yaml = ThreadLocal.withInitial(() -> {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Representer representer = new Representer(options) {
            {
                this.representers.put(Config.class, data -> represent(((Config) data).map));
            }
        };
        return new Yaml(new Constructor(new LoaderOptions()), representer, options);
    });
    public static Yaml getThreadLocalYaml() {
        return yaml.get();
    }

    @SuppressWarnings("unchecked")
    public static Config load(InputStream in) throws ConfigurationException {
        Map<String, Object> val = yaml.get().loadAs(in, LinkedHashMap.class);
        if (val == null) {
            throw new ConfigurationException("Failed to load config: map object is null");
        }
        return new Config(val);
    }

    @SuppressWarnings("unchecked")
    public static Config load(String config) throws ConfigurationException {
        Map<String, Object> val = yaml.get().loadAs(config, LinkedHashMap.class);
        if (val == null) {
            throw new ConfigurationException("Failed to load config: map object is null");
        }
        return new Config(val);
    }

    public static Config load(File configFile) throws ConfigurationException {
        try (InputStream in = Files.newInputStream(configFile.toPath())) {
            return load(in);
        } catch (IOException | ConfigurationException e) {
            throw new ConfigurationException("IOException while loading config");
        }
    }

    public static void write(Config config, OutputStream os) {
        yaml.get().dump(config, new OutputStreamWriter(os));
    }

    public static String writeAsString(Config config) {
        return yaml.get().dump(config);
    }

    public final Map<String, Object> map;
    public Config defaults = null;

    public Config(Map<String, Object> map) {
        this.map = map;
    }

    public void setDefaults(Config defaults) {
        this.defaults = defaults;
    }

    public Object get(String key) {
        return map.getOrDefault(key, defaults == null ? null : defaults.get(key));
    }

    @SuppressWarnings("unchecked")
    public Config getSection(String path) {
        Object val = get(path);
        if (val instanceof Map) {
            return new Config((Map<String, Object>) val);
        }
        return new Config(new LinkedHashMap<>());
    }

    public Collection<String> getKeys() {
        return new ArrayList<>(map.keySet());
    }

    public String getString(String key) {
        Object val = get(key);
        return val instanceof String ? (String) val : "";
    }

    public String getString(String key, String defaultValue) {
        Object val = map.get(key);
        return val instanceof String ? (String) val : defaultValue;
    }

    public int getInt(String key) {
        Object val = get(key);
        return val instanceof Number ? ((Number) val).intValue() : 0;
    }

    public double getDouble(String key) {
        Object val = get(key);
        return val instanceof Number ? ((Number) val).doubleValue() : 0D;
    }

    public List<String> getStringList(String key) {
        Object val = get(key);
        if (val instanceof List<?> origin) {
            List<String> results = new ArrayList<>(origin.size());
            for (Object o : (List<?>) val) {
                if (o instanceof String) {
                    results.add((String) o);
                }
            }
            return results;
        }
        return new ArrayList<>();
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

}
