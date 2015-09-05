package com.hy.singlesubscribenews.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

public class ImageLoader {

	private LruCache<String, Bitmap> lruCache;
	private AbsListView absListView;
	
	@SuppressLint("NewApi")
	public ImageLoader(AbsListView absListView){
		this.absListView = absListView;
		int maxCatchSize = (int)Runtime.getRuntime().maxMemory()/4;
		lruCache = new LruCache<String, Bitmap>(maxCatchSize){
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}
	
	@SuppressLint("NewApi")
	public Bitmap loadImage(String url){
		Bitmap bitmap = lruCache.get(url);
		if(bitmap == null){
			LoadImageTask loadTask = new LoadImageTask();
			loadTask.execute(url);
		}
		return bitmap;
	}
	
    private byte[] readStream(InputStream in) throws IOException{
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        byte[] bytes = new byte[512];
        int len = 0;
        while(-1!=(len=in.read(bytes))){
            bytesOut.write(bytes, 0, len);
        }
        in.close();
        return bytesOut.toByteArray();
    }
    
	private Bitmap getSampleBitmap(byte[] bitmapBytes,int imageWidth){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
		int picWidth = options.outWidth;
		int picHeight = options.outHeight;
		int insamplesize = 1;
		if(picWidth>picHeight){
			insamplesize = picWidth > imageWidth ? picWidth/imageWidth : 1;
		}else {
			insamplesize = picHeight > imageWidth ? picHeight/imageWidth : 1;
		}
		
		options.inSampleSize = insamplesize;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

		return bitmap;
	}
	
	
	private class LoadImageTask extends AsyncTask<String, Void, Bitmap>{
		String imageUrl = null;
		@SuppressLint("NewApi")
		@Override
		protected Bitmap doInBackground(String... params) {
			imageUrl = params[0];
			Bitmap bitmap = null;
			try {
				URL url = new URL(params[0]);
				bitmap = getSampleBitmap(readStream(url.openStream()),80);
				if(bitmap != null){
					lruCache.put(imageUrl, bitmap);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			ImageView imageView = (ImageView)absListView.findViewWithTag(imageUrl);
			if(imageView != null){
				imageView.setImageBitmap(result);
			}
			setBitmapToImageView(imageViewsList,imageUrl,result);
		}
		
		
		
	}
	
	
	//临时方法
	private ArrayList<View> imageViewsList; 
	public void setImageViewList(ArrayList<View> list){
		imageViewsList = list;
	}
	
	private void setBitmapToImageView(ArrayList<View> imageViewsList,String url,Bitmap bitmap){
		for(View view : imageViewsList){
			if(((String)view.getTag()).equals(url)){
				((ImageView)view).setImageBitmap(bitmap);
			}
		}
	}
}
