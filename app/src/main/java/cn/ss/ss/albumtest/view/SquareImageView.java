package cn.ss.ss.albumtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 长宽相同的imageView
 * @author yushen
 * @version 1.0.0 2014-3-16
 */
public class SquareImageView extends ImageView {


	public SquareImageView(Context context) {
		super(context);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

}
