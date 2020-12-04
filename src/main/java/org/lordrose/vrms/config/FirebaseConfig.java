package org.lordrose.vrms.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    private static final String configPath = "firebase-config.json";

    @Primary
    @Bean
    public StorageOptions initFirebaseStorage() {
        try {
            FileInputStream serviceAccount = new FileInputStream(configPath);
            return StorageOptions.newBuilder()
                    .setProjectId("vrms-290212")
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Unable to init storage");
        }
    }

    @Primary
    @Bean
    public void initializeApp() {
        try {
            FileInputStream serviceAccount = new FileInputStream(configPath);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            System.out.println("Error when initialized: " + e.getLocalizedMessage());
        }
    }
}
