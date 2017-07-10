package com.zbxn.activity.createbulletin;

import com.zbxn.init.http.RequestParams;
import com.zbxn.pub.application.BaseApp;
import com.zbxn.pub.frame.mvp.AbsBasePresenterOld;
import com.zbxn.pub.frame.mvp.base.RequestResult;
import com.zbxn.pub.http.RequestUtils;
import com.zbxn.pub.utils.MyToast;

import org.json.JSONObject;

import utils.PreferencesUtils;

/**
 * Created by Administrator on 2016/8/29.
 */
public class CreateBulletinPresenter extends AbsBasePresenterOld<ICreateBulletin> {

    private CreatBulletinModel bulletinModel;

    public CreateBulletinPresenter(ICreateBulletin controller) {
        super(controller);
        bulletinModel = new CreatBulletinModel(this);
    }

    @Override
    public void onSuccess(RequestUtils.Code code, JSONObject response, RequestResult result) {
        mController.finishForResult(true);
    }

    @Override
    public void onFailure(RequestUtils.Code code, JSONObject error, RequestResult result) {
//        mController.message().show("发布失败");
        MyToast.showToast("发布失败");
    }

    public void toSubmit() {
        String title = mController.getBulletinTitle();
        String content = mController.getBulletinContent();
        int label = mController.getLabel() + 1;
        int type = mController.getType() + 1;
        String attachmentguid = mController.getAttachmentguid();
        String[] persons = mController.getPersons();
        int isTop = mController.getIsTop();
        String date = mController.getTopTime();
        int allowComment = mController.getAllowComment();

        if (persons == null) {
//            mController.message().show("接收人不能为空");
            MyToast.showToast("接收人不能为空");
        } else {
            if (title.isEmpty()) {
//                mController.message().show("标题不能为空");
                MyToast.showToast("标题不能为空");
            } else {
                if (content.isEmpty()) {
//                    mController.message().show("内容不能为空");
                    MyToast.showToast("内容不能为空");
                } else {
                    mController.loading().show();
                    RequestParams params = new RequestParams();
                    params.put("title", title);
                    params.put("content", content);
                    params.put("label", label);
                    params.put("type", type);
                    params.put("attachmentguid", attachmentguid);
                    params.put("persons", persons);
                    params.put("istop", isTop);
                    params.put("time", date);
                    params.put("allowComment", allowComment);
                    //将用户输入的账号保存起来，以便下次使用
                    String ssid = PreferencesUtils.getString(BaseApp.getContext(), "ssid");
                    params.put("tokenid", ssid);
                    System.out.println("params.toString():"+params.toString());
                    bulletinModel.post(RequestUtils.Code.CREATE_BULLETIN, params);
                }
            }
        }
    }

}
