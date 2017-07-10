package com.github.yoojia.anyversion;

/**
 * Created by Yoojia.Chen
 * yoojia.chen@gmail.com
 * 2015-01-04
 */
public interface Callback {

    /**
     * 新版本
     * notVersion
     * 有新版本为ture，否则false
     */
    void onVersion(Version version, boolean notVersion);
}
