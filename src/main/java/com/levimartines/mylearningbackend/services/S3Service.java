package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.exceptions.S3Exception;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;
    @Value("${s3.user.picture.bucket-name}")
    private String bucketName;

    public void uploadFile(InputStream inputStream, String fileName, String contentType) {
        log.info("Uploading file [{}]", fileName);
        try {
            long contentLength = inputStream.available();
            PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .contentLength(contentLength)
                .build();
            RequestBody requestBody = RequestBody.fromInputStream(inputStream, contentLength);
            s3Client.putObject(putRequest, requestBody);
        } catch (IOException ex) {
            log.error("Error uploading file [{}]: ", fileName);
        }
        log.info("Upload finished [{}]", fileName);
    }

    public byte[] getFile(String fileName) {
        GetObjectRequest getRequest = GetObjectRequest.builder().bucket(bucketName).key(fileName).build();
        ResponseBytes<GetObjectResponse> response;
        try {
            response = s3Client.getObject(getRequest, ResponseTransformer.toBytes());
        } catch (NoSuchKeyException ex) {
            log.warn("No file [{}] in s3 bucket", fileName);
            return new byte[0];
        }
        if (response == null) {
            log.error("Error downloading file [{}] in s3 bucket [{}]", fileName, bucketName);
            throw new S3Exception("Unable to retrieve file from s3");
        }
        return response.asByteArray();
    }

}
