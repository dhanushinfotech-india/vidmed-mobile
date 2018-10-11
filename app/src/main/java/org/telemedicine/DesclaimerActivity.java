package org.telemedicine;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by suneel on 3/21/17.
 */

public class DesclaimerActivity extends SuperActivity {
    Button btn_back;
    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.termsandconditionsdilogue);
        WebView we= (WebView) findViewById(R.id.termsview);
        btn_back= (Button) findViewById(R.id.btn_back);
        we.setWebViewClient(new WebViewClient());
        we.loadUrl("file:///android_asset/desclaimer.html");
        btn_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                super.onBackPressed();
                break;
        }

    }
}
