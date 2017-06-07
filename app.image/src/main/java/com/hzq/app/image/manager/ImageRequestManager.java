package com.hzq.app.image.manager;

/**
 * @author: hezhiqiang
 * @date: 17/1/19
 * @version:
 * @description:    此manager是为了和底层库解耦,不同的库可以随意切换,不会影响到业务层代码。
 */

public class ImageRequestManager {
    private static final String TYPE_GLIDE = "glide";
    private static final String TYPE_PICASSO = "picasso";
    private static final String TYPE_FRESCO = "fresco";

    private ImageRequestManager(){}

    public static ImagerListener getRequest(){
       return getRequest(TYPE_FRESCO);
    }

    public static ImagerListener getRequest(String type){
        switch (type){
            case TYPE_GLIDE:
                return new GlideRequest();
            case TYPE_PICASSO:
                return new PicassoRequest();
            case TYPE_FRESCO:
                return new FrescoRequest();
            default:
                return new FrescoRequest();
        }
    }
}
