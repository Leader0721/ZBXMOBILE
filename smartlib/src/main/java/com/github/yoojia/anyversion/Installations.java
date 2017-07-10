package com.github.yoojia.anyversion;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 */
class Installations {

    private final BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (!Downloads.KEEPS.contains(reference)) return;
            // 下载完成，自动安装
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(reference);
            DownloadManager download = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            Cursor cursor = download.query(query);
            try{
                if (cursor.moveToFirst()) {
                    int fileNameIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                    String fileName = cursor.getString(fileNameIdx);
                    if (fileName.endsWith(".apk")){
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(install);
                    }
                }
            }finally {
                cursor.close();
            }

        }
    };

    public void register(Context context){
        Preconditions.requiredMainUIThread();
        context.getApplicationContext().registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void unregister(Context context){
        Preconditions.requiredMainUIThread();
        context.getApplicationContext().unregisterReceiver(downloadReceiver);
    }
}
