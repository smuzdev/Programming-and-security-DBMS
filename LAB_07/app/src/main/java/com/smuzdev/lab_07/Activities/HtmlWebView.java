package com.smuzdev.lab_07.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.smuzdev.lab_07.R;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class HtmlWebView extends AppCompatActivity {

    WebView webView;

    //method will ask user for permissions to EXTERNAL storage
    @NeedsPermission(Manifest.permission.INTERNET) //annotation of PermissionsDispatcher library
    void openHTMLinWebView() {
        Intent intent = getIntent();
        String HTMLFilePath = intent.getStringExtra("filePath");
        webView.loadUrl("file:///" + HTMLFilePath);
    }


    //if user denied permission to external storage
    @OnPermissionDenied(Manifest.permission.INTERNET)
    void DeserializeNotesDenied() {
        Toast.makeText(this, "You should allow access to File storage for work with APP. After close app Notes will be DELETED;", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HtmlWebViewPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_web_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        webView = findViewById(R.id.activity_html_web_view);

        WebSettings websettings = webView.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setAllowContentAccess(true);
        websettings.setAppCacheEnabled(true);
        websettings.setDomStorageEnabled(true);
        websettings.setUseWideViewPort(true);

        HtmlWebViewPermissionsDispatcher.openHTMLinWebViewWithPermissionCheck(this);
    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
        handler.proceed();
    }
}