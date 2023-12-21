package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourcesLoader {
    private final String resourcePath;

    public ResourcesLoader(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public Properties readProperties() {
        Properties props = new Properties();
        try (InputStream in = new File(resourcePath).exists() ? new FileInputStream(resourcePath)
                : this.getClass().getResourceAsStream(resourcePath)) {
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // URL resource = Resources.getResource("config.properties");
        // try (FileInputStream fis = new FileInputStream(resource.getPath())) {
        //     props.load(fis);
        // } catch (IOException e) {
        //     throw new RuntimeException(e);
        // }
        return props;
    }
}
