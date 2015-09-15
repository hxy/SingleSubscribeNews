package com.hy.singlesubscribenews.activity;

import com.hy.singlesubscribenews.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class NewsDetialActivity extends Activity{

	private WebView webView;
	private TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle(getString(R.string.news_detial));
		this.setContentView(R.layout.activity_detial);
		
		webView = (WebView)findViewById(R.id.webview);
		textView = (TextView)findViewById(R.id.originalnews);
		
		
		String url = getIntent().getStringExtra("newsUrl");
		String description = getIntent().getStringExtra("description");
		this.setContentView(initWebView(url,description));
	}
	
	private WebView initWebView(String url,String description){
		WebView webView = new WebView(this);
		webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
		webView.loadData(description, "text/html;charset=UTF-8", null);
		return webView;
	}
}
