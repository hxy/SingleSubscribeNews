package com.hy.singlesubscribenews.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.AbsListView;
import android.widget.ImageView;

public class ImageLoader {

	private static LruCache<String, Bitmap> lruCache;
	private AbsListView absListView;
	
	public ImageLoader(){}
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
			LoadImageTask loadTask = new LoadImageTask(url,null);
			loadTask.execute();
		}
		return bitmap;
	}
	
	@SuppressLint("NewApi")
	public Bitmap loadImage(String url,ImageView imageView){
		Bitmap bitmap = lruCache.get(url);
		if(bitmap == null){
			LoadImageTask loadTask = new LoadImageTask(url,imageView);
			loadTask.execute();
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
	
	
	private class LoadImageTask extends AsyncTask<Void, Void, Bitmap>{
		String imageUrl = null;
		ImageView imageView = null;
		public LoadImageTask(String url,ImageView imageView){
		      this.imageUrl = url;
		      this.imageView = imageView;
		}
		@SuppressLint("NewApi")
		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap bitmap = null;
			try {
				URL url = new URL(imageUrl);
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
			ImageView imageView = null;
			if(this.imageView ==null){
				imageView = (ImageView)absListView.findViewWithTag(imageUrl);
			}else {
				imageView = this.imageView;
			}
			
			if(imageView != null){
				imageView.setImageBitmap(result);
			}
		}

	}

}
