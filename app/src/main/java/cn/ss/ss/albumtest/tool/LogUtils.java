package cn.ss.ss.albumtest.tool;

import android.util.Log;

/**
 * 打印log类
 *@author yushen
 */
public class LogUtils {
	
	/**
	 * develop mode when isShow is true, publish mode when isShow is false
	 */
	private static final boolean isShow = true;
	
	
	public static void logi(String tag,String msg){
		if(isShow){
			Log.i(tag,msg);
		}
	}
	public static void logi(String msg){
		if(isShow){
			Log.i("LS",msg);
		}
	}
	
	public static void logd(String tag,String msg){
		if(isShow){
			Log.d(tag,msg);
		}
	}
	public static void logd(String msg){
		if(isShow){
			Log.d("LS",msg);
		}
	}
	
	public static void loge(String tag,String msg){
		if(isShow){
			Log.e(tag,msg);
		}
	}
	public static void loge(String msg){
		if(isShow){
			Log.e("LS",msg);
		}
	}
	
}
