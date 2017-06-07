package com.hzq.lib.design.hybrid;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:hybrid常量聚合类,所有与js约定的常量都定义在此
 */

public final class HybridConstants {
    public static final class HandlerName{
        public static final String ALERT_DIALOG_METHOD = "showModal";
        public static final String ALERT_TOAST_METHOD = "showToast";
        public static final String ALERT_TOOLBAR_METHOD = "initToolbar";
        public static final String ALERT_CAMERA_METHOD = "startCamera";
    }

    public static final class ParamsName{
        public static final String TITLE = "title";
        public static final String MESSAGE = "message";
        public static final String STATUS = "status";
        public static final String DATA = "data";
        public static final String BUTTOM_LABELS = "buttonLabels";
        public static final String BUTTOM_INDEX = "buttonIndex";
        public static final String HIDDEN = "hidden";
        public static final String BG_COLOR = "bgColor";
        public static final String TEXT_COLOR = "textColor";
        public static final String LEFT_BUTTON = "leftButton";
        public static final String RIGHT_BUTTON = "rightButton";
        public static final String BUTTONS = "buttons";
        public static final String HANDLER_BY_JS = "handleByJs";            //执行js的返回方法
        public static final String DISABLE_REAL_BACK = "disableHardback";   //实体键返回按钮
        public static final String TEXT = "text";
        public static final String ICON = "icon";
        public static final String TYPE = "type";
        public static final String GROUP_TITLE = "groupTitle";
        public static final String GROUP_ICON = "groupIcon";
    }

    public static final class IconResourceName{
        public static final String ADD = "add";
        public static final String SEARCH = "search";
        public static final String MORE = "more";
    }

    public static final class ValueName{
        public static final String INLINE = "inline";   //菜单显示在标题栏上
    }

    public static final class RequestCode{
        public static final int RC_PERMISSION_CAMERA = 2000;
        public static final int TAKE_PHOTO_WITH_DATA = 2001;
    }
}
