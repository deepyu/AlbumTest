package cn.ss.ss.albumtest.bean;
import java.io.Serializable;

public class PhotoImageItem implements Serializable {
	private String imageId;//图片id
	private String imagePath;//图片路径
	private String imageName ;//图片句
	
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}
