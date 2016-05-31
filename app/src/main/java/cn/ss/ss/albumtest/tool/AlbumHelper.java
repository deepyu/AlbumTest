package cn.ss.ss.albumtest.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import android.provider.MediaStore.Images.Media;

import cn.ss.ss.albumtest.bean.PhotoImageBucket;
import cn.ss.ss.albumtest.bean.PhotoImageItem;

/**
 * 获取手机相册及图片
 * @author yushen
 * @version 1.0.0 2014-3-16
 */
public class AlbumHelper extends AsyncTask<Object, Object, Object>{

    final String TAG = getClass().getSimpleName();
    //过滤掉非常小的图片的参数(微信的默认图片列表貌似就是过滤掉了较小的图片)
    public static int mMinImageSize = 5000;//5000b
    Context context;
    ContentResolver cr;
    // 专辑列表
    LinkedHashMap<String, PhotoImageBucket> bucketList = new LinkedHashMap<>();//保证相册的顺序与输入相同
    private GetAlbumList getAlbumList;

    private String allPhotosBucketId = "-11111111111";//自定义全部图片的bucketId（避免与手机相册id相同）


    /**
     * 初始化
     * @param context
     */
    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }


    /**
     * 是否创建了图片集
     */
    boolean hasBuildImagesBucketList = false;
    /**
     * 得到图片集
     */
    private void buildImagesBucketList() {
        // 构造相册索引
        String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
                Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
                Media.SIZE, Media.BUCKET_DISPLAY_NAME };

        String where = Media.SIZE + " > " + mMinImageSize;//过滤掉非常小的图片()
        String sortOrder = Media.DATE_ADDED + " DESC";

        // 得到一个游标
        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
                sortOrder);
        if (cur == null) {
            LogUtils.logd(TAG, "call: " + "Empty images");
        }else
        if (cur.moveToFirst()) {
            //设置存放所有图片的相册，第一个入存放相册的map
            PhotoImageBucket allBucket = new PhotoImageBucket();
            allBucket.setImageList(new ArrayList<PhotoImageItem>());
            allBucket.setBucketName("所有图片");
            bucketList.put(allPhotosBucketId, allBucket);

            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
            int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);

            do {
                //过滤掉名字为空的图片，如.jpg,.png等
                if (cur.getString(photoPathIndex).substring(
                        cur.getString(photoPathIndex).lastIndexOf("/")+1,
                        cur.getString(photoPathIndex).lastIndexOf("."))
                        .replaceAll(" ", "").length()<=0)
                {
                    LogUtils.logd(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex)="+cur.getString(photoPathIndex));
                }else {
                    String _id = cur.getString(photoIDIndex);
                    String path = cur.getString(photoPathIndex);
                    String bucketName = cur.getString(bucketDisplayNameIndex);
                    int photoSize = cur.getInt(photoSizeIndex);
                    //以相册句为key将相册名相册的图片放入同一个相册（微信），如果想不同相册分开显示可以使用bucketId为key(qq)
                    //String bucketId = cur.getString(bucketIdIndex);
                    //PhotoImageBucket bucket = bucketList.get(bucketId);
                    PhotoImageBucket bucket = bucketList.get(bucketName);
                    //这里完成图片归并到相应的相册里去
                    if (bucket == null) {
                        bucket = new PhotoImageBucket();
                        bucketList.put(bucketName, bucket);//使用bucketName进行合并，将名字相同的保存到同一相册
                        bucket.setImageList(new ArrayList<PhotoImageItem>());
                        bucket.setBucketName(bucketName);
                    }
                    bucket.setCount(bucket.getCount() + 1);
                    PhotoImageItem imageItem = new PhotoImageItem();
                    imageItem.setImageId(_id);
                    imageItem.setImagePath(path);
                    bucket.getImageList().add(imageItem);
                    //将每一张图片都存入所有图片的相册里
                    LogUtils.loge(TAG, path + "");
                    if(photoSize > mMinImageSize){//所有图片里过滤掉较小图片，避免显示过多无效图片(空图)---测试微信中进行了过滤，不知道过滤规则
                        allBucket.setCount(allBucket.getCount() + 1);
                        allBucket.getImageList().add(imageItem);
                    }
                }
            } while (cur.moveToNext());
        }
        cur.close();
        hasBuildImagesBucketList = true;
    }

    /**
     * 得到图片集
     * @param refresh
     * @return tmpList
     */
    private List<PhotoImageBucket> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        List<PhotoImageBucket> tmpList = new ArrayList<>();
        Iterator<Entry<String, PhotoImageBucket>> itr = bucketList.entrySet().iterator();
        //将Hash转化为List
        while (itr.hasNext()) {
            Map.Entry<String, PhotoImageBucket> entry = itr
                    .next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    public void setGetAlbumList(GetAlbumList getAlbumList) {
        this.getAlbumList = getAlbumList;
    }
    //回调接口
    public interface GetAlbumList{
        void getAlbumList(List<PhotoImageBucket> list);
    }

    @Override
    protected Object doInBackground(Object... params) {
        return getImagesBucketList((Boolean)(params[0]));
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        getAlbumList.getAlbumList((List<PhotoImageBucket>) result);
    }

}
