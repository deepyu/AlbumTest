package cn.ss.ss.albumtest.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ss.ss.albumtest.R;
import cn.ss.ss.albumtest.adapter.PicSelectedGridViewAdapter;
import cn.ss.ss.albumtest.bean.PhotoImageItem;
import cn.ss.ss.albumtest.tool.StaticUtils;

public class MainActivity extends Activity implements OnClickListener{

	private final String TAG = getClass().getSimpleName();

	private static final int PHOTO_PIC = 0;
	public static final int MAX_PHOTO = 9;
	private ImageView back;
	private TextView bar_title;

	private GridView mPicGridView;
	private PicSelectedGridViewAdapter mPicSelectedAdapter;
	private static List<PhotoImageItem> tempSelectphotos = new ArrayList<PhotoImageItem>();
	private PhotoImageItem mPhotoImageItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}


	public void initView(){
		back = (ImageView)findViewById(R.id.back);
		back.setOnClickListener(this);
		bar_title = (TextView) findViewById(R.id.bar_title);
		mPicGridView = (GridView)findViewById(R.id.pic_gridview);
		mPicSelectedAdapter = new PicSelectedGridViewAdapter(this, tempSelectphotos);
		mPicGridView.setAdapter(mPicSelectedAdapter);
		mPicGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if(position == tempSelectphotos.size()){
					int max = MAX_PHOTO - tempSelectphotos.size();
					//LogUtils.loge(TAG, max + "");
					Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
					intent.putExtra("MAX_PHOTO", max);
					startActivityForResult(intent, PHOTO_PIC);
				}else{

				}
			}

		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String imgPath = null;
		if(resultCode == RESULT_OK){
			switch (requestCode) {
				case PHOTO_PIC:
					tempSelectphotos.addAll(StaticUtils.tempSelectphotos);
					StaticUtils.tempSelectphotos.clear();//清除已选
					mPicSelectedAdapter = new PicSelectedGridViewAdapter(this, tempSelectphotos);
					mPicGridView.setAdapter(mPicSelectedAdapter);
					mPicSelectedAdapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.back:
				finish();
				break;
			default:
				break;
		}
	}
}
