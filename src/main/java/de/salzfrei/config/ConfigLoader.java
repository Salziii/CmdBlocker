package de.salzfrei.config;

import com.google.common.io.ByteStreams;

import javax.naming.ConfigurationException;
import java.io.*;
import java.nio.file.Files;

public class ConfigLoader {
    protected final File baseFile;
    protected final File configFile;

    public ConfigLoader(File baseFile, String name) {
        this.baseFile = baseFile;
        this.configFile = new File(baseFile, name);
    }

    @SuppressWarnings("unused")
    public Config reloadConfig() {
        baseFile.mkdirs();
        Config internalConfig = loadInternalConfig();
        return loadConfig(internalConfig);
    }

    public Config loadConfig(Config defaults) {
        if (!configFile.isFile()) {
            saveDefaultConfig();
        }
        Config config;
        try {
            try {
                config = Config.load(configFile);
                config.setDefaults(defaults);
            } catch (Exception e) {
                regenerateConfig();
                config = Config.load(configFile);
                config.setDefaults(defaults);
            }
            return config;
        } catch (Exception ignored) {}
        return null;
    }

    public void regenerateConfig() {
        try {
            for (int i = 0; i < 100; i++) {
                File to = new File(baseFile, String.format("config.old.%s.yml", i));
                if (!to.exists()) {
                    Files.move(configFile.toPath(), to.toPath());
                    saveDefaultConfig();
                    return;
                }
            }
        } catch (Exception ignored) {}
    }

    public Config loadInternalConfig() {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.yml")) {
            try {
                return Config.load(in);
            } catch (ConfigurationException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException ignored) {}
        return null;
    }

    public void saveDefaultConfig() {
        try {
            baseFile.mkdirs();
            configFile.createNewFile();
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("config.yml"); OutputStream os = Files.newOutputStream(configFile.toPath())) {
                assert in != null;
                ByteStreams.copy(in, os);
            }
        } catch (IOException ignored) {}
    }

}
