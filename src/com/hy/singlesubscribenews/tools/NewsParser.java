package com.hy.singlesubscribenews.tools;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hy.singlesubscribenews.objects.NewsBrief;

public class NewsParser {

	public ArrayList<NewsBrief> parseJsonToNewslist(String jsonString) throws JSONException{
		ArrayList<NewsBrief> newsList = new ArrayList<NewsBrief>();
		JSONArray jsonArray = new JSONArray(jsonString);
		for(int n=0;n<jsonArray.length();n++){
			newsList.add(parseJsonToNews(jsonArray.getJSONObject(n)));
		}
		return newsList;
	}
	
	private NewsBrief parseJsonToNews(JSONObject jsonObject) throws JSONException{
		NewsBrief news = new NewsBrief();
		news.setTitle(jsonObject.getString("title"));
		news.setUrl(jsonObject.getString("url"));
		news.setSource(jsonObject.getString("source"));
		news.setThumbnail(jsonObject.getString("thumbnail"));
		news.setDescription(jsonObject.getString("description"));
		
		return news;
	}
}
