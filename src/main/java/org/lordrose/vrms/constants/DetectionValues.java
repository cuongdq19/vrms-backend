package org.lordrose.vrms.constants;

import org.lordrose.vrms.exceptions.InvalidStateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DetectionValues {

    @Value("${mapping.file.path}")
    private String mappingFilePath;

    @Bean
    public ValueMapping getMapper() {
        try (FileInputStream in = new FileInputStream(mappingFilePath)) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                Map<String, String> mappingMap = new HashMap<>();
                String line;

                while ((line = br.readLine()) != null) {
                    if (line.contains(":")) {
                        mappingMap.put(line.split(":")[0], line.split(":")[1]);
                    }
                }
                return new ValueMapping(mappingMap);
            } catch (Exception e) {
                throw new InvalidStateException("Error while reading value file");
            }
        } catch (Exception e) {
            throw new InvalidStateException("Error while loading value file");
        }
    }

    public static class ValueMapping {

        private final Map<String, String> valueMapping;

        public ValueMapping(Map<String, String> map) {
            valueMapping = map;
        }

        public String getSystemValue(String externalValue) {
            String value = valueMapping.get(externalValue);
            if (value == null) {
                value = "";
            }
            return value;
        }
    }
}
