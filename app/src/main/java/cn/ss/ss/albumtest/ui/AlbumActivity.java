package cn.ss.ss.albumtest.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.TextView;

import cn.ss.ss.albumtest.R;
import cn.ss.ss.albumtest.adapter.AlbumBucketListAdapter;
import cn.ss.ss.albumtest.adapter.AlbumGridViewAdapter;
import cn.ss.ss.albumtest.bean.PhotoImageBucket;
import cn.ss.ss.albumtest.bean.PhotoImageItem;
import cn.ss.ss.albumtest.tool.AlbumHelper;
import cn.ss.ss.albumtest.tool.LogUtils;
import cn.ss.ss.albumtest.tool.StaticUtils;

/**
 * 显示所有图片的界面，可以更换相册，选择图片
 * @author yushen
 *
 */
public class AlbumActivity extends Activity implements OnClickListener {

	private static final String TAG = "AlbumActivity";

	private static final String OKBTNCLICK = "OKBTNCLICK";
	private static final int PHOTO_PREVIEW = 0;
	private static final int TAKE_PICTURE = 1;

	private ImageView back;
	private TextView bar_title;
	private ImageView search_button;
	private GridView albumGridview;
	private AlbumGridViewAdapter mAlbumAdapter;

	private int maxPhoto = 9;

	private AlbumHelper mAlbumHelper;
	private Button mOKBtn;
	private Button mAlbumListBtn;
	private boolean isAlbumBucketShow = false;
	private LinearLayout albumLl;
	private ListView albumList;
	private AlbumBucketListAdapter mBucketListAdapter;
	private List<PhotoImageBucket> mPhotoBucketList = new ArrayList<>();
	private List<PhotoImageItem> imageList = new ArrayList<>();

