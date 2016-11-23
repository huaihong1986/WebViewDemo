package threedi.me.webviewdemo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private WebView webView;
    private LinearLayout ll_root;
    private EditText et_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_root = (LinearLayout) findViewById(R.id.ll_root);
        et_user = (EditText) findViewById(R.id.et_user);
        initWebView();
//        if(null!=savedInstanceState){
//            webView.restoreState(savedInstanceState);
//            Log.i(TAG, "restore state");
//        }else{
//            webView.loadUrl("http://3g.cn");
//        }
    }

    //初始化WebView

    private void initWebView() {
        //动态创建一个WebView对象并添加到LinearLayout中
        webView = new WebView(getApplication());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        ll_root.addView(webView);
        //不跳转到其他浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //加载本地html文件
        webView.loadUrl("file:///android_asset/JavaAndJavaScriptCall.html");
        webView.addJavascriptInterface(new JSInterface(),"Android");
    }

    //按钮的点击事件
    public void click(View view){
        //java调用JS方法
        //webView.loadUrl("javascript:javaCallJs(\"name\")");
        webView.loadUrl("javascript:javaCallJs(\"Hello\"+" + "'" + et_user.getText().toString()+"'"+" + \"Hello\"+" + "'" + et_user.getText().toString()+"'"+")");
    }

    //在页面销毁的时候将webView移除
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ll_root.removeView(webView);
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView = null;
    }

    private class JSInterface {
        //JS需要调用的方法
        @JavascriptInterface
        public void showToast(String arg){
            Toast.makeText(MainActivity.this,arg,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
        Log.e(TAG, "save state...");
    }
}
