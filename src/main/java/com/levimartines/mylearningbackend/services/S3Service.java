package com.levimartines.mylearningbackend.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.levimartines.mylearningbackend.exceptions.S3Exception;

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
    private final AmazonS3 s3Client;
    @Value("${s3.user.picture.bucket-name}")
    private String bucketName;

    public void uploadFile(InputStream inputStream, String fileName, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        try {
            objectMetadata.setContentLength(inputStream.available());
        } catch (IOException ex) {
            log.error("Error retrieving content lenght from uploaded file [{}]: ", fileName);
        }
        log.info("Uploading file [{}]", fileName);
        s3Client.putObject(bucketName, fileName, inputStream, objectMetadata);
        log.info("Upload finished [{}]", fileName);
    }

    public byte[] getFile(String fileName) {
        S3Object object = s3Client.getObject(bucketName, fileName);
        if (object == null) {
            log.error("No file [{}] in s3 bucket", fileName);
            throw new S3Exception("Unable to retrieve file from s3");
        }
        try {
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (IOException e) {
            log.error("Error converting file [{}] to byte array", fileName);
            throw new S3Exception("Unable to retrieve file from s3");
        }
    }


}
