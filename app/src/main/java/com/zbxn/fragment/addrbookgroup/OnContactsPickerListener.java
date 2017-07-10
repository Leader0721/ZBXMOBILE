package com.zbxn.fragment.addrbookgroup;

import com.zbxn.pub.bean.Contacts;

import java.util.List;

/**
 * 选择联系人监听
 * @author GISirFive
 * @time 2016/8/15
 */
public interface OnContactsPickerListener {

    /**
     * 选中联系人
     * @param list {@link Contacts}
     * @param  containsLoginUser 当前联系人是否包含当前登录的用户
     */
    void onSelectedContacts(List<Contacts> list, boolean containsLoginUser);

}
