package com.zbxn.init.http;

import com.zbxn.init.http.okhttputils.model.HttpParams;
import com.zbxn.pub.http.IRequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * @author GISirFive
 * @since 2015-12-13 上午12:24:40
 */
public class RequestParams implements IRequestParams {

    private HttpParams mParams;

    public RequestParams() {
        mParams = new HttpParams();
    }

    public HttpParams getRequestParams() {
        return mParams;
    }

    public RequestParams(String key, String value) {
        mParams = new HttpParams(key, value);
    }

    @Override
    public Object getParams() {
        return mParams;
    }

    @Override
    public void put(String key, String value) {
        mParams.put(key, value);
    }

    @Override
    public void put(String key, String[] values) {
        List<String> list = Arrays.asList(values);
        mParams.putUrlParams(key, list);
    }

    @Override
    public void put(String key, File[] files) throws FileNotFoundException {
        List<File> list = Arrays.asList(files);
        mParams.putFileParams(key, list);
    }

    @Override
    public void put(String key, File file) throws FileNotFoundException {
        mParams.put(key, file);
    }

    @Deprecated
    @Override
    public void put(String key, Object value) {
//		mParams.put(key, value);
    }

    @Override
    public void put(String key, int value) {
        mParams.put(key, String.valueOf(value));
    }

    @Override
    public void put(String key, long value) {
        mParams.put(key, String.valueOf(value));
    }

    @Override
    public void remove(String key) {
        mParams.removeUrl(key);
        mParams.removeFile(key);
    }

}
