package com.hzq.lib.design.hybrid.utils;

import com.hzq.lib.design.R;
import com.hzq.lib.design.hybrid.HybridConstants;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:
 */

public class IconUtil {
    public static final int INVALID_ID = -1;

    public static int getIconId(String id){
        int resId = INVALID_ID;
        switch (id){
            case HybridConstants.IconResourceName.ADD:
                resId = R.drawable.ic_add_white;
                break;
            case HybridConstants.IconResourceName.SEARCH:
                resId = R.drawable.ic_search_white;
                break;
            case HybridConstants.IconResourceName.MORE:
                resId = R.drawable.ic_more_white;
                break;
        }
        return resId;
    }
}
