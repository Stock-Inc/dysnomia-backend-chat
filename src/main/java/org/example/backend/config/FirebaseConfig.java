package org.example.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
public class FirebaseConfig {


    public void initializeFirstApp() {
        InputStream serviceAccount;
        try {
            serviceAccount = new ClassPathResource("dysnomia-chat-firebase-adminsdk-fbsvc-b0d5c4707c.json").getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FirebaseOptions options;
        try {
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FirebaseApp.initializeApp(options);
    }

    public void initializeSecondApp() {
        InputStream secondServiceAccount;
        FirebaseOptions secondOptions;
        try {
            secondServiceAccount = new ClassPathResource("storage-vladev-firebase-adminsdk-fbsvc-951d3c50a1.json").getInputStream();

            secondOptions = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(secondServiceAccount))
                    .setDatabaseUrl("https://storage-vladev.firebaseio.com")
                    .setStorageBucket("storage-vladev.appspot.com")
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(secondOptions);
        }
    }


    public void sendNotification(String title, String body) {
        if (FirebaseApp.getApps().isEmpty()) {
            initializeFirstApp();
        }
        String topic = "global";
        Message message = Message.builder()
                .putData("title", title)
                .putData("body", body)
                .setTopic(topic)
                .build();

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void upload() {
        if (FirebaseApp.getApps().isEmpty()) {
            initializeSecondApp();
        }
        try {
            InputStream credentials = new ClassPathResource("storage-vladev-firebase-adminsdk-fbsvc-951d3c50a1.json").getInputStream();
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(credentials))
                    .setProjectId("storage-vladev")
                    .build()
                    .getService();

            BlobId blobId = BlobId.of("storage-vladev.appspot.com", "file.jpg");
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

            byte[] fileContent = Files.readAllBytes(Path.of("C:\\Users\\bogda\\IdeaProjects\\dysnomia-backend-chat\\src\\main\\resources\\img.jpg"));
            storage.create(blobInfo, fileContent);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }

    public void download() {
        if (FirebaseApp.getApps().isEmpty()) {
            initializeSecondApp();
        }
        try {
            InputStream credentials = new ClassPathResource("storage-vladev-firebase-adminsdk-fbsvc-951d3c50a1.json").getInputStream();
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(credentials))
                    .setProjectId("storage-vladev")
                    .build()
                    .getService();
            StorageOptions storageOptions = StorageOptions.newBuilder().setProjectId("storage-vladev")
                    .build();
            storage.downloadTo(BlobId.of("storage-vladev.appspot.com", "file.jpg")
                    , Paths.get("file.jpg"));


        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
    }
}