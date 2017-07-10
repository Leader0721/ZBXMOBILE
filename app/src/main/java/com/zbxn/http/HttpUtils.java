package com.zbxn.http;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HttpUtils {

    private static final String TAG = "HttpUtils";
    private final static int TIMEOUT_SOCKET = 20000;

    public static String sessionId = null;

    public static String httpGet(String actionUrl, Map<String, String> fields) throws Exception {
        String strResult = null;
        String requestUrl = strResult;
        if (fields.size() > 0) {
            StringBuilder queryBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                //String urlValue = URLEncoder.encode(entry.getValue(), "UTF-8");
                queryBuilder.append(entry.getKey() + "=" + entry.getValue()
                        + "&");
            }
            requestUrl = actionUrl
                    + "?"
                    + queryBuilder.toString().substring(0,
                    queryBuilder.length() - 1);
            Log.d("requestUrl", requestUrl);
            requestUrl = requestUrl.replaceAll(" ", "%20");
            System.out.println("请求地址" + requestUrl);
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl);

        //第一次一般是还未被赋值，若有值则将SessionId发给服务器
        if (null != sessionId) {
            httpGet.setHeader("cookie", "JSESSIONID=" + sessionId);
        }

        // 设置超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_SOCKET);
        // 读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SOCKET);

        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity(),
                        HTTP.UTF_8);
                CookieStore mCookieStore = httpClient.getCookieStore();
                List<Cookie> cookies = mCookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    //这里是读取Cookie['PHPSESSID']的值存在静态变量中，保证每次都是同一个值
                    if ("JSESSIONID".equals(cookies.get(i).getName())) {
                        sessionId = cookies.get(i).getValue();
                        break;
                    }
                }
            } else {
                strResult = "11";
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            strResult = "22";
            throw AppException.http(e);
        } catch (IOException e) {
            e.printStackTrace();
            strResult = "33";
            throw AppException.network(e);
        } finally {
            if (httpClient != null)
                httpClient.getConnectionManager().shutdown();
        }
        System.out.println(strResult);
        return strResult;
    }

    // 无参的url地址
    public static String httpGet(String actionUrl) throws AppException {
        String strResult = null;
        String requestUrl = strResult;
        requestUrl = actionUrl;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(requestUrl);

        // 设置超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_SOCKET);
        // 读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SOCKET);

        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity(),
                        HTTP.UTF_8);
            } else {
                throw AppException.http(statusCode);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw AppException.http(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw AppException.network(e);
        } finally {
            if (httpClient != null)
                httpClient.getConnectionManager().shutdown();
        }
        return strResult;
    }

    public static String httpPost(String actionUrl, Map<String, String> fields)
            throws AppException {
        System.out.println("HttpUtils-->httpPost,actionUrl=" + actionUrl);
        String strResult = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost httpPost = new HttpPost(actionUrl);
            HttpParams httpParams = new BasicHttpParams();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            // 建立一个NameValuePair数组，用于存储欲传送的参数
//            httpPost.addHeader("Content-type","application/json; charset=utf-8");
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));

