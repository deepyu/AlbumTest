package cn.ss.ss.albumtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
/**
 * 自定义gridview，解决scrollView中嵌套gridview显示不正常的问题
 * @author yushen
 * @version 1.0.0 2014-3-16
 */
public class MyGridView extends GridView {

	public MyGridView(Context context, AttributeSet attrs) {   
        super(context, attrs);   
    }   
   
    public MyGridView(Context context) {   
        super(context);   
    }   
   
    public MyGridView(Context context, AttributeSet attrs, int defStyle) {   
        super(context, attrs, defStyle);   
    }   
   
    @Override   
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
   
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,   
                MeasureSpec.AT_MOST);   
        try{
        	super.onMeasure(widthMeasureSpec, expandSpec); 
        }catch(IllegalArgumentException ex){
        	ex.printStackTrace();
        } 
    }   
    
}
