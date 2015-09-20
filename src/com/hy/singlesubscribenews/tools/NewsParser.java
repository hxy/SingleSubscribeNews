package com.hy.singlesubscribenews.tools;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hy.singlesubscribenews.objects.NewsBrief;

public class NewsParser {

	public ArrayList<NewsBrief> parseNewslist(String jsonString) throws JSONException{
		ArrayList<NewsBrief> newsList = new ArrayList<NewsBrief>();
		JSONArray jsonArray = new JSONArray(jsonString);
		//n要从1开始，因为第一个位置保存了title news list 的下标
		for(int n=1;n<jsonArray.length();n++){
			newsList.add(parseNews(jsonArray.getJSONObject(n)));
		}
		return newsList;
	}
	
	private NewsBrief parseNews(JSONObject jsonObject) throws JSONException{
		NewsBrief news = new NewsBrief();
		news.setTitle(jsonObject.getString("title"));
		news.setUrl(jsonObject.getString("url"));
		news.setSource(jsonObject.getString("source"));
		news.setThumbnail(jsonObject.getString("thumbnail"));
		news.setDescription(jsonObject.getString("description"));
		
		return news;
	}
	
	public int[] parserTitleNewsIndex(String jsonString) throws JSONException{
		JSONArray jsonArray = new JSONArray(jsonString);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		int one = jsonObject.getInt("one");
		int tow = jsonObject.getInt("tow");
		int three = jsonObject.getInt("three");
		int[] index = {one,tow,three};
		return index;
	}
}
