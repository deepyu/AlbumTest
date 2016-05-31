package cn.ss.ss.albumtest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ss.ss.albumtest.R;
import cn.ss.ss.albumtest.adapter.GalleryPagerAdapter;
import cn.ss.ss.albumtest.tool.StaticUtils;
import cn.ss.ss.albumtest.view.ViewPagerFixed;


/**
 * 图片预览界面
 * @author yushen
 *
 */
public class GalleryActivity extends Activity implements OnClickListener{


	private static final String OKBTNCLICK = "OKBTNCLICK";

	private ImageView back;
	private TextView bar_title;
	private ImageView search_button;
	private CheckBox mSelectBox;
	private Button mOKBtn;
	private ViewPagerFixed mGalleryPager;
	private GalleryPagerAdapter mGalleryAdapter;
	private int position = 0;//显示图片的位置

	private int maxPhoto = 9;
	private boolean isClickedOKBtn = false;//是否点击确定按钮，如果是则跳过AlbumActivity直接返回反馈界面

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		initView();
		initData();
	}

	public void initView(){
		back = (ImageView)findViewById(R.id.back);
		back.setOnClickListener(this);
		bar_title = (TextView) findViewById(R.id.bar_title);
		bar_title.setText("图片");
		mGalleryPager = (ViewPagerFixed)findViewById(R.id.gallery_viewpager);
		mOKBtn = (Button) findViewById(R.id.ok_button);
		mOKBtn.setOnClickListener(this);
		mSelectBox = (CheckBox)findViewById(R.id.gallery_checkbox);
	}

	public void initData(){
		Intent intent = getIntent();
		position = intent.getIntExtra("position", 0);
		maxPhoto = intent.getIntExtra("MAX_PHOTO", 9);

		if(StaticUtils.tempSelectphotos.contains(StaticUtils.allSelectBucketphotos.get(position))){
			mSelectBox.setChecked(true);
		}else{
			mSelectBox.setChecked(false);
		}
		mSelectBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(StaticUtils.tempSelectphotos.size() >= maxPhoto && isChecked){
					buttonView.setChecked(false);
					Toast.makeText(GalleryActivity.this, "选择图片已达上限", Toast.LENGTH_SHORT).show();
					return;
				}
				if(isChecked){
					if(!StaticUtils.tempSelectphotos.contains(StaticUtils.allSelectBucketphotos.get(mGalleryPager.getCurrentItem())))
						StaticUtils.tempSelectphotos.add(StaticUtils.allSelectBucketphotos.get(mGalleryPager.getCurrentItem()));
				}else{
					if(StaticUtils.tempSelectphotos.contains(StaticUtils.allSelectBucketphotos.get(mGalleryPager.getCurrentItem())))
						StaticUtils.tempSelectphotos.remove(StaticUtils.allSelectBucketphotos.get(mGalleryPager.getCurrentItem()));
				}
				mOKBtn.setText("完成" + "(" + StaticUtils.tempSelectphotos.size() + "/" + maxPhoto);
			}
		});
		mOKBtn.setText("完成" + "(" + StaticUtils.tempSelectphotos.size() + "/" + maxPhoto);
		mGalleryAdapter = new GalleryPagerAdapter(StaticUtils.allSelectBucketphotos, this);
		mGalleryPager.setAdapter(mGalleryAdapter);
		mGalleryPager.setCurrentItem(position);
		mGalleryPager.addOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageSelected(int position) {
				if(StaticUtils.tempSelectphotos.contains(StaticUtils.allSelectBucketphotos.get(position))){
					mSelectBox.setChecked(true);
				}else{
					mSelectBox.setChecked(false);
				}
			}
		});

	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.back:
				isClickedOKBtn = false;
				finish();
				break;
			case R.id.ok_button:
				isClickedOKBtn = true;
				if(StaticUtils.tempSelectphotos.size() == 0){
					mSelectBox.setChecked(true);
					mOKBtn.setText("完成" + "(" + StaticUtils.tempSelectphotos.size() + "/" + maxPhoto);
				}
				finish();
				break;
			default:
				break;
		}
	}

	@Override
	public void finish(){
		Intent intent=new Intent();
		intent.putExtra(OKBTNCLICK, isClickedOKBtn);
		setResult(RESULT_OK, intent);
		super.finish();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
}
