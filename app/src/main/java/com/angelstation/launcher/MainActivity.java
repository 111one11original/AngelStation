package com.angelstation.launcher;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        webView = new WebView(this);
        setContentView(webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        
        webView.addJavascriptInterface(new WebAppInterface(), "AndroidBridge");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/index.html");
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void launchApp(String appName) {
            runOnUiThread(() -> {
                try {
                    Intent intent = new Intent();
                    if (appName.equals("games")) {
                        intent = getPackageManager().getLaunchIntentForPackage("com.android.vending");
                    } else if (appName.equals("browser")) {
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
                    } else if (appName.equals("settings")) {
                        intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    }
                    
                    if (intent != null) startActivity(intent);
                    else Toast.makeText(MainActivity.this, "Приложение не найдено", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}