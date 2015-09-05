package com.hy.singlesubscribenews.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.net.Uri;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import com.hy.singlesubscribenews.objects.NewsBrief;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class RssParser {

	private static XPath xPath = XPathFactory.newInstance().newXPath();
	private static final String ITEM = "/rss/channel/item";
	private static final String TITLE = "title/text()";
	private static final String SOURCE = "source/text()";
	private static final String LINK = "link/text()";
	private static final String DESCRIPTION = "description/text()";
	//private static final String THUMBNAIL = "enclosure/@url";
	// 获取img标签正则
	private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	// 获取src路径的正则
	private static final String IMGSRC_REG = "http:\"?(.*?)(\"|>|\\s+)";
	
	public ArrayList<NewsBrief> getNewsList(String rssString) throws XPathExpressionException{
		ArrayList<NewsBrief> mlist = new ArrayList<NewsBrief>();
		if(rssString == "" || null == rssString){return mlist;}
		
		InputSource is = new InputSource(new StringReader(rssString));
		
		NodeList nodes = getNotes(is);
		String thumbnail = null;
		for(int n = 0; n<nodes.getLength();n++){
			Node node = nodes.item(n);
			thumbnail = getThumbnail(node);
			if(thumbnail == null || thumbnail.equals("")){
				continue;
			}
			NewsBrief newsBrief = new NewsBrief();
			newsBrief.setTitle(getTitle(node));
			newsBrief.setUrl(getLink(node));
			newsBrief.setThumbnail(thumbnail);
			newsBrief.setSource(getSource(node));
			mlist.add(newsBrief);
		}
		
		return mlist;
	}
	
	
	private NodeList getNotes(InputSource is) throws XPathExpressionException{
		return (NodeList)xPath.evaluate(ITEM, is, XPathConstants.NODESET);
	}
	
	private String getTitle(Object item) throws XPathExpressionException{
		return xPath.evaluate(TITLE, item);
	}
	
	private String getSource(Object item) throws XPathExpressionException{
		return xPath.evaluate(SOURCE, item);
	}
	
	private String getLink(Object item) throws XPathExpressionException{
		return xPath.evaluate(LINK, item);
	}
	
	private String getDescription(Object item) throws XPathExpressionException{
		return xPath.evaluate(DESCRIPTION, item);
	}
	
	private String getThumbnail(Object item) throws XPathExpressionException{
		String thumbnail = null;
		thumbnail = xPath.evaluate("enclosure/@url", item);
		if(thumbnail == null || thumbnail.equals("")){
			String des = getDescription(item);
			thumbnail = getImageUrl(des);
//			if(thumbnail == null || thumbnail.equals("")){
//				thumbnail = getThumbnailFromHtml(getLink(item));
//			}
		}
		return thumbnail;
	}
	
	private String getThumbnailFromHtml(String newsLink){
		String thumbnail = null;
		try {
			URL uri = new URL(newsLink);
			String htmlString = new String(readStream(uri.openStream()));
			thumbnail = getImageUrl(htmlString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return thumbnail;
	}
	
	private String getImageUrl(String HTML) {
		String imgUrl = null;
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(HTML);
		if(matcher.find()) {
			imgUrl = matcher.group();
			matcher = Pattern.compile(IMGSRC_REG).matcher(imgUrl);
			if(matcher.find()){
				imgUrl = matcher.group().substring(0,matcher.group().length() - 1);
			}
		}
		return imgUrl;
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
}
