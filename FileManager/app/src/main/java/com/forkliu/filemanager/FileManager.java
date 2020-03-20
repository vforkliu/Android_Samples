package com.forkliu.filemanager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private Context context;
    private static final  String LOG_TAG = "FileManager";

    public void dump(){
        // dumpMusic();
        // dumpVideo();
        // dumpImage();
        // queryImage("Screenshot_20200318-143902.png");
        insertImage("Screenshot_20200318-143902.png");
    }

    public void insertImage(String name){
        File src = new File(context.getExternalFilesDir("images"),name);
        inserImagesIntoMediaStore(context,"filemanager_" + name, src.getAbsolutePath());
    }

    public static Uri inserImagesIntoMediaStore(Context context, String fileName,String srcPath) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/FileManager/");
        contentValues.put(MediaStore.Video.Media.MIME_TYPE, "image/png");


        try{
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri,"w");
            FileOutputStream fos = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
            FileInputStream fis = new FileInputStream(new File(srcPath));
            copy(fis,fos);

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static void copy(ParcelFileDescriptor parcelFileDescriptor, File dst) throws IOException {
        FileInputStream istream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
        try {
            FileOutputStream ostream = new FileOutputStream(dst);
            try {
                copy(istream, ostream);
            } finally {
                ostream.close();
            }
        } finally {
            istream.close();
        }
    }


    public static void copy(InputStream ist, OutputStream ost) throws IOException {
        byte[] buffer = new byte[4096];
        int byteCount = 0;
        while ((byteCount = ist.read(buffer)) != -1) {  // 循环从输入流读取 buffer字节
            ost.write(buffer, 0, byteCount);        // 将读取的输入流写入到输出流
        }
    }

    public void copyMediaStore(Uri uri,String name){
        File imagesDir = context.getExternalFilesDir("images");
        if (!imagesDir.exists()){
            imagesDir.mkdirs();
        }

        File dstPath = new File(imagesDir,name);
        try {
            ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            copy(parcelFileDescriptor,dstPath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void queryImage(String imageName){
        Cursor c = null;
        try {
            String selection = String.format("%s=? AND (%s=? OR %s=?)",MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.MIME_TYPE);
            String[] selectionArgs = new String[]{
                    imageName,
                    "image/jpeg",
                    "image/png"
            };
            ContentResolver cr = context.getContentResolver();
            c = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    MediaStore.Images.Media.DATE_MODIFIED + " LIMIT 1"
            );

            while (c.moveToNext()) {

                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));// 路径
                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));// 视频的id
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)); // 视频名称
                Uri uri = getRealImagePath(id);
                Log.i(LOG_TAG,String.format("images:%s,%s,%d",path,name,id));
                Log.i(LOG_TAG,String.format("images:%s|%s",uri.toString(),cr.getType(uri)));
                // cr.getType(uri);
                // Screenshot_20200318-143902.png
                copyMediaStore(uri,name);


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public void dumpImage(){
        Cursor c = null;
        try {
            ContentResolver cr = context.getContentResolver();
            c = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Images.Media.MIME_TYPE + "= ? or " + MediaStore.Images.Media.MIME_TYPE + "= ?",
                    new String[]{"image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED
            );

            while (c.moveToNext()) {

                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));// 路径
                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));// 视频的id
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)); // 视频名称
                Uri uri = getRealImagePath(id);
                Log.i(LOG_TAG,String.format("images:%s,%s,%d",path,name,id));
                Log.i(LOG_TAG,String.format("images:%s|%s",uri.toString(),cr.getType(uri)));
                // cr.getType(uri);
                // Screenshot_20200318-143902.png


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    private Uri getRealImagePath(int id){
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
    }

    public void dumpVideo(){
        Cursor c = null;
        try {
            ContentResolver cr = context.getContentResolver();
            c = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));// 路径
                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Video.Media._ID));// 视频的id
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 视频名称
                Log.i(LOG_TAG,String.format("video:%s,%s,%d",path,name,id));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    public void dumpMusic(){

        Cursor c = null;
        try {
            ContentResolver cr = context.getContentResolver();
            c = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名
                int id = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));// 歌曲的id
                Log.i(LOG_TAG,String.format("audio:%s,%s,%d",path,name,id));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }
    private FileManager(Context context){
        this.context = context;
    }

    private static volatile FileManager INSTANCE = null;
    public static FileManager getInstance(Context context){
        if (INSTANCE == null){
            synchronized (FileManager.class){
                if (INSTANCE == null){
                    INSTANCE = new FileManager(context);
                }
            }
        }
        return INSTANCE;
    }
}
