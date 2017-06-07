package com.example.rishabh.amazonutility;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CryptoConfiguration;
import com.amazonaws.services.s3.model.KMSEncryptionMaterials;
import com.amazonaws.services.s3.model.KMSEncryptionMaterialsProvider;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String s3BucketName = "cc-clinic-fs";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-south-1:0186c083-2e5b-4df9-81d4-6ef80f30d0b4", // Identity Pool ID
                Regions.AP_SOUTH_1 // Region
        );


      /*  final KMSEncryptionMaterialsProvider materialProvider = new KMSEncryptionMaterialsProvider();
        final CryptoConfiguration cryptoConfiguration = new CryptoConfiguration()
                .withAwsKmsRegion(Region
                        .getRegion(Regions.US_EAST_1));*/


        AmazonS3 s3Client = new AmazonS3Client(credentialsProvider) ;


        TransferUtility transferUtility = new TransferUtility(s3Client , getApplicationContext()) ;

        File file = new File("/storage/emulated/0/Pictures/Screenshots/oops.png");

        File fileDownloaded = new File("/storage/emulated/0/Pictures/Screenshots/oopsDownload.png");


        String s = "16385c44-e290-4196-a0cc-ea85fa938f68/FileVault/Personal/oops.png";

        TransferObserver transferObserver = transferUtility.upload(s3BucketName,s,file) ;

        transferObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                Log.e("Rishabh", "State := "+state.toString());

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);

                Log.e("Rishabh","id "+id);

                Log.e("Rishabh","bytes total "+bytesTotal);

                Log.e("Rishabh","percentage := "+percentage);

                if(percentage == 100) {
                    Log.e("Rishabh","success");
                }

            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("Rishabh","exception := "+ex);
            }
        });


      /*  TransferObserver transferObserverDownload = transferUtility.download(s3BucketName,s,fileDownloaded);

        transferObserverDownload.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("Rishabh", "download State := "+state.toString());
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);

                Log.e("Rishabh","download id "+id);

                Log.e("Rishabh","download bytes total "+bytesTotal);

                Log.e("Rishabh","download percentage := "+percentage);

                if(percentage == 100) {
                    Log.e("Rishabh","download success");
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("Rishabh","download exception := "+ex);
            }
        });*/

    }
}
