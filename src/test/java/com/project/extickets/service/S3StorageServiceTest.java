package com.project.extickets.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

class S3StorageServiceTest {

    private S3Client s3Client;
    private S3StorageService s3StorageService;

    @BeforeEach
    void setUp() {
        s3Client = mock(S3Client.class);
        s3StorageService = new S3StorageService(s3Client);
        // inject bucket name (since it's @Value in real app)
        ReflectionTestUtils.setField(s3StorageService, "bucketName", "test-bucket");
    }

    @Test
    void testUploadFile_Success() throws Exception {
        // Arrange
        MockMultipartFile mockFile =
                new MockMultipartFile("file", "test.txt", "text/plain", "Hello".getBytes());

        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);

        // Act
        String url = s3StorageService.uploadFile(mockFile);

        // Assert
        verify(s3Client).putObject(requestCaptor.capture(), any(RequestBody.class)); // ✅ specify type
        PutObjectRequest capturedRequest = requestCaptor.getValue();

        assertEquals("test-bucket", capturedRequest.bucket());
        assertTrue(capturedRequest.key().endsWith("_test.txt")); // UUID + filename
        assertEquals("text/plain", capturedRequest.contentType());

        assertTrue(url.contains("https://test-bucket.s3.amazonaws.com/"));
    }


    @Test
    void testUploadFile_ThrowsIOException() throws IOException {
        // Arrange: simulate a MultipartFile that throws IOException on getBytes()
        MultipartFile failingFile = mock(MultipartFile.class);
        when(failingFile.getOriginalFilename()).thenReturn("fail.txt");
        when(failingFile.getContentType()).thenReturn("text/plain");
        when(failingFile.getBytes()).thenThrow(new IOException("Simulated failure"));

        // Act & Assert
        assertThrows(IOException.class, () -> s3StorageService.uploadFile(failingFile));
        verifyNoInteractions(s3Client);
    }
}
