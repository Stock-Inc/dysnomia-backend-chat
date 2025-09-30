package org.example.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
//
//@Configuration
//public class FirebaseConfig {

//    @PostConstruct
//    public void initializeApp() {
//        InputStream serviceAccount;
//        try {
//            serviceAccount = new ClassPathResource("dysnomia-chat-firebase-adminsdk-fbsvc-b0d5c4707c.json").getInputStream();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        FirebaseOptions options;
//        try {
//            options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        FirebaseApp.initializeApp(options);
//    }

//    @PostConstruct
//    public static void initializeFirebase() {
//        try {
////        InputStream serviceAccount = new ClassPathResource("storage-vladev-firebase-adminsdk-fbsvc-951d3c50a1.json").getInputStream();
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://storage-vladev.firebaseio.com")
//                    .setStorageBucket("storage-vladev.appspot.com")
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to initialize Firebase", e);
//        }
//    }
//
//    public static void upload() {
//        try {
//            // Или через GCS с credentials
//            InputStream credentials = new ClassPathResource("storage-vladev-firebase-adminsdk-fbsvc-951d3c50a1.json").getInputStream();
//            Storage storage = StorageOptions.newBuilder()
//                    .setCredentials(GoogleCredentials.fromStream(credentials))
//                    .setProjectId("storage-vladev")
//                    .build()
//                    .getService();
//
//            BlobId blobId = BlobId.of("storage-vladev.appspot.com", "file.jpg");
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//
//            byte[] fileContent = Files.readAllBytes(Paths.get("C:\\Users\\bogda\\IdeaProjects\\dysnomia-backend-chat\\src\\main\\resources\\img.jpg"));
//            storage.create(blobInfo, fileContent);
//
//            System.out.println("File uploaded successfully");
//
//        } catch (IOException e) {
//            throw new RuntimeException("File upload failed", e);
//        }
//    }
//
//    public static void sendNotification(String title, String body) {

//        if (FirebaseApp.getApps().isEmpty()) {
//            initializeFirebase();
//        }
//        String topic = "global";
//        Message message = Message.builder()
//                .putData("title", title)
//                .putData("body", body)
//                .setTopic(topic)
//                .build();
//
//        try {
//            FirebaseMessaging.getInstance().send(message);
//        } catch (FirebaseMessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
