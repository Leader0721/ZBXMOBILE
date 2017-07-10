package com.zbxn.pub.http;

import com.orhanobut.logger.Logger;
import com.zbxn.pub.utils.ConfigUtils;
import com.zbxn.pub.utils.ConfigUtils.KEY;

/**
 * 接口
 *
 * @author GISirFive
 */
public class RequestUtils {

    /**
     * 接口标识
     */
    public enum Code {
        /**
         * 检查版本更新
         */
        APP_UPDATE,

        /**
         * 检查手机号是否存在
         */
        MOBILE_EXIST,

        /**
         * 用户登录
         */
        USER_LOGIN,

        /**
         * 用户退出登录
         */
        USER_LOGOUT,

        /**
         * 用户注册
         */
        USER_REGISTER,

        /**
         * 更新个人信息
         */
        USER_UPDATE,

        /**
         * 用户详情
         */
        USER_DETAIL,

        /**
         * 修改密码
         */
        USER_RESET_PASSWORD,

        /**
         * 日志列表，顶部下拉刷新
         */
        WORKBLOG_LIST_TOP,

        /**
         * 日志列表，底部加载更多
         */
        WORKBLOG_LIST_BOTTOM,

        /**
         * 查询用户当天是否写了日志
         */
        WORKBLOG_CHECKTODAY,

        /**
         * 保存/更新日志
         */
        WORKBLOG_SAVE,

        /**
         * 通知公告列表，顶部下拉刷新
         */
        BULLETIN_LIST_TOP,

        /**
         * 通知公告列表，底部加载更多
         */
        BULLETIN_LIST_BOTTOM,

        /**
         * 通知公告详情
         */
        BULLETIN_DETAIL,

        /**
         * 将通知公告标记为已读
         */
        BULLETIN_SETREAD,

        /**
         * 更改某条消息的收藏状态
         */
        ALERT_COLLECT,

        /**
         * 获取收藏列表，顶部下拉刷新
         */
        COLLECT_LIST_TOP,

        /**
         * 获取收藏列表，上拉加载更多
         */
        COLLECT_LIST_BOTTOM,

        /**
         * 通讯录列表
         */
        ADDRESSBOOK_LIST,

        /**
         * 提交意见反馈
         */
        SUBMIT_FEEDBACK,

        /**
         * 获取评论列表
         */
        COMMENT_LIST,

        /**
         * 分页获取评论
         */
        COMMENT_LISTBOTTOM,

        /**
         * 评论日志，公告
         */
        COMMENT_MESSAGE,

        /**
         * 回复某人评论
         */
        COMMENT_USER,

        /**
         * 发布公告
         */
        CREATE_BULLETIN,
        /**
         * 根据权限动态加载应用
         */
        DYNAMIC_APP,

        /**
         * 动态获取轮播图
         */
        ADVERT_IMAGE,

        /**
         * 取消置顶公告
         */
        UPDATETOP_BULLETIN,

        /**
         * 删除公告
         */
        DELETE_BULLETIN
    }

    /**
     * 根据接口标识，获取请求地址
     *
     * @param flag
     * @return
     */
    public static String getUrlWithFlag(Code flag) {

        String server = ConfigUtils.getConfig(KEY.server);
        String api = "";
        switch (flag) {
            case APP_UPDATE:
                api = "/mobileVersion/getNewVersion.do?flag=Android";
                break;
            case USER_LOGIN:
                api = "/baseUser/doLogin.do";
                break;
            case USER_LOGOUT:
                api = "/baseUser/logout.do";
                break;
            case USER_REGISTER:
                api = "/member/save.do";
                break;
            case MOBILE_EXIST:
                api = "/member/checkPhone.do";
                break;
            case USER_RESET_PASSWORD:
                api = "/baseUser/updatePassword.do";
                break;
            case USER_UPDATE:
                api = "/baseUser/updateUserInfo.do";
                break;
            case USER_DETAIL:
                api = "/member/getId.do";
                break;
            case WORKBLOG_LIST_TOP:
            case WORKBLOG_LIST_BOTTOM:
                api = "/oaWorkBlog/dataList.do";
                break;
            case WORKBLOG_CHECKTODAY:
                api = "/oaWorkBlog/getBlogToday.do";
                break;
            case WORKBLOG_SAVE:
                api = "/oaWorkBlog/save.do";
                break;
            case BULLETIN_LIST_TOP:
            case BULLETIN_LIST_BOTTOM:
//                api = "/oaBulletin/getSelfDataList.do";
                api = "/oaAlert/findBaseUserOaAlertType.do";
                break;
            case BULLETIN_DETAIL:
                api = "/oaBulletin/getDetailById.do";
                break;
            case BULLETIN_SETREAD:
                api = "/oaBulletin/updateReadState.do";
                break;
            case ALERT_COLLECT:
                api = "/oaAlertUser/collect.do";
                break;
            case COLLECT_LIST_TOP:
            case COLLECT_LIST_BOTTOM:
                api = "/oaBulletin/getCollectList.do";
                break;
            case ADDRESSBOOK_LIST:
                api = "/baseUser/findDepartmentContacts.do";
                break;
            case SUBMIT_FEEDBACK:
                api = "/baseSuggest/suggest.do";
                break;
            case COMMENT_LIST:
            case COMMENT_LISTBOTTOM:
                api = "/baseComment/dataList.do";
                break;
            case COMMENT_MESSAGE:
                api = "/baseComment/save.do";
                break;
            case COMMENT_USER:
                api = "/baseReply/save.do";
                break;
            case CREATE_BULLETIN:
                api = "/oaBulletin/publishBulletin.do";
                break;
            case DYNAMIC_APP:
                api = "/applyIcon/dataList.do";
                break;
            case ADVERT_IMAGE:
                api = "/mobilePicture/dataList.do";
                break;
            case UPDATETOP_BULLETIN:
                api = "/oaBulletin/updateIsTop.do";
                break;
            case DELETE_BULLETIN:
                api = "/oaBulletin/delete.do";
                break;
            default:
                break;
        }
        Logger.i(server + api);
        return server + api;
    }

}
