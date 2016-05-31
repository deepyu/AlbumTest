package cn.ss.ss.albumtest.adapter;

import java.util.ArrayList;

import com.lidroid.xutils.BitmapUtils;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import cn.ss.ss.albumtest.R;
import cn.ss.ss.albumtest.bean.PhotoImageItem;
import cn.ss.ss.albumtest.view.SquareImageView;

/**
 * 显示手机图片的gridview adpter
 * @author yushen
 * @version 1.0.0 2014-3-16
 */
public class AlbumGridViewAdapter extends BaseAdapter{
	final String TAG = getClass().getSimpleName();
	private LayoutInflater inflater;
	private ArrayList<PhotoImageItem> dataList;
	private ArrayList<PhotoImageItem> selectedDataList;
	private BitmapUtils mBitmapUtils;

	public AlbumGridViewAdapter(Context context, ArrayList<PhotoImageItem> dataList,
								ArrayList<PhotoImageItem> selectedDataList) {
		inflater = LayoutInflater.from(context);
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;

		mBitmapUtils = new BitmapUtils(context);
		mBitmapUtils.configDefaultLoadingImage(R.mipmap.album_default_loading_pic);//默认背景图片
		mBitmapUtils.configDefaultLoadFailedImage(R.mipmap.album_default_loading_pic);//加载失败图片
		mBitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型

	}

	public int getCount() {
		return dataList.size();
	}

	public Object getItem(int position) {
		return dataList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}



	private static class ViewHolder {
		public SquareImageView imageView;
		public CheckBox checkBox;
		public View maskView;
		public LinearLayout ll;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.album_pic_item, parent, false);
			viewHolder.imageView = (SquareImageView)convertView.findViewById(R.id.item_selected_image);
			viewHolder.maskView = convertView.findViewById(R.id.image_mask);
			viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.album_checkbox);
			viewHolder.ll = (LinearLayout)convertView.findViewById(R.id.album_item_ll);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if(selectedDataList.contains(dataList.get(position))){
			viewHolder.checkBox.setChecked(true);
			viewHolder.maskView.setBackgroundResource(R.color.image_select_mask);
		}else{
			viewHolder.checkBox.setChecked(false);
			viewHolder.maskView.setBackgroundResource(R.color.image_default_mask);
		}

		viewHolder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dataList != null && mOnPhotoClickListener != null
						&& position < dataList.size()) {
					mOnPhotoClickListener.onItemClick(position);
				}
			}
		});



		viewHolder.ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dataList != null && mOnCheckBoxClickListener != null
						&& position < dataList.size()) {
					mOnCheckBoxClickListener.onItemClick(viewHolder.maskView, position, viewHolder.checkBox);
				}
			}
		});

		mBitmapUtils.display(viewHolder.imageView, dataList.get(position).getImagePath());

		return convertView;
	}


	private OnPhotoClickListener mOnPhotoClickListener;

	public void setOnPhotoClickListener(OnPhotoClickListener l){
		mOnPhotoClickListener = l;
	}

	//预览图片的回调监听
	public interface OnPhotoClickListener{
		void onItemClick(int position);
	}

	private OnCheckBoxClickListener mOnCheckBoxClickListener;

	public void setOnCheckBoxClickListener(OnCheckBoxClickListener l){
		mOnCheckBoxClickListener = l;
	}

	//选择图片的预览监听
	public interface OnCheckBoxClickListener{
		void onItemClick(View maskView, int position, CheckBox checkBox);
	}

}