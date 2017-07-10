/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.zbxn.pub.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 文件操作工具类
 * Created by Jorstin on 2015/3/17.
 */
public class FileAccessor {
    public static final String APPS_ROOT_DIR = getExternalStorePath() + "/zbx";
    public static final String IMESSAGE_IMAGE = getExternalStorePath() + "/zbx/img";

    /**
     * 初始化应用文件夹目录
     */
    public static void initFileAccess() {
        File rootDir = new File(APPS_ROOT_DIR);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        File imageDir = new File(IMESSAGE_IMAGE);
        if (!imageDir.exists()) {
            imageDir.mkdir();
        }
    }

    /**
     * 外置存储卡的路径
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static File getTackPicFilePath() {
        File localFile = new File(IMESSAGE_IMAGE, "temp.jpg");
        System.out.println("localFile::" + localFile);
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            System.out.println("SD卡不存在");
            localFile = null;
        }
        return localFile;
    }

    /**
     * 压缩图片
     *
     * @param filePathStr
     * @return
     */
    public static String getSmallPicture(String filePathStr) {
        String filePath_Success = IMESSAGE_IMAGE + "/" + System.currentTimeMillis() + "_" + filePathStr.substring(filePathStr.lastIndexOf("/") + 1);//+1的话就不带/号了
        System.out.println("filePath_Success:" + filePath_Success);
        Bitmap bitmap = getSmallBitmap(filePathStr);

        int degree = DemoUtils.readPictureDegree(filePathStr);
        if (degree != 0) {
            bitmap = DemoUtils.degreeBitmap(bitmap, degree);
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;// 个人喜欢从100开始,
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 150) { // 压缩到小于150k
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(filePath_Success);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            return filePath_Success;
        }
        return filePath_Success;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
