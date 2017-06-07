package com.hzq.lib.design.hybrid.handler;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.hzq.lib.design.R;
import com.hzq.lib.design.hybrid.HybridConstants;
import com.hzq.lib.design.hybrid.JavaScriptBridge;
import com.hzq.lib.design.hybrid.JsCallback;
import com.hzq.lib.design.hybrid.NativeResponse;
import com.hzq.lib.design.hybrid.utils.IconUtil;
import com.hzq.lib.design.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: hezhiqiang
 * @date: 17/1/17
 * @version:
 * @description:  导航栏定制,
 * 1、对返回按钮操作:
 *      a、可以调用原生的返回方法
 *      b、可以调用js的返回方法
 *      c、可以选择禁用手机实体返回键
 * 2、设置标题,设置标题颜色,设置导航栏背景颜色
 * 3、设置右侧按钮
 *      a、支持全部显示在导航栏上
 *      b、支持收起在pop中
 *
 *
 * 对应的js调用方式如下:
 * function initToolbar(){
      var params = {"hidden":false,"leftButton":{"handleByJs":true,"disableHardback":false},"title":"这是js的标题",
            "rightButton":{"type":"collapse","buttons":[{"text":"添加","icon":"add"},{"text":"搜索","icon":"search"}],
            "groupTitle":"more","groupIcon":"more"}};
      native.callNative("initToolbar",JSON.stringify(params),"callId");
    }
 */

public abstract class ToolbarHandler extends BaseHandler {
    private Toolbar mToolbar;
    private boolean isDisableBack;  //是否禁用实体返回键
    public ToolbarHandler(Toolbar toolbar){
        this.mToolbar = toolbar;
    }

    public boolean isDisableBack() {
        return isDisableBack;
    }

    public void setDisableBack(boolean disableBack) {
        isDisableBack = disableBack;
    }

    @Override
    public void handle(JavaScriptBridge bridge, final JSONObject params, final JsCallback callback, String hName) {
        if(params == null || mToolbar == null){
            return;
        }
        try {
            /**隐藏导航栏*/
            onHiddenTitleBar(params.has(HybridConstants.ParamsName.HIDDEN)
                    && params.getBoolean(HybridConstants.ParamsName.HIDDEN));

            /**设置toolbar的背景颜色与标题颜色*/
            configToolbarColor(mToolbar, StringUtils.getString(params, HybridConstants.ParamsName.BG_COLOR),
                    StringUtils.getString(params, HybridConstants.ParamsName.TEXT_COLOR));

            /**左上角按钮*/
            if(params.has(HybridConstants.ParamsName.LEFT_BUTTON)){
                JSONObject leftbtn = params.getJSONObject(HybridConstants.ParamsName.LEFT_BUTTON);
                //执行js的返回方法
                if(leftbtn.has(HybridConstants.ParamsName.HANDLER_BY_JS)
                        && leftbtn.getBoolean(HybridConstants.ParamsName.HANDLER_BY_JS)){
                    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject data = new JSONObject();
                            try {
                                data.put("button", "left");
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                            callback.callback(new NativeResponse(data));
                        }
                    });
                }else{ //执行native返回方法
                    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                }

                /**设置是否禁用实体键返回按钮*/
                onDisableBack(leftbtn.has(HybridConstants.ParamsName.DISABLE_REAL_BACK) &&
                                leftbtn.getBoolean(HybridConstants.ParamsName.DISABLE_REAL_BACK));

            }

            /**设置title*/
            if(params.has(HybridConstants.ParamsName.TITLE)){
                mToolbar.setTitle(params.getString(HybridConstants.ParamsName.TITLE));
            }

