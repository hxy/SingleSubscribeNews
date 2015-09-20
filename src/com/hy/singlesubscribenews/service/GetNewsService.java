package com.hy.singlesubscribenews.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.json.JSONObject;

import com.hy.singlesubscribenews.objects.NewsBrief;
import com.hy.singlesubscribenews.tools.NewsParser;
import com.hy.singlesubscribenews.tools.RssParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class GetNewsService extends Service {

	private Handler threadHandler;
	private Handler mainHandler;
	//private String url = "http://blog.sina.com.cn/rss/1286528122.xml";
	//private String url ="http://www.zhihu.com/rss";
	//private String url = "http://www.scipark.net/feed";
	//private String url = "http://xianguo.com/service/dailyshare";http://www.netnoease.com/rss2.php
	//private String url = "http://www.nbweekly.com/rss/smw/";
	//private String url = "http://9.douban.com/rss/life";
	private String url = "http://10.0.2.2:8080/SingleSubscribeNewsServer/newsList.jsp";
	private AsyncHttpClient mClient = new AsyncHttpClient();
	private ArrayList<NewsBrief> mList = null;
	private int[] titleNewsIndex;
	private CallBack callBack;

	private static final int PARSER_NEWS_LIST = 0;
	private static final int GET_NEWS_STRING = 2;
	private static final int PARSER_NEWS_FINISHED = 1;

	@Override
	public IBinder onBind(Intent intent) {

		return new ServiceBind();
	}

	public class ServiceBind extends Binder {
		public GetNewsService getService() {
			return GetNewsService.this;
		}
	}

	public interface CallBack{
		public void onParserNewsFinished(ArrayList<NewsBrief> list,int[] titleNewsIndex);
	}
	
	public void setCallBack(CallBack callBack){
		this.callBack = callBack;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		mainHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case PARSER_NEWS_FINISHED:
					callBack.onParserNewsFinished(mList,titleNewsIndex);
					break;

				default:
					break;
				}
			}

		};

		GetNewsThread getNewsThread = new GetNewsThread();
		getNewsThread.start();
	}

	public void getNewsList(int newsType) {
		Log.d("bbbb", "getNewsList");

		Message msg = Message.obtain();
		msg.what = GET_NEWS_STRING;
		msg.arg1 = newsType;
		threadHandler.sendMessage(msg);
	}

	private void parserNewsList(String newsJsonString) {
		Log.d("bbbb", "newsJsonString:"+newsJsonString);
		//RssParser parser = new RssParser();
		NewsParser parser = new NewsParser();
		try {
			mList = parser.parseNewslist(newsJsonString);
			titleNewsIndex = parser.parserTitleNewsIndex(newsJsonString);
//			for(NewsBrief news : mList){
//				String brief = "title:"+news.getTitle()+"\n"+"source:"+news.getSource()+"\n"+"thumbnail:"+news.getThumbnail();
//				Log.d("nnnn", "brief"+brief);
//			}
			mainHandler.sendEmptyMessage(PARSER_NEWS_FINISHED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void getNewsJsonString(int newsType){
		Log.d("bbbb", "getNewsJsonString--newsType:"+newsType);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("newsType", newsType);
			String parameterJsonString = jsonObject.toString();
			
			URL newsurl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection)newsurl.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
            connection.setUseCaches(false);    
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
			dos.writeBytes(parameterJsonString);
            dos.flush();
            dos.close();
			if(connection.getResponseCode() == 200){
				InputStream in = connection.getInputStream();
				byte[] bytes = readStream(in);
				String rssJsonString = new String(bytes);
				Message msg = Message.obtain();
				msg.what = PARSER_NEWS_LIST;
				msg.obj = rssJsonString;
				threadHandler.sendMessage(msg);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    private byte[] readStream(InputStream in) throws IOException{
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        byte[] bytes = new byte[512];
        int len = 0;
        while(-1!=(len=in.read(bytes))){
            bytesOut.write(bytes, 0, len);
        }
        return bytesOut.toByteArray();
    }
	
	private class GetNewsThread extends Thread {

		@Override
		public void run() {
			Looper.prepare();
			threadHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case PARSER_NEWS_LIST:
						parserNewsList((String) msg.obj);
						break;
					case GET_NEWS_STRING:
						getNewsJsonString(msg.arg1);
					default:
						break;
					}
				}
			};
			Looper.loop();
		}

	}
}
