package com.fesi.mukitlist.api.service.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private String preSignUrl;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile, String s3FileName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objectMetadata);
        return URLDecoder.decode(amazonS3.getUrl(bucket, s3FileName).toString(), "UTF-8");
    }

    public void delete (String keyName) {
        try {
            amazonS3.deleteObject(bucket, keyName);
        } catch (AmazonServiceException e) {
            log.error(e.toString());
        }
    }

    /*
    * 파일의 presigned URL 반환
    */
    public String getPresignedUrl(String keyName) {
        // presigned URL이 유효하게 동작할 만료기한 설정 (2분)
        Date expiration = new Date();
        Long expirationTimeMillis = expiration.getTime();
        expirationTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expirationTimeMillis);

        try {
            // presigned URL 발급
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, keyName)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);
            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            preSignUrl = url.toString();
        } catch (Exception e) {
            log.error(e.toString());
        }
        return preSignUrl;
    }
}
