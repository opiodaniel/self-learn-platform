package com.e_learning.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class GoogleCloudStorageService {

    @Value("${google.cloud.bucket}")
    private String BUCKET_NAME;

    @Value("${gcp_credential}")
    private String GCP_FILE_NAME;

    private Storage storage;

    //Ensure this runs AFTER properties are injected
    @PostConstruct
    public void init() {
        try {
            System.out.println("Initializing Google Cloud Storage...");
            System.out.println("Bucket Name: " + BUCKET_NAME);
            System.out.println("GCP Credential File: " + GCP_FILE_NAME);

            if (GCP_FILE_NAME == null || GCP_FILE_NAME.isEmpty()) {
                throw new IOException("GCP credentials file path is not set.");
            }

            FileInputStream credentialsStream = new FileInputStream(GCP_FILE_NAME);
            this.storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build()
                    .getService();
            System.out.println("Google Cloud Storage initialized successfully.");
        } catch (IOException e) {
            System.err.println("Error initializing Google Cloud Storage: " + e.getMessage());
            this.storage = null; // Prevent crashes
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (storage == null) {
            throw new IOException("Google Cloud Storage is not initialized. Check your credentials.");
        }

        // Generate a unique file name
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        System.out.println("Uploading to Bucket: " + BUCKET_NAME);

        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Upload file
        storage.create(blobInfo, file.getBytes());

        // Return file URL
        return String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, fileName);
    }

    public void deleteFile(String fileUrl) throws IOException {
        if (storage == null) {
            throw new IOException("Google Cloud Storage is not initialized. Check your credentials.");
        }

        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        System.out.println("Deleting file: " + fileName + " from bucket: " + BUCKET_NAME);

        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        storage.delete(blobId);
    }
}