	private Uri photoUri;
	private String filePath;//拍摄照片的存放路径
	private String fileName;//拍摄照片的文件名


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		initView();
		StaticUtils.allSelectBucketphotos.addAll(imageList);
		initData();
	}

	public void initView() {
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText("图片");
		search_button = (ImageView) findViewById(R.id.take_photo);
		search_button.setImageResource(R.mipmap.photograph);
		search_button.setVisibility(View.VISIBLE);
		search_button.setPadding(10, 5, 10, 5);
		search_button.setOnClickListener(this);
		albumGridview = (GridView) findViewById(R.id.album_gridview);
		mOKBtn = (Button) findViewById(R.id.ok_button);
		mOKBtn.setBackgroundResource(R.mipmap.album_button_focused);
		mOKBtn.setOnClickListener(this);
		albumLl = (LinearLayout) findViewById(R.id.album_bucket_layout);
		mAlbumListBtn = (Button) findViewById(R.id.album_list_btn);
		albumList = (ListView) findViewById(R.id.album_bucket_list);
		mAlbumListBtn.setOnClickListener(this);
	}

	public void initData() {
		Intent intent = getIntent();
		maxPhoto = intent.getIntExtra("MAX_PHOTO", 9);
		mOKBtn.setText("完成" + "(" + StaticUtils.tempSelectphotos.size() + "/" + maxPhoto);
		if (StaticUtils.tempSelectphotos.size() == 0) {
			mOKBtn.setClickable(false);
		} else {
			mOKBtn.setClickable(true);
		}
		//初始化Album GridView
		mAlbumAdapter = new AlbumGridViewAdapter(this, StaticUtils.allSelectBucketphotos, StaticUtils.tempSelectphotos);
		albumGridview.setAdapter(mAlbumAdapter);

		//设置图片预览的监听
		mAlbumAdapter.setOnPhotoClickListener(new AlbumGridViewAdapter.OnPhotoClickListener() {
			@Override
			public void onItemClick(int position) {
				Intent intent = new Intent();
				intent.putExtra("position", position);
				intent.putExtra("MAX_PHOTO", maxPhoto);
				intent.setClass(AlbumActivity.this, GalleryActivity.class);
				startActivityForResult(intent, PHOTO_PREVIEW);

			}

		});
		//设置选择图片的监听
		mAlbumAdapter.setOnCheckBoxClickListener(new AlbumGridViewAdapter.OnCheckBoxClickListener() {
			@Override
			public void onItemClick(View maskView, int position,
									CheckBox checkBox) {
				if (StaticUtils.tempSelectphotos.size() >= maxPhoto && !checkBox.isChecked()) {//当选择的数据等于还能选择的图片数时并且所点图片未被选择时不做操作
					Toast.makeText(AlbumActivity.this, "选择图片已达上限", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!checkBox.isChecked()) {
					if (!StaticUtils.tempSelectphotos.contains(StaticUtils.allSelectBucketphotos.get(position)))
						StaticUtils.tempSelectphotos.add(StaticUtils.allSelectBucketphotos.get(position));
					checkBox.setChecked(true);
					maskView.setBackgroundResource(R.color.image_select_mask);
				} else {
					if (StaticUtils.tempSelectphotos.contains(StaticUtils.allSelectBucketphotos.get(position)))
						StaticUtils.tempSelectphotos.remove(StaticUtils.allSelectBucketphotos.get(position));
					checkBox.setChecked(false);
					maskView.setBackgroundResource(R.color.image_default_mask);
				}
				mOKBtn.setText("完成" + "(" + StaticUtils.tempSelectphotos.size() + "/" + maxPhoto);
				if (StaticUtils.tempSelectphotos.size() == 0) {//当没有选择图片时，禁止点击
					mOKBtn.setClickable(false);
					mOKBtn.setBackgroundResource(R.mipmap.album_button_focused);
				} else {
					mOKBtn.setClickable(true);
					mOKBtn.setBackgroundResource(R.drawable.album_ok_btn_state);
				}
			}
		});

		//初始化Album bucket listView
		mBucketListAdapter = new AlbumBucketListAdapter(this, mPhotoBucketList);
		albumList.setAdapter(mBucketListAdapter);

		albumList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				StaticUtils.allSelectBucketphotos.clear();
				StaticUtils.allSelectBucketphotos.addAll(mPhotoBucketList.get(position).getImageList());
				mAlbumAdapter.notifyDataSetChanged();
				mAlbumListBtn.setText(mPhotoBucketList.get(position).getBucketName());
				albumLl.setVisibility(View.GONE);
				isAlbumBucketShow = !isAlbumBucketShow;
			}
		});

		//获取Album bucket 数据
		mAlbumHelper = new AlbumHelper();
		mAlbumHelper.init(this);
		//回调接口
		mAlbumHelper.setGetAlbumList(new AlbumHelper.GetAlbumList() {
			@Override
			public void getAlbumList(List<PhotoImageBucket> list) {
				StaticUtils.allSelectBucketphotos.clear();
				list.get(0).getImageList();
				StaticUtils.allSelectBucketphotos.addAll(list.get(0).getImageList());
				mAlbumAdapter.notifyDataSetChanged();
				mPhotoBucketList.clear();
				mPhotoBucketList.addAll(list);
				mBucketListAdapter.notifyDataSetChanged();

			}
		});
		mAlbumHelper.execute(false);//异步线程执行

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case PHOTO_PREVIEW:
					if (data.getBooleanExtra(OKBTNCLICK, false)) {
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						finish();
					}
					if (StaticUtils.tempSelectphotos.size() == 0) {
						mOKBtn.setClickable(false);
					} else {
						mOKBtn.setClickable(true);
					}

					mOKBtn.setText("完成" + "(" + StaticUtils.tempSelectphotos.size() + "/" + maxPhoto);
					if (StaticUtils.tempSelectphotos.size() == 0) {//当没有选择图片时，禁止点击
						mOKBtn.setClickable(false);
						mOKBtn.setBackgroundResource(R.mipmap.album_button_focused);
					} else {
						mOKBtn.setClickable(true);
						mOKBtn.setBackgroundResource(R.drawable.album_ok_btn_state);
					}
					mAlbumAdapter.notifyDataSetChanged();
					break;
				case TAKE_PICTURE:
					PhotoImageItem takePhoto = new PhotoImageItem();
					takePhoto.setImagePath(filePath + fileName);
					takePhoto.setImageName(fileName);
					StaticUtils.tempSelectphotos.clear();
					StaticUtils.tempSelectphotos.add(takePhoto);
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();

					//必须执行scanFile 扫描新添加的文件，保证文件立即对应用有效，没有则需要重启手机或者在文件管理器后图片才能在应用中显示（测试微信也同样扫描不到）
					MediaScannerConnection.scanFile(this,
							new String[]{filePath + fileName}, null,
							new MediaScannerConnection.OnScanCompletedListener() {
								public void onScanCompleted(String path, Uri uri) {

								}
							});
			}
		}
	}

	public void photo() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			//照片的路径
			filePath = Environment.getExternalStorageDirectory()
					+ "/albumtest/" + "testPIC/";
			//照片名
			fileName = "albumtestpic." + String.valueOf(System.currentTimeMillis()) + ".jpg";
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			photoUri = Uri.fromFile(new File(filePath, fileName));
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, TAKE_PICTURE);
		} else {
			Toast.makeText(AlbumActivity.this, "内存卡不可用，不能拍照", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.take_photo:
				photo();
				break;
			case R.id.ok_button:
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
				break;
			case R.id.album_list_btn:
				if (isAlbumBucketShow) {
					albumLl.setVisibility(View.GONE);
				} else {
					albumLl.setVisibility(View.VISIBLE);
				}
				isAlbumBucketShow = !isAlbumBucketShow;
				break;
			default:
				break;
		}
	}

	@Override
	public void finish() {
		if (isAlbumBucketShow) {
			albumLl.setVisibility(View.GONE);
			isAlbumBucketShow = !isAlbumBucketShow;
			return;
		}
		super.finish();
	}

	@Override
	protected void onDestroy() {
		StaticUtils.allSelectBucketphotos.clear();
		StaticUtils.tempSelectphotos.clear();
		super.onDestroy();
	}
}