package com.hy.singlesubscribenews.activity;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import com.hy.singlesubscribenews.R;
import com.hy.singlesubscribenews.objects.NewsBrief;
import com.hy.singlesubscribenews.service.GetNewsService;
import com.hy.singlesubscribenews.service.GetNewsService.CallBack;
import com.hy.singlesubscribenews.tools.ImageLoader;
import com.hy.singlesubscribenews.ui.ListViewAdapter;
import com.hy.singlesubscribenews.ui.ViewPagerAdapter;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.loopj.android.http.AsyncHttpClient;
import com.viewpagerindicator.LinePageIndicator;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements CallBack{

	private AsyncHttpClient mClient = new AsyncHttpClient(); 
	private ServiceConnection connection;
	private GetNewsService getNewsService;
	private AutoScrollViewPager asViewPager;
	private LinePageIndicator lineIndicator;
	private ListView listView;
	//LDrawer
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		asViewPager = (AutoScrollViewPager)findViewById(R.id.view_pager);
		lineIndicator = (LinePageIndicator)findViewById(R.id.indicator);
		listView = (ListView)findViewById(R.id.newsList);
		initLDrawer();
		bindNewsService();
	}
	
	private void bindNewsService(){
		Intent intent = new Intent(this,GetNewsService.class);
		connection = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				getNewsService = ((GetNewsService.ServiceBind)service).getService();
				getNewsService.setCallBack(MainActivity.this);
				getNewsService.getNewsList(0);
			}
		};
		
		bindService(intent, connection, BIND_AUTO_CREATE);
	}

	@Override
	public void onParserNewsFinished(ArrayList<NewsBrief> newsList) {
		if(newsList == null || newsList.size() == 0){
			Toast.makeText(this, "获取数据失败，请重试", Toast.LENGTH_SHORT).show();
			return;
		}
		ListViewAdapter adapter = new ListViewAdapter(this,listView,newsList);
		listView.setAdapter(adapter);
		
		getTitlePicList(adapter);
	}
	
	
	@SuppressLint({ "ResourceAsColor", "NewApi" })
	private void initLDrawer(){
        ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);


        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
            drawerArrow, R.string.drawer_open,
            R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        String[] values = new String[]{
            "登录",
            "我的收藏",
            "消息推送",
            "意见反馈",
            "缓存清理",
            "给个好评",
            "关于我们"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
			@Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                    	Toast.makeText(MainActivity.this, "登录", Toast.LENGTH_SHORT).show();
//                        mDrawerToggle.setAnimateEnabled(false);
//                        drawerArrow.setProgress(1f);
                        break;
                    case 1:
                    	Toast.makeText(MainActivity.this, "我的收藏", Toast.LENGTH_SHORT).show();
//                        mDrawerToggle.setAnimateEnabled(false);
//                        drawerArrow.setProgress(0f);
                        break;
                    case 2:
                    	Toast.makeText(MainActivity.this, "消息推送", Toast.LENGTH_SHORT).show();
//                        mDrawerToggle.setAnimateEnabled(true);
//                        mDrawerToggle.syncState();
                        break;
                    case 3:
                    	Toast.makeText(MainActivity.this, "意见反馈", Toast.LENGTH_SHORT).show();
//                        if (drawerArrowColor) {
//                            drawerArrowColor = false;
//                            drawerArrow.setColor(R.color.ldrawer_color);
//                        } else {
//                            drawerArrowColor = true;
//                            drawerArrow.setColor(R.color.drawer_arrow_second_color);
//                        }
//                        mDrawerToggle.syncState();
                        break;
                    case 4:
                    	Toast.makeText(MainActivity.this, "缓存清理", Toast.LENGTH_SHORT).show();
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IkiMuhendis/LDrawer"));
//                        startActivity(browserIntent);
                        break;
                    case 5:
                    	Toast.makeText(MainActivity.this, "给个好评", Toast.LENGTH_SHORT).show();
//                        Intent share = new Intent(Intent.ACTION_SEND);
//                        share.setType("text/plain");
//                        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        share.putExtra(Intent.EXTRA_SUBJECT,
//                            getString(R.string.app_name));
//                        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.hello_world) + "\n" +
//                            "GitHub Page :  https://github.com/IkiMuhendis/LDrawer\n" +
//                            "Sample App : https://play.google.com/store/apps/details?id=" +
//                            getPackageName());
//                        startActivity(Intent.createChooser(share,
//                            getString(R.string.app_name)));
                        break;
                    case 6:
                    	Toast.makeText(MainActivity.this, "关于我们", Toast.LENGTH_SHORT).show();
//                        String appUrl = "https://play.google.com/store/apps/details?id=" + getPackageName();
//                        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
//                        startActivity(rateIntent);
                        break;
                }

            }
        });
	}
	
	
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	
	
	
	//临时方法
	private void getTitlePicList(ListViewAdapter adapter){
		ArrayList<View> pageList = new ArrayList<View>();
		ImageView page1 = new ImageView(this);
		page1.setBackgroundColor(0x7f000000);
		page1.setScaleType(ScaleType.FIT_XY);
		page1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		page1.setTag(((NewsBrief)adapter.getItem(0)).getThumbnail());
		pageList.add(page1);
		ImageView page2 = new ImageView(this);
		page2.setScaleType(ScaleType.FIT_XY);
		page2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		page2.setTag(((NewsBrief)adapter.getItem(1)).getThumbnail());
		pageList.add(page2);
		ImageView page3 = new ImageView(this);
		page3.setScaleType(ScaleType.FIT_XY);
		page3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		page3.setTag(((NewsBrief)adapter.getItem(2)).getThumbnail());
		pageList.add(page3);
		
		ImageLoader imageLoader = adapter.getImageLoader();
		imageLoader.setImageViewList(pageList);
		
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(pageList);
		asViewPager.setAdapter(viewPagerAdapter);
		lineIndicator.setViewPager(asViewPager);
		asViewPager.setInterval(5000);
		asViewPager.startAutoScroll();
	}
}