package com.zbxn.pub.frame.mvc;

public interface IVisibleControl {

    /**
     * 该窗体是否正在显示
     *
     * @return
     */
    public boolean isShowing();

    /**
     * 设置该窗体是否显示
     *
     * @param b 显示/隐藏
     *          <b>true - </b> 显示 对应 View.VISIBLE<br>
     *          <b>false - </b> 隐藏 对应 View.GONE
     */
    public void setVisibility(boolean b);

    /**
     * 显示该窗体
     */
    public void show();

    /**
     * 隐藏该窗体
     */
    public void hide();

}
