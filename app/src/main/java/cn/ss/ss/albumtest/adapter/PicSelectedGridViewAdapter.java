package cn.ss.ss.albumtest.adapter;


import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import cn.ss.ss.albumtest.R;
import cn.ss.ss.albumtest.bean.PhotoImageItem;
import cn.ss.ss.albumtest.tool.LogUtils;

/**
 * 图片选择框的gridview adpter
 * @author yushen
 * @version 1.0.0 2014-3-16
 */
public class PicSelectedGridViewAdapter extends BaseAdapter {

	private static final String TAG = "PicSelectedAdapter";

	private LayoutInflater inflater;
	private int selectedPosition = -1;
	private List<PhotoImageItem> tempSelectPhotos;
	private BitmapUtils mBitmapUtils;


	public PicSelectedGridViewAdapter(Context context, List<PhotoImageItem> tempSelectPhotos) {
		inflater = LayoutInflater.from(context);
		this.tempSelectPhotos = tempSelectPhotos;
		if(this.tempSelectPhotos == null){
			this.tempSelectPhotos = new ArrayList<PhotoImageItem>();
		}

		mBitmapUtils = new BitmapUtils(context);
		mBitmapUtils.configDefaultLoadingImage(R.mipmap.album_default_loading_pic);//默认背景图片
		mBitmapUtils.configDefaultLoadFailedImage(R.mipmap.album_default_loading_pic);//加载失败图片
		mBitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型
	}

	public int getCount() {
		if(tempSelectPhotos.size() == 9){
			return 9;
		}
		return (tempSelectPhotos.size() + 1);
	}

	public Object getItem(int position) {
		return tempSelectPhotos.get(position);
	}

	public long getItemId(int position) {
		return position;
	}


	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.pic_selected_item,
					parent, false);
			holder = new ViewHolder();
			holder.picSelect = (ImageView) convertView
					.findViewById(R.id.item_selected_image);
			holder.picDelete = (ImageView) convertView
					.findViewById(R.id.item_delete_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == tempSelectPhotos.size()) {
			holder.picDelete.setVisibility(View.GONE);
			holder.picSelect.setImageResource(R.mipmap.icon_addpic_unfocused);
			if (position == 9) {
				holder.picSelect.setVisibility(View.GONE);
				LogUtils.loge(TAG, "2");
			}
		} else {
			holder.picDelete.setVisibility(View.VISIBLE);
			mBitmapUtils.display(holder.picSelect, tempSelectPhotos.get(position).getImagePath());
			holder.picDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					tempSelectPhotos.remove(position);
					notifyDataSetChanged();
				}
			});
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView picSelect;
		public ImageView picDelete;
	}

}
