package ru.javaops.webapp;

import ru.javaops.webapp.storage.SqlStorage;
import ru.javaops.webapp.storage.Storage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public class Config {
    private static final String PROPS = "/resumes.properties";
    private static final Config INSTANCE = new Config();

    private final File storageDir;
    private final Storage storage;

    private Config() {
        try (InputStream is = Config.class.getResourceAsStream(PROPS)) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            Class.forName(props.getProperty("db.driverClassName"));
            storage = new SqlStorage(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));

        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }

//    private static File getHomeDir() {
//        String prop = System.getProperty("homeDir");
//        File homeDir = new File(prop == null ? "." : prop);
//        if (!homeDir.isDirectory()) {
//            throw new IllegalStateException(homeDir + " is not directory");
//        }
//        return homeDir;
//    }
}
