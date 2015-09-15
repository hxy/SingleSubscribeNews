package com.hy.singlesubscribenews.activity;

import com.hy.singlesubscribenews.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class NewsDetialActivity extends Activity{

	private WebView webView;
	private TextView textView;
	private String newsUrl;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle(getString(R.string.news_detial));
		this.setContentView(R.layout.activity_detial);
		
		webView = (WebView)findViewById(R.id.webview);
		//textView = (TextView)findViewById(R.id.originalnews);
		
		newsUrl = getIntent().getStringExtra("newsUrl");
		String description = getIntent().getStringExtra("description");
        initWebView(description);
	}
	
	private void initWebView(String description){
		webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
		webView.loadData(description, "text/html;charset=UTF-8", null);
	}

	private void goToOriginalNews(){
		webView.loadUrl(newsUrl);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.detial, menu);  
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getOrder()) {
		case 100:
			goToOriginalNews();
			break;

		default:
			break;
		}
		
		return true;
	}
	
}
