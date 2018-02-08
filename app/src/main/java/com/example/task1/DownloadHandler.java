package com.example.task1;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;


/**
 * Created by zayed on 08-Feb-18.
 */

public class DownloadHandler {
    private DownloadManager downloadManager;
    private long downloadID;
    private Handler handlerUpdate;
    private IntentFilter intentFilter;
    private Context context;
    static ProgressDialog progressDialog;
    private Runnable runnableUpdate=new Runnable() {
        @Override
        public void run() {
            if(done)
                return;
            updateStatus();
            handlerUpdate.postDelayed(this,1000);

        }
    };
    private volatile boolean done;



    public DownloadHandler(Context context) {
        done=false;
        this.context = context;

        intentFilter=new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.getApplicationContext().registerReceiver(new DownloadReceiver(),intentFilter);

        progressDialog=new ProgressDialog(context);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

    }

    public void downloadFile(String DOWNLOAD_URL)
    {
        Uri uriDownload=Uri.parse(DOWNLOAD_URL);
        downloadFileFromURL(uriDownload);
        progressDialog.show();
    }

    private void downloadFileFromURL(Uri uri) {
        downloadManager=(DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request= new DownloadManager.Request(uri);

        request.setTitle("Download Manager");
        request.setDescription("downloading...");
        request.setVisibleInDownloadsUi(true);

        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"5MB.zip");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"SampleVideo_1280x720_30mb.mp4");

        downloadID = downloadManager.enqueue(request);

        handlerUpdate = new Handler();
        handlerUpdate.post(runnableUpdate);
    }

     public void  updateStatus(){

        DownloadManager.Query query=new DownloadManager.Query();
        query.setFilterById(downloadID);

        Cursor cursor=downloadManager.query(query);
        if (cursor.moveToFirst())
        {
            int colStatusIndex=cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int colReasonIndex=cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int coldownloadedSoFar=cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            int colfileSize=cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int colFileIndex=cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);

            int status=cursor.getInt(colStatusIndex);
            int reason=cursor.getInt(colReasonIndex);

            int downloadedSoFar=cursor.getInt(coldownloadedSoFar);
            int fileSize=cursor.getInt(colfileSize);

            //file size return -1 , so filesize is counted manually


            progressDialog.setProgress((downloadedSoFar/250000));

            String statusTxt=null;
            String reasonStr=null;

            switch (status){
                case DownloadManager.STATUS_FAILED:
                statusTxt="STATUS_FAILED";
                done=true;

                switch (reason)
                {
                    case DownloadManager.ERROR_CANNOT_RESUME:
                        reasonStr="ERROR_CANNOT_RESUME";
                        break;

                    case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                        reasonStr="RROR_DEVICE_NOT_FOUND";
                        break;

                    case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                        reasonStr="ERROR_FILE_ALREADY_EXISTS";
                        break;

                    case DownloadManager.ERROR_FILE_ERROR:
                        reasonStr="ERROR_FILE_ERROR";
                        break;

                    case DownloadManager.ERROR_HTTP_DATA_ERROR:
                        reasonStr="ERROR_HTTP_DATA_ERROR";
                        break;

                    case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                        reasonStr="ERROR_INSUFFICIENT_SPACE";
                        break;

                    case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                        reasonStr="ERROR_UNHANDLED_HTTP_CODE";
                        break;

                    case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                        reasonStr="ERROR_TOO_MANY_REDIRECTS";
                        break;

                    case DownloadManager.ERROR_UNKNOWN:
                        reasonStr="ERROR_UNKNOWN";
                        break;
                }
                break;

                case DownloadManager.STATUS_PAUSED:
                    statusTxt="STATUS_PAUSED";
                    switch (reason)
                    {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            reasonStr="PAUSED_QUEUED_FOR_WIFI";
                            break;

                        case DownloadManager.PAUSED_UNKNOWN:
                            reasonStr="PAUSED_UNKNOWN";
                            break;

                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            reasonStr="PAUSED_WAITING_FOR_NETWORK";
                            break;

                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            reasonStr="PAUSED_WAITING_TO_RETRY";
                            break;
                    }
                    break;

                case DownloadManager.STATUS_PENDING:
                    statusTxt="STATUS_PENDING";
                    break;

                case DownloadManager.STATUS_RUNNING:
                    statusTxt="STATUS_RUNNING";
                    break;

                case DownloadManager.STATUS_SUCCESSFUL:
                    statusTxt="STATUS_SUCCESSFUL";
                    done=true;
                    break;

            }
            if(reasonStr != null)
            Toast.makeText(context,reasonStr,Toast.LENGTH_SHORT).show();

        }
        }

}
