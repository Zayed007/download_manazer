package com.example.task1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static Button downloadBTN;
    private NetworkAccess networkAccess;
    private boolean storagePermissionGranted;
    private DownloadHandler downloadHandler;
    private final String DOWNLOAD_URL="http://dropbox.sandbox2000.com/intrvw/SampleVideo_1280x720_30mb.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadBTN=findViewById(R.id.BTN_download);
        networkAccess=new NetworkAccess(MainActivity.this);
        storagePermissionGranted=false;
        downloadHandler=new DownloadHandler(MainActivity.this);

        storagePermissionCheck();                                                                    //getting user's storage permission

        downloadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(networkAccess.haveNetworkConnection()){
                    if(storagePermissionGranted){
                        //do the code here
                        downloadBTN.setEnabled(false);
                        downloadHandler.downloadFile(DOWNLOAD_URL);

                    }
                    else
                    {
                        storagePermissionCheck();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"Internet Required!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    void storagePermissionCheck(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
                return;
            }

        }
        storagePermissionGranted=true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            storagePermissionGranted=true;
        }
        else
        {
            storagePermissionGranted=false;
            storagePermissionCheck();
        }
    }


}
