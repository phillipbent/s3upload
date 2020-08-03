package com.sample.untrusted_cloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.concurrent.TimeUnit;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class App 
{
    public static void main( String[] args )
    {
    	try {
			// SECTION 1 OPTION 1: Create a S3 client with in-program credential
			//
			BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAQG5X6IKHJFFBDG4T", "JZwr66RqzASFje7FEPJozksKF3RKMawMYeuCQExT");
			// us-west-2 is AWS Oregon
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("us-west-2").withCredentials(new AWSStaticCredentialsProvider(awsCreds))
					.build();

			// SECTION 1 OPTION 2: Create a S3 client with the aws credentials set in OS (require config and crendentials in .aws folder.) Demonstrate at the end of this video.
			//
//			AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

			// SECTION 2: Put file in S3 bucket
			//
			String bucketName = "en.605.731.81.su20-untrustedcloud";
			String folderName = "folder1";
			String fileNameInS3 = "file1.txt";
			String fileNameInLocalPC = "file1.txt";
			
			long startTime = System.nanoTime();

			PutObjectRequest request = new PutObjectRequest(bucketName, folderName + "/" + fileNameInS3, new File(fileNameInLocalPC));
			s3Client.putObject(request);
			
			long endTime = System.nanoTime();
			long timeElapsed = endTime - startTime;
			System.out.println("--Uploading file done");
			System.out.println("--Execution Time:" + timeElapsed / 1000000 + "ms");
			
			// SECTION 3: Get file from S3 bucket
			//
			S3Object fullObject;
			fullObject = s3Client.getObject(new GetObjectRequest(bucketName, folderName + "/" + fileNameInS3));
			System.out.println("--Downloading file done");
			// Print file content line by line
			InputStream is = fullObject.getObjectContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			System.out.println("--File content:");
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

		} catch (IOException | AmazonS3Exception e) {

			e.printStackTrace();
		}
    }
}
