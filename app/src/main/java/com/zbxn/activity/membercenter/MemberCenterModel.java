package com.zbxn.activity.membercenter;

import com.zbxn.bean.Member;
import com.zbxn.pub.frame.mvp.AbsBaseModel;
import com.zbxn.pub.frame.mvp.base.ModelCallback;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.http.Response;
import com.zbxn.pub.utils.JsonUtil;

import org.json.JSONObject;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/8/8 15:59
 */
public class MemberCenterModel extends AbsBaseModel {
    public MemberCenterModel(ModelCallback callback) {

        super(callback);
    }

    @Override
    protected RequestResult getResultSuccess(RequestUtils.Code code, JSONObject response) {

        RequestResult requestResult = new RequestResult(0);
        try {
            switch (code) {
                case USER_UPDATE:
//                    if (response.optBoolean(Response.SUCCESS, false)) {
                    if ("0".equals(response.optString(Response.SUCCESS, "1"))) {
                        // 获取返回的值付给data
                        String data = response.optString(Response.DATA, null);
                        // 解析返回的值
                        Member member = JsonUtil
                                .fromJsonString(data, Member.class);
                        // 保存到本地
                        Member.save(member);
                        requestResult.message = "修改成功";

                        // showToast("修改成功", Toast.LENGTH_SHORT);

                    }
                    break;

                case USER_DETAIL:
                    if ("0".equals(response.optString(Response.SUCCESS, "1"))) {
//                    if (response.optBoolean("success", false)) {
                        String data = response.optString(Response.DATA, null);
                        Member member = JsonUtil
                                .fromJsonString(data, Member.class);

                    }

                    break;

                default:
                    break;
            }
        } catch (Exception e) {

        } finally {
            /*if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();*/
        }
        return null;
    }



    @Override
    protected RequestResult getResultFailure(RequestUtils.Code code, JSONObject error) {
        RequestResult requestResult=new RequestResult(0);

        try {
            switch (code) {
                case USER_UPDATE:
                    if (error.optBoolean(Response.SUCCESS, false)) {
                        requestResult.message="修改失败";
                        //showToast("修改失败", Toast.LENGTH_SHORT);

                    }
                    break;

                default:
                    break;
            }
        } catch (Exception e) {

        } finally {
           /* if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();*/
        }
        return null;
    }
}
