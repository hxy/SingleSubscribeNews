package com.hy.singlesubscribenews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsDetialActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String url = getIntent().getStringExtra("newsUrl");
		initWebView(url);
		this.setContentView(initWebView(url));
	}
	
	private WebView initWebView(String url){
		WebView webView = new WebView(this);
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
			
		});
		
		return webView;
	}
}
