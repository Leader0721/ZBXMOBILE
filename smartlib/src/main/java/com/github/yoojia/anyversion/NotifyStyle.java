package com.github.yoojia.anyversion;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 * 发现新版本的处理方式
 */
public enum NotifyStyle {

    /**
     * 使用广播机制来处理新版本
     */
    Broadcast,

    /**
     * 使用系统级弹出窗口来处理新版本
     */
    Dialog,

    /**
     * 使用回调接口来处理新版本
     */
    Callback,

}
