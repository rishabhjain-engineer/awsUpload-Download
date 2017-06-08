package com.example.rishabh.amazonutility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.SSEAwsKeyManagementParams;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String s3BucketName = "cc-clinic-fs";
        String s = "16385c44-e290-4196-a0cc-ea85fa938f68/FileVault/Personal/oops.png";
        File file = new File("/storage/emulated/0/Pictures/Screenshots/oops.png");

        File fileDownloaded = new File("/storage/emulated/0/Pictures/oops.png");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:0186c083-2e5b-4df9-81d4-6ef80f30d0b4", // Identity Pool ID
                Regions.AP_SOUTH_1 // Region
        );


        PutObjectRequest putRequest = new PutObjectRequest(s3BucketName,
                s, file).withSSEAwsKeyManagementParams(new SSEAwsKeyManagementParams());


        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider);
        s3Client.setObjectAcl(s3BucketName, s, CannedAccessControlList.PublicRead);
        s3Client.putObject(putRequest);


        TransferUtility transferUtility = new TransferUtility(s3Client, getApplicationContext());


        TransferObserver transferObserver = transferUtility.upload(s3BucketName, s, file);

        transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                Log.e("Rishabh", "State := " + state.toString());

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent / bytesTotal * 100);

                Log.e("Rishabh", "id " + id);

                Log.e("Rishabh", "bytes total " + bytesTotal);

                Log.e("Rishabh", "percentage := " + percentage);

                if (percentage == 100) {
                    Log.e("Rishabh", "success");
                }

            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("Rishabh", "exception := " + ex);
            }
        });


        TransferObserver transferObserverDownload = transferUtility.download(s3BucketName, s, fileDownloaded);

        transferObserverDownload.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("Rishabh", "download State := " + state.toString());
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent / bytesTotal * 100);

                Log.e("Rishabh", "download id " + id);

                Log.e("Rishabh", "download bytes total " + bytesTotal);

                Log.e("Rishabh", "download percentage := " + percentage);

                if (percentage == 100) {
                    Log.e("Rishabh", "download success");
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("Rishabh", "download exception := " + ex);
            }
        });


    }
}
