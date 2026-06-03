package com.jpmc.midascore;

import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileLoader {
    
    public String[] loadStrings(String path) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(path);
            if (inputStream == null) {
                return null;
            }
            
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            return lines.toArray(new String[0]);
            
        } catch (Exception e) {
            return null;
        }
    }
}
