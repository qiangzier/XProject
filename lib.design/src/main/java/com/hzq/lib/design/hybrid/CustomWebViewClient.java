package com.hzq.lib.design.hybrid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author: hezhiqiang
 * @date: 17/1/16
 * @version:
 * @description:
 *
 * 1、在利用shouldOverrideUrlLoading来拦截URL时，如果return true，
 * 则会屏蔽系统默认的显示URL结果的行为，不需要处理的URL也需要调用loadUrl()来加载进WebVIew，
 * 不然就会出现白屏；如果return false，则系统默认的加载URL行为是不会被屏蔽的，所以一般建议
 * 大家return false，我们只关心我们关心的拦截内容，对于不拦截的内容，让系统自己来处理即可。
 */

public class CustomWebViewClient  extends WebViewClient {

    Context context;
    ProgressDialog progressDialog;
    public CustomWebViewClient(Context context){
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }
    //拦截 url 跳转,在里边添加点击链接跳转或者操作
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //公自定义schema
        if (url.startsWith("xxx://")){
            Intent wbgIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(wbgIntent);
            return true;
        }

        //邮件，电话和地理位置调用相关的app打开
        if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
            return true;
        }

        if(TextUtils.isEmpty(url) && Uri.parse(url).getScheme() != null){
            String schema = Uri.parse(url).getScheme().trim();
            if(schema.equalsIgnoreCase("http") || schema.equalsIgnoreCase("https")){
                super.shouldOverrideUrlLoading(view,injectIsParams(url));
            }
        }
        return false;
    }

    //拦截 url 跳转,在里边添加点击链接跳转或者操作,原生WebView的方法
    /*@SuppressLint("NewApi")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, final WebResourceRequest request) {
        if(request != null && request.getUrl() != null) {
            String schema = request.getUrl().getScheme().trim();
            if(!TextUtils.isEmpty(schema) && (schema.equalsIgnoreCase("http") || schema.equalsIgnoreCase("https"))) {
                return super.shouldOverrideUrlLoading(view, new WebResourceRequest() {
                    @Override
                    public Uri getUrl() {
                        return Uri.parse(injectIsParams(request.getUrl().toString()));
                    }

                    @Override
                    public boolean isForMainFrame() {
                        return request.isForMainFrame();
                    }

                    @Override
                    public boolean hasGesture() {
                        return request.hasGesture();
                    }

                    //获取请求方式
                    @Override
                    public String getMethod() {
                        return request.getMethod();
                    }

                    //拦截header处理
                    @Override
                    public Map<String, String> getRequestHeaders() {
                        return request.getRequestHeaders();
                    }
                });
            }
        }
        return false;
    }*/

    //通知主程序页面当前开始加载,这里显示dialog
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        progressDialog.show();
    }

    //在结束加载网页时会回调,这里隐藏dialog
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        progressDialog.dismiss();
    }

    // 加载错误的时候会回调，在其中可做错误处理，比如再请求加载一次，或者提示404的错误页面
//    @Override
//    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//        super.onReceivedError(view, request, error);
//        //加载一个错误页面
//        view.loadUrl("http://192.168.6.104:8080/hybird/error.html");
//    }

    /**
     *  当接收到https错误时，会回调此函数，在其中可以做错误处理
        WebView view:当前的WebView实例
        SslErrorHandler handler：当前处理错误的Handler，它只有两个函数SslErrorHandler.proceed()和SslErrorHandler.cancel()，
        SslErrorHandler.proceed()表示忽略错误继续加载，SslErrorHandler.cancel()表示取消加载。在onReceivedSslError的默认实现
        中是使用的SslErrorHandler.cancel()来取消加载，所以一旦出来SSL错误，HTTPS网站就会被取消加载了，如果想忽略错误继续加载就只
        有重写onReceivedSslError，并在其中调用SslErrorHandler.proceed()
        SslError error：当前的的错误对象，SslError包含了当前SSL错误的基本所有信息。
     */
    @Override
    public void onReceivedSslError(WebView view, com.tencent.smtt.export.external.interfaces.SslErrorHandler handler, SslError error) {
        //必须注销掉这句
//        super.onReceivedSslError(view, handler, error);
        handler.proceed();
    }

    //在每一次请求资源时，都会通过这个函数来回调
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return super.shouldInterceptRequest(view, request);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        return super.shouldInterceptRequest(view, url);
    }

    /**对指定的url做统一的处理,比如 增加 xxx=1参数**/
    public String injectIsParams(String url){
        if(url != null && !url.contains("xxx=")){
            if(url.contains("?")){
                return url + "xxx=1";
            }else{
                return url + "?xxx=1";
            }
        }else{
            return url;
        }
    }
}
