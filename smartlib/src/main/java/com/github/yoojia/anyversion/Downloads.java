package com.github.yoojia.anyversion;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 */
class Downloads {

    static final Set<Long> KEEPS = new HashSet<Long>();

    public void destroy(Context context){
        DownloadManager download = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        for (long id : KEEPS){
            download.remove(id);
            KEEPS.remove(id);
        }
    }

    public long submit(Context context, Version version){
        DownloadManager download = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(version.URL);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(version.name);
        long id = download.enqueue(request);
        KEEPS.add(id);
        return id;
    }
}
