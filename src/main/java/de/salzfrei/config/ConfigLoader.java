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

    /*
    public void loadDefaultFilterActions(Config defaultActionsSection) {
        try {
            for (String k : defaultActionsSection.getKeys()) {
                String action;
                action = defaultActionsSection.getSection(k).getString("allow");
                if (action == null) {
                    throw new ConfigurationException("\"default actions\" is an invalid configuration section");
                } else {
                    filters.setDefaultAllowActionFor(k, action);
                }
                action = defaultActionsSection.getSection(k).getString("deny");
                if (action == null) {
                    throw new ConfigurationException("\"default actions\" is an invalid configuration section");
                } else {
                    filters.setDefaultDenyActionFor(k, action);
                }
            }
        } catch (Exception e) {
            throw new ConfigurationException("Error while loading a types default actions", e);
        }
    }


    public boolean loadFilters(Config rt) {
        log.info("loading filters...");
        boolean errorless = true;
        List<String> keys = new ArrayList<>(rt.getKeys());
        Comparator<String> com = (k1, k2) -> {
            Config c1 = rt.getSection(k1);
            Config c2 = rt.getSection(k2);
            if (c1 != null && c2 != null) {
                int p1, p2;
                if (c1.contains("priority")) {
                    p1 = c1.getInt("priority");
                } else {
                    p1 = 0;
                }
                if (c2.contains("priority")) {
                    p2 = c2.getInt("priority");
                } else {
                    p2 = 0;
                }
                return p2 - p1;
            }
            return 0;
        };
        keys.sort(com);

        List<AbstractFilter> filterList = new ArrayList<>();
        int patternCount = 0;
        for (String k : keys) {
            try {
                Config c = rt.getSection(k);
                if (c == null) {
                    throw new ConfigurationException(k + " is not a valid configuration section");
                }
                List<String> patterns = c.getStringList("patterns");
                if (patterns == null) {
                    throw new ConfigurationException("\"" + k + "." + "patterns\" is not a valid string list");
                }
                final String type = c.getString("type", filters.getDefaultType());
                AbstractFilter filter = filters.filterOfType(type, k.toLowerCase());
                filter.setPattern(patterns.toArray(new String[patterns.size() - 1]));
                if (c.contains("message")) {
                    filter.setDenyMsg(c.getString("message"));
                }
                filterList.add(filter);
                patternCount += patterns.size();
            } catch (Exception e) {
                log.log(Level.SEVERE, "could not load filter \"" + k + "\" with error: ", e);
                errorless = false;
            }
        }
        filters.setFilters(filterList);

        log.info("loaded " + filterList.size() + " filters with " + patternCount + " pattern(s)");
        return errorless;
    }
     */

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
                ByteStreams.copy(in, os);
            }
        } catch (IOException ignored) {}
    }

}