            /**设置右上角菜单*/
            final Menu menu = mToolbar.getMenu();
            if(menu != null && params.has(HybridConstants.ParamsName.RIGHT_BUTTON)){
                mToolbar.post(new Runnable() {
                    @Override
                    public void run() {
                        menu.clear();
                        try {
                            JSONObject right = params.getJSONObject(HybridConstants.ParamsName.RIGHT_BUTTON);
                            JSONArray menus = right.getJSONArray(HybridConstants.ParamsName.BUTTONS);
                            boolean collapse = true;    //默认菜单收起
                            if(right.has(HybridConstants.ParamsName.TYPE) &&
                                    HybridConstants.ValueName.INLINE.equals(right.getString(HybridConstants.ParamsName.TYPE))){
                                collapse = false;
                            }
                            if(menus.length() <= 0) return;
                            if(menus.length() == 1 || !collapse){    //只有一个菜单时展开,或者菜单属性是显示在toolbar上。
                                for (int i = 0; i < menus.length(); i++) {
                                    JSONObject item = menus.getJSONObject(i);
                                    menu.add(0,0,i,StringUtils.getString(item,HybridConstants.ParamsName.TEXT));
                                    MenuItem mi = menu.getItem(i);
                                    if(item.has(HybridConstants.ParamsName.ICON)){
                                        int resId = IconUtil.getIconId(item.getString(HybridConstants.ParamsName.ICON));
                                        if(resId != IconUtil.INVALID_ID){
                                            mi.setIcon(resId);
                                        }
                                    }
                                    mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                                    final JSONObject data = new JSONObject();
                                    try {
                                        data.put("button", "right");
                                        data.put("buttonIndex", i);
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
                                    mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            callback.callback(new NativeResponse(data));
                                            return true;
                                        }
                                    });
                                }
                                return;
                            }

                            /**多个菜单,切收起,改成SubMenu而不直接使用Menu，是为了方便以后定制icon和菜单标题*/
                            String groupTitle = "";
                            if(right.has(HybridConstants.ParamsName.GROUP_TITLE)){
                                groupTitle = right.getString(HybridConstants.ParamsName.GROUP_TITLE);
                            }
                            int groupIcon = IconUtil.INVALID_ID;
                            if(right.has(HybridConstants.ParamsName.GROUP_ICON)){
                                groupIcon = IconUtil.getIconId(right.getString(HybridConstants.ParamsName.GROUP_ICON));
                            }
                            SubMenu subMenu = menu.addSubMenu(groupTitle);
                            if(groupIcon != IconUtil.INVALID_ID){
                                subMenu.setHeaderTitle("");
                                subMenu.setIcon(groupIcon);
                            }

                            menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                            for (int i = 0; i < menus.length(); i++) {
                                JSONObject item = menus.getJSONObject(i);
                                subMenu.add(item.getString(HybridConstants.ParamsName.TEXT));
                                MenuItem mi = subMenu.getItem(i);
                                if(item.has(HybridConstants.ParamsName.ICON)){
                                    int resId = IconUtil.getIconId(item.getString(HybridConstants.ParamsName.ICON));
                                    if(resId != IconUtil.INVALID_ID){
                                        mi.setIcon(resId);
                                    }
                                }
                                final JSONObject data = new JSONObject();
                                try {
                                    data.put("button", "right");
                                    data.put("buttonIndex", i);
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                                mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        callback.callback(new NativeResponse(data));
                                        return true;
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            callback.callback(new NativeResponse(NativeResponse.STATUS_FAILED,e.getMessage()));
                        }
                    }
                });
            }

        } catch (JSONException e) {
            callback.callback(new NativeResponse(NativeResponse.STATUS_FAILED,e.getMessage()));
        }
    }

    @Override
    public String[] handleName() {
        return new String[]{HybridConstants.HandlerName.ALERT_TOOLBAR_METHOD};
    }

    public void configToolbarColor(android.support.v7.widget.Toolbar toolbar, String bgColorStr, String txtColorStr){
        try{
            if (!TextUtils.isEmpty(bgColorStr)){
                int color = Color.parseColor(bgColorStr);
                toolbar.setBackgroundColor(color);
                if (StringUtils.isColorDark(color)){
                    toolbar.setNavigationIcon(R.drawable.ic_back_white);
                } else {
                    toolbar.setNavigationIcon(R.drawable.ic_back_black);
                }

                if ("#FFFFFF".equalsIgnoreCase(bgColorStr)) {
                    if (Build.VERSION.SDK_INT < 21) {
//                        toolbar.findViewById(R.id.toolbar_line).setVisibility(View.GONE);
                    } else {
                        View v = (View) toolbar.getParent();
                        v.setElevation(0);
                        v.setTranslationZ(0);
                    }
                }
            }

            if (!TextUtils.isEmpty(txtColorStr)){
                toolbar.setTitleTextColor(Color.parseColor(txtColorStr));
            }
        } catch (IllegalArgumentException ex){
            ex.printStackTrace();
        }
    }

    public abstract void onHiddenTitleBar(boolean hidden);

    public abstract void onBackPressed();

    //控制实体键返回按钮
    private void onDisableBack(boolean b){
        setDisableBack(b);
    }
}
