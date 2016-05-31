package cn.ss.ss.albumtest.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.BitmapUtils;

import cn.ss.ss.albumtest.R;
import cn.ss.ss.albumtest.bean.PhotoImageBucket;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 相册的listView adapter
 * @author yushen
 * @version 1.0.0 2014-3-16
 */
public class AlbumBucketListAdapter extends BaseAdapter {

	private final String TAG = getClass().getSimpleName();
	private LayoutInflater mInflater;
	private List<PhotoImageBucket> photoBucketList = new ArrayList<PhotoImageBucket>();
	private BitmapUtils mBitmapUtils;

	public AlbumBucketListAdapter(Context context, List<PhotoImageBucket> photoBucketList){
		mInflater = LayoutInflater.from(context);
		this.photoBucketList = photoBucketList;

		mBitmapUtils = new BitmapUtils(context);
		mBitmapUtils.configDefaultLoadingImage(R.mipmap.album_default_loading_pic);//默认背景图片
		mBitmapUtils.configDefaultLoadFailedImage(R.mipmap.album_default_loading_pic);//加载失败图片
		mBitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);//设置图片压缩类型


	}

	@Override
	public int getCount() {
		return photoBucketList.size();
	}

	@Override
	public Object getItem(int position) {
		return photoBucketList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.album_bucket_list_item, parent, false);
			holder.bucketImage = (ImageView)convertView.findViewById(R.id.album_bucket_image);
			holder.bucketTitle = (TextView)convertView.findViewById(R.id.album_bucket_title);
			holder.bucketNum = (TextView)convertView.findViewById(R.id.album_bucket_num);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		mBitmapUtils.display(holder.bucketImage, photoBucketList.get(position).getImageList().get(0).getImagePath());
		holder.bucketTitle.setText(photoBucketList.get(position).getBucketName());
		holder.bucketNum.setText(photoBucketList.get(position).getCount() + "张");//一定要转化为String,否则参数会被认为int类型的id而报错

		return convertView;
	}

	private class ViewHolder{
		public ImageView bucketImage;
		public TextView bucketTitle;
		public TextView bucketNum;
	}

}
