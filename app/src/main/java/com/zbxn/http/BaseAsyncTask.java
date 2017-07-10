package com.zbxn.http;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.zbxn.activity.Main;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.data.base.BaseAsyncTaskInterface;
import com.zbxn.pub.utils.JsonUtil;
import com.zbxn.pub.utils.MyToast;

import java.util.Map;

import utils.DeviceUtils;

/**
 * 获取及解析数据
 *
 * @author Lenovo
 */
public class BaseAsyncTask extends AsyncTask<Map<String, String>, Void, Object> {
    private static BaseAsyncTask baseAsyncTask;

    private Context context;
    private boolean showProgress;
    private int funId;
    private String url;
    private BaseAsyncTaskInterface baseAsyncTaskInterface;
    private Dialog progressDialog;// 自定义加载弹框

    /**
     * 获取及解析数据构造函数
     *
     * @param context                context
     * @param showProgress           是否显示进度滚动条
     * @param funId                  区分一个页面中是哪个方法 默认0--表示这个类中只需要调用一个，所以不做区分
     * @param url                    服务器地址
     * @param baseAsyncTaskInterface 接口
     */
    public BaseAsyncTask(Context context, boolean showProgress, int funId, String url,
                         BaseAsyncTaskInterface baseAsyncTaskInterface) {
        baseAsyncTask = this;

        this.context = context;
        this.showProgress = showProgress;
        this.funId = funId;
        this.url = url;
        this.baseAsyncTaskInterface = baseAsyncTaskInterface;
    }

    /**
     * 取消task
     */
    public static void cancelTask() {
        if (baseAsyncTask != null) {
            if (!baseAsyncTask.isCancelled()) {
                baseAsyncTask.cancel(true);
            }
        }
    }

    /**
     * 异步请求后台的执行
     */
    @Override
    protected Object doInBackground(Map<String, String>... params) {
        Object result = null;

        if (!DeviceUtils.getInstance(context).hasInternet()) {//未联网
            return null;
        }

        // 提交参数
        Map<String, String> map = params[0];
        // 请求地址
        String newUrl = url;

        String resultStr;
        try {
            resultStr = HttpUtils.httpPost(newUrl, map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try {
            String success = JsonUtil.getString(resultStr + "", "success");
            String msg = JsonUtil.getString(resultStr + "", "msg");
            if ("-1".equals(success)) {
                //被踢出登录
                MyToast.showToast(msg);
                Intent intent = new Intent(BaseApp.getContext(), Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("isExit",true);
                BaseApp.getContext().startActivity(intent);
            }
        } catch (Exception e) {
            System.out.println();
        }

        // 调用activity中的解析
        try {
            result = baseAsyncTaskInterface.dataParse(funId, resultStr);
        } catch (Exception e) {
            System.out.println("解析数据出错：" + e.toString());
        }

        return result;
    }

    /**
     * 异步请求过程中UI的操作
     */
    @Override
    protected void onPreExecute() {
        /*try {
            if (showProgress) {
                if (progressDialog == null) {
                    progressDialog = new MyProgressDialog(context, true);
                }
                progressDialog.show();
            }
        } catch (Exception e) {

        }*/
        baseAsyncTaskInterface.dataSubmit(funId);
        super.onPreExecute();
    }

    /**
     * 异步请求结束
     */
    @Override
    protected void onPostExecute(Object result) {
        try {
            if (null != progressDialog) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {

        }
        if (null == result) {
            if (!DeviceUtils.getInstance(context).hasInternet()) {//未联网
                Toast.makeText(context, "无网络连接，请检查网络设置！", Toast.LENGTH_SHORT).show();
            }
            baseAsyncTaskInterface.dataError(funId);
        } else {
            baseAsyncTaskInterface.dataSuccess(funId, result);
        }

        super.onPostExecute(result);
    }

}

