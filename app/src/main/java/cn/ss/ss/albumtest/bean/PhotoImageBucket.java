package cn.ss.ss.albumtest.bean;

import java.io.Serializable;
import java.util.List;

public class PhotoImageBucket implements Serializable {
	private int count = 0;//相册中照片数
	private String bucketName;//相册句
	private List<PhotoImageItem> imageList;//照片列表

	public int getCount(){
		return count;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	public String getBucketName(){
		return bucketName;
	}
	
	public void setBucketName(String bucketName){
		this.bucketName = bucketName;
	}
	
	public List<PhotoImageItem> getImageList(){
		return imageList;
	}
	
	public void setImageList(List<PhotoImageItem> imageList){
		this.imageList = imageList;
	}
}
