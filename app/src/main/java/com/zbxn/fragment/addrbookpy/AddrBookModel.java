package com.zbxn.fragment.addrbookpy;

import com.zbxn.pub.bean.Contacts;
import com.zbxn.pub.frame.mvp.AbsBaseModel;
import com.zbxn.pub.frame.mvp.base.ModelCallback;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.JsonUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * @author GISirFive
 * @time 2016/8/8
 */
public class AddrBookModel extends AbsBaseModel {

    public AddrBookModel(ModelCallback callback) {
        super(callback);
    }

    @Override
    protected RequestResult getResultSuccess(RequestUtils.Code code, JSONObject response) {
        RequestResult result = new RequestResult(0);
        try {
            switch (code) {
                case ADDRESSBOOK_LIST:
                    if ("0".equals(response.optString(Response.SUCCESS, "1"))) {
//                    if (response.optBoolean(Response.SUCCESS, false)) {
                        List<Contacts> list = JsonUtil.fromJsonList(response.optString(Response.ROWS, null), Contacts.class);
                        result.obj1 = list;
                    } else {
                        return getResultFailure(code, response);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected RequestResult getResultFailure(RequestUtils.Code code, JSONObject error) {
        RequestResult result = new RequestResult(1);
        try {
            switch (code) {
                case ADDRESSBOOK_LIST:
                    String message = error.optString(Response.MSG, "请求数据失败");
                    result.message = "请求数据失败";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
