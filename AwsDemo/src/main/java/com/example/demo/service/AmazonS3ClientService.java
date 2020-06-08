package com.example.demo.service;

import java.io.ByteArrayOutputStream;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3ClientService {
	
	void uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess,String name);

    void deleteFileFromS3Bucket(String fileName);
    public ByteArrayOutputStream downloadFile(String keyName);
    

	
}
