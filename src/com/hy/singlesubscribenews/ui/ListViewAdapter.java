package com.hy.singlesubscribenews.ui;

import java.util.ArrayList;

import com.hy.singlesubscribenews.R;
import com.hy.singlesubscribenews.objects.NewsBrief;
import com.hy.singlesubscribenews.tools.ImageLoader;

import android.content.ContentResolver;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

	private ArrayList<NewsBrief> mNewsList;
	private Context context;
	private LayoutInflater inflater;
	private ImageLoader imageLoader = null;
	
	public ListViewAdapter(Context context,AbsListView absListView,ArrayList<NewsBrief> list){
		this.context = context;
		mNewsList = list;
		inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(absListView);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mNewsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mNewsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsBrief news = mNewsList.get(position);
		if(convertView == null){
			convertView = inflater.inflate(R.layout.listitem_layout, null);
			Holder holder = new Holder();
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.source = (TextView)convertView.findViewById(R.id.source);
			holder.thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);
			convertView.setTag(holder);
		}
		Holder holder = (Holder)convertView.getTag();
		holder.title.setText(news.getTitle());
		//holder.source.setText(news.getSource());
		holder.thumbnail.setTag(news.getThumbnail());
		holder.thumbnail.setImageBitmap(imageLoader.loadImage(news.getThumbnail()));
		
		return convertView;
	}
	
	private class Holder{
		TextView title;
		TextView source;
		ImageView thumbnail;
	}

	//临时方法
	public ImageLoader getImageLoader(){
		return imageLoader;
	}
	
}
