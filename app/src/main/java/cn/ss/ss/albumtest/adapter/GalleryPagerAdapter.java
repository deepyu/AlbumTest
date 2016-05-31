package cn.ss.ss.albumtest.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.ss.ss.albumtest.R;
import cn.ss.ss.albumtest.bean.PhotoImageItem;
import cn.ss.ss.albumtest.view.ViewPagerFixed;

public class GalleryPagerAdapter extends PagerAdapter {

	private static final String TAG = "GalleryPagerAdapter";
	private ArrayList<PhotoImageItem> dataList = new ArrayList<PhotoImageItem>();
	private List<ImageView> views = new ArrayList<ImageView>();;
	private BitmapUtils bitmapUtils;

	public GalleryPagerAdapter(ArrayList<PhotoImageItem> dataList, Context context){
		this.dataList = dataList;

		for(int i=0; i<dataList.size(); i++){
			ImageView iv = new ImageView(context);
			views.add(iv);;
		}
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadingImage(R.mipmap.album_default_loading_pic);//默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.mipmap.album_default_loading_pic);//加载失败图片
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型

	}


	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	//在instantiateItem中加载图片，避免一次性添加很多图片引起oom
	public Object instantiateItem(ViewGroup arg0, final int arg1) {

		bitmapUtils.display(views.get(arg1), dataList.get(arg1).getImagePath());
		try{
			((ViewPagerFixed) arg0).addView(views.get(arg1), 0);
		}catch (Exception e){

		}
		return views.get(arg1);
	}

	public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
		((ViewPagerFixed) arg0).removeView(views.get(arg1));
	}

}