//            manager.responseSerializer.acceptableContentTypes = [NSSet setWithObjects:@"application/json",@"text/html",@"text/plain",nil];

            // 设置超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_SOCKET);
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SOCKET);
            if (null != fields) {
                for (Map.Entry<String, String> entry : fields.entrySet()) {
//                     nameValuePair.add(new BasicNameValuePair(entry.getKey(),
//                     URLEncoder.encode(entry.getValue(), "UTF-8")));
                    // 去掉此处的encode，可以解决提交数据中带"/"，插入数据库出现乱码问题
                    nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    System.out.println(entry.getKey() + "," + entry.getValue());
                }

                //输出请求地址
                StringBuilder queryBuilder = new StringBuilder();
                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    queryBuilder.append(entry.getKey() + "=" + entry.getValue() + "&");
                }
                String requestUrl = actionUrl + "?" + queryBuilder.toString().substring(0, queryBuilder.length() - 1);
                Log.d("requestUrl", requestUrl);
                requestUrl = requestUrl.replaceAll(" ", "%20");
                System.out.println("请求地址" + requestUrl);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,
                    HTTP.UTF_8));
            httpPost.setParams(httpParams);

            HttpResponse httpResponse = httpClient.execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的字符串
                strResult = EntityUtils.toString(httpResponse.getEntity(),
                        HTTP.UTF_8);
            } else {
                System.out.println("poooijkghj");
            }
            System.out.println("HttpStatus StatusCode="
                    + httpResponse.getStatusLine().getStatusCode());
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            strResult = "exceptionCode=0001";
            throw AppException.socket(e);
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            strResult = "exceptionCode=0001";
            throw AppException.network(e);
        } catch (HttpHostConnectException e) {
            e.printStackTrace();
            strResult = "exceptionCode=0002";
            throw AppException.http(e);
        } catch (Exception e) {
            e.printStackTrace();
            strResult = "exceptionCode=0002";
            throw AppException.io(e);
        } finally {
            if (httpClient != null)
                httpClient.getConnectionManager().shutdown();
        }
        System.out.println("结束");
        if (strResult.contains("\n")) {
            strResult = strResult.replaceAll("\n", "");
        }
        System.out.println(TAG + ",http result=" + strResult);
        return strResult;
    }

    public static String httpDelete(String actionUrl, Map<String, String> fields) {
        String strResult = null;
        String requestUrl = strResult;
        if (fields.size() > 0) {
            StringBuilder queryBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                queryBuilder.append(entry.getKey() + "=" + entry.getValue()
                        + "&");
            }
            requestUrl = actionUrl
                    + "?"
                    + queryBuilder.toString().substring(0,
                    queryBuilder.length() - 1);
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpDelete httpDelete = new HttpDelete(requestUrl);
        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpDelete);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(httpResponse.getEntity(),
                        HTTP.UTF_8);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null)
                httpClient.getConnectionManager().shutdown();
        }
        return strResult;
    }

    public static String httpPut(String actionUrl, Map<String, String> fields)
            throws AppException {
        String strResult = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPut httpPut = new HttpPut(actionUrl);
        HttpParams httpParams = new BasicHttpParams();
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

        // 设置超时
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_SOCKET);
        // 读取超时
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_SOCKET);

        if (null != fields) {
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
        }
        HttpResponse httpResponse;
        try {
            httpPut.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
            httpPut.setParams(httpParams);
            httpResponse = httpClient.execute(httpPut);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                strResult = EntityUtils.toString(httpResponse.getEntity(),
                        HTTP.UTF_8);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw AppException.io(e);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw AppException.http(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw AppException.io(e);
        } finally {
            if (httpClient != null)
                httpClient.getConnectionManager().shutdown();
        }
        return strResult;
    }

    // 上传方法
    public static String post(String actionUrl, Map<String, String> params,
                              Map<String, File> files) throws IOException {
        System.out.println("actionUrl=" + actionUrl);
        String boundary = UUID.randomUUID().toString();
        String prefix = "--", linend = "\r\n";
        String multipart_from_data = "multipart/form-data";
        String charset = "UTF-8";

        URL url = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // conn.setReadTimeout(5 * 1000);// 缓存的最长时间
        // conn.setConnectTimeout(TIMEOUT_CONNECTION);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);// 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charset", charset);
        conn.setRequestProperty("Content-Type", multipart_from_data
                + ";boundary=" + boundary);

        StringBuilder strBuilder = new StringBuilder();
        if (null != params) {
            // 首先组拼文本类型的数据
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println("key=" + entry.getKey() + ",value="
                        + entry.getValue());
                strBuilder.append(prefix);
                strBuilder.append(boundary);
                strBuilder.append(linend);
                strBuilder.append("Content-Disposition:form-data;name=\""
                        + entry.getKey() + "\"" + linend);
                strBuilder.append("Content-Type:text/plain;charset=" + charset
                        + linend);
                strBuilder.append("Content-Transfer-Encoding:8bit;" + linend);
                strBuilder.append(linend);
                // strBuilder.append(URLEncoder.encode(entry.getValue(),
                // HTTP.UTF_8));
                strBuilder.append(entry.getValue());
                strBuilder.append(linend);
            }
        }

        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        dos.write(strBuilder.toString().getBytes());

        if (null != files) {
            for (Map.Entry<String, File> file : files.entrySet()) {
                System.out.println("key=" + file.getKey() + ",value="
                        + file.getValue().getAbsolutePath());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(prefix);
                sb2.append(boundary);
                sb2.append(linend);
                sb2.append("Content-Disposition:form-data;name=\""
                        + file.getKey() + "" + "\";filename=\""
                        + file.getValue().getName() + "\"" + linend);
                sb2.append("Content-Type:application/octet-stream;charset="
                        + charset + linend);
                sb2.append(linend);
                dos.write(sb2.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue()
                        .getAbsolutePath());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    dos.write(buffer, 0, len);
                }
                is.close();
                dos.write(linend.getBytes());
            }
        }
        // 请求结束标记
        byte[] end_data = (prefix + boundary + prefix + linend).getBytes();
        dos.write(end_data);
        dos.flush();

        // 得到响应码
        int res = conn.getResponseCode();
        InputStream in = null;
        StringBuffer buffer = new StringBuffer();
        if (res == 200) {
            System.out.println("HttpUtils ,responseCode=" + res);
            in = conn.getInputStream();
            BufferedReader in2 = new BufferedReader(new InputStreamReader(in,
                    "UTF-8"));

            String line = "";
            while ((line = in2.readLine()) != null) {
                buffer.append(line);
            }
        } else {
            System.out.println("HttpUtils request error ,responseCode=" + res);
        }
        System.out.println("HttpUtils,result=" + buffer.toString());
        return buffer.toString();
    }

    public static String doHttpPost(String requestUrl,
                                    Map<String, String> paramMap) {
        try {
            URL url = new URL(requestUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");

            conn.connect();

            StringBuffer sb = new StringBuffer();

            if (paramMap != null && !paramMap.keySet().isEmpty()) {
                for (String key : paramMap.keySet()) {
                    sb.append("&");
                    sb.append(key);
                    sb.append("=");
                    sb.append(URLEncoder.encode(paramMap.get(key), "UTF-8"));

                }
            }

            String params = sb.toString();
            System.out.println(params);
            OutputStreamWriter writer = null;

            if (params != null && !"".equals(params)) {
                writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(params.substring(1));
                writer.flush();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            char[] cbuf = new char[1024];
            int count;
            sb = new StringBuffer();

            while ((count = reader.read(cbuf)) > 0) {
                sb.append(cbuf, 0, count);
            }

            if (reader != null) {
                reader.close();
            }

            if (writer != null) {
                writer.close();
            }

            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}
