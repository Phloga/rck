package de.vee.rck.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String outputAsString(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = is.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }

    public static String readResourceAsString(ResourceLoader resLoader , String location) throws IOException {
        Resource res = resLoader.getResource(location);
        InputStream fileStream = res.getInputStream();
        return outputAsString(fileStream);
    }
}
