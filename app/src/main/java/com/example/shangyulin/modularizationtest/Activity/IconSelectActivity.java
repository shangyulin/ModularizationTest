package com.example.shangyulin.modularizationtest.Activity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.shangyulin.modularizationtest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Locale;

@Route(path = "/main/IconSelectActivity")
public class IconSelectActivity extends AppCompatActivity {

    private static final int ALBUM = 1000;
    private static final int CAMERA = 2000;
    private static final int CUPREQUEST = 3000;
    private ImageView image;

    private String picPath;
    private File mOutImage;
    private Uri mImageUri;
    private Uri uritempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);
        image = findViewById(getResources().getIdentifier("circle", "id", getPackageName()));
        findViewById(getResources().getIdentifier("icon_album", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, ALBUM);
            }
        });

        findViewById(getResources().getIdentifier("icon_photo", "id", getPackageName())).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得项目缓存路径
                String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                //根据时间随机生成图片名
                String photoName = new DateFormat().format("yyyyMMddhhmmss",
                        Calendar.getInstance(Locale.CHINA)) + ".jpg";
                picPath = sdPath + "/" + photoName;
                mOutImage = new File(picPath);
                //如果是7.0以上 那么就把uri包装
                if (Build.VERSION.SDK_INT >= 24) {
                    mImageUri = FileProvider.getUriForFile(IconSelectActivity.this, "com.example.demo.fileprovider", mOutImage);
                } else {
                    //否则就用老系统的默认模式
                    mImageUri = Uri.fromFile(mOutImage);
                }
                //启动相机
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, CAMERA);
            }
        });
    }

    private void setCropPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0剪裁图片 同理 需要把uri包装
            //通过FileProvider创建一个content类型的Uri
            Uri inputUri = FileProvider.getUriForFile(IconSelectActivity.this,
                    "com.example.demo.fileprovider", mOutImage);
            startPhotoZoom(inputUri);//设置输入类型
        } else {
            Uri inputUri = Uri.fromFile(mOutImage);
            startPhotoZoom(inputUri);
        }

    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //去除默认的人脸识别，否则和剪裁匡重叠
            intent.putExtra("noFaceDetection", false);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 宽高的比例
        //华为特殊处理 不然会显示圆
        if (android.os.Build.MODEL.contains("HUAWEI")) {
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        if (Build.MANUFACTURER.contains("Xiaomi")) {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "tt.jpg");
        } else {
            intent.putExtra("return-data", true);
        }
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CUPREQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            // 裁剪相机照片
            case CAMERA:
                setCropPhoto();
//                showPicture();
                break;

            //裁剪本地相册
            case ALBUM:
                Uri url = data.getData();
                if (DocumentsContract.isDocumentUri(this, url)) {// 如果是document类型的url
                    String docId = DocumentsContract.getDocumentId(url);
                    if ("com.android.providers.media.documents".equals(url.getAuthority())) {
                        String id = docId.split(":")[1];
                        String selection = MediaStore.Images.Media._ID + "=" + id;
                        picPath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                    } else if ("com.android.providers.downloads.documents".equals(url.getAuthority())) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                        picPath = getImagePath(contentUri, null);
                    }
                } else if ("content".equalsIgnoreCase(url.getScheme())) {// 如果是content类型的URL则是普通方式处理
                    picPath = getImagePath(url, null);
                } else if ("file".equalsIgnoreCase(url.getScheme())) {// 如果是file类型，直接获取路径
                    picPath = url.getPath();
                }
                displayImage();
                break;
            case CUPREQUEST:
                if (data == null) {
                    return;
                }
                Bundle extras = data.getExtras();
                String s;
                if (Build.MANUFACTURER.contains("Xiaomi")){
                    if (uritempFile != null){
                        s = uritempFile.getPath();
                    }else{
                        s = "";
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(s);
                    image.setImageBitmap(bitmap);
                }else{
                    if (extras != null){
                        Bitmap photo = extras.getParcelable("data");
                        image.setImageBitmap(photo);
                    }
                }
                break;
        }
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        Cursor query = getContentResolver().query(externalContentUri, null, selection, null, null);
        if (query != null) {
            if (query.moveToFirst()) {
                path = query.getString(query.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            query.close();
        }
        return path;
    }

    private void showPicture() {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
            image.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void displayImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
        image.setImageBitmap(bitmap);
    }

}
