package cn.ss.ss.albumtest.tool;


import java.util.ArrayList;

import cn.ss.ss.albumtest.bean.PhotoImageItem;

/**
 * 静态变量会占用内存，影响运行效率，以及内存不足销毁引起异常，慎用
 * @author yushen
 *
 */
public class StaticUtils {

	/**
	 * 存放临时选中的图片数据
	 */
	public static ArrayList<PhotoImageItem> tempSelectphotos = new ArrayList<>();
	/**
	 * 存放选择相集中的图片数据
	 */
	public static ArrayList<PhotoImageItem> allSelectBucketphotos = new ArrayList<>();
}
