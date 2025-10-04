//package com.project.extickets.service;
//
//import java.io.InputStream;
//import java.util.Collections;
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//
//public class DriveServiceFactory {
//
//    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//
//    public static Drive createDrive(InputStream credentialsStream, String appName) throws Exception {
//    	// 1. Load credentials directly from the stream provided by the caller (DriveStorageService)
//    	GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
//    	        .createScoped(Collections.singleton(DriveScopes.DRIVE));
//
//        // 2. Build and return the Drive service
//        return new Drive.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JSON_FACTORY,
//                new HttpCredentialsAdapter(credentials))
//                .setApplicationName(appName)
//                .build();
//    }
//}
