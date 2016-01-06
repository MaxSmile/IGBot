package com.vasilkoff.igbot;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    WebView webview = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWebView();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String js="(function(){$( 'a._ebwb5[data-reactid=\".0.1.0.1.0.$1156080445428026059.2.2.0\"]' )[0].click();})();";
                callJavaScript(js);
                Snackbar.make(view, "Like status changed?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initWebView() {
        webview = (WebView)findViewById(R.id.webView);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.setInitialScale(0);//default,no zoom
        WebSettings ws = webview.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        ws.setSupportZoom(false);
        ws.setBuiltInZoomControls(false);
        ws.setLoadWithOverviewMode(false);
        ws.setUseWideViewPort(false);
        ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        ws.setJavaScriptCanOpenWindowsAutomatically(false);


        webview.setWebViewClient(getWebViewClient());
        webview.addJavascriptInterface(new WebViewInterface(), "rx");
        webview.loadUrl(Config.IG_URL);
    }

    private void callJavaScript(String javascript){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In KitKat+ you should use the evaluateJavascript method
            webview.evaluateJavascript(javascript, new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String s) {
                    if (s!=null) {

                    }
                }
            });
        } else {
            webview.loadUrl("javascript:"+javascript);
        }

    }
    private WebViewClient getWebViewClient() {
        final MainActivity instance = this;

        return new WebViewClient() {
            public void onPageFinished(WebView view, String url) {

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();

            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                return false;
            }

            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("LogTag", message);
                result.confirm();
                return true;
            }
        };
    }

    private class WebViewInterface{

        @JavascriptInterface
        public void onError(String error){
            Toast.makeText(MainActivity.this,error,Toast.LENGTH_LONG);
        }



    }
}
