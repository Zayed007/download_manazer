package com.example.task1;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by zayed on 08-Feb-18.
 */

public class DownloadReceiver extends BroadcastReceiver {
    DownloadHandler downloadHandler;

    @Override
    public void onReceive(Context context, Intent intent) {

        long refID=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        Toast.makeText(context,"Download Complete",Toast.LENGTH_SHORT).show();
        MainActivity.downloadBTN.setEnabled(true);
        DownloadHandler.progressDialog.dismiss();

    }
}
