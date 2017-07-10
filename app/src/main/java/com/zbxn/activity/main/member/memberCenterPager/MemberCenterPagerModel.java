package com.zbxn.activity.main.member.memberCenterPager;

import com.zbxn.pub.frame.mvp.AbsBaseModel;
import com.zbxn.pub.frame.mvp.base.ModelCallback;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;

import org.json.JSONObject;

/**
 * 项目名称：ZBXMobile
 * 创建人：LiangHanXin
 * 创建时间：2016/8/4 9:22
 */
public class MemberCenterPagerModel extends AbsBaseModel{
    public MemberCenterPagerModel(ModelCallback callback) {
        super(callback);
    }

    @Override
    protected RequestResult getResultSuccess(RequestUtils.Code code, JSONObject response) {
        return null;
    }

    @Override
    protected RequestResult getResultFailure(RequestUtils.Code code, JSONObject error) {
        return null;
    }





}
