package com.demo1.smsapp.api.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;

import java.io.*;

public class FileHelper {
    public static Bitmap base64ToImage(String base64String) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    public static String fileUriToBase64(Uri uri, ContentResolver resolver) {
        String encodedBase64 = "";
        try {
            byte[] bytes = readBytes(uri, resolver);
            encodedBase64 = Base64.encodeToString(bytes, 0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return encodedBase64;
    }

    public static void saveDocx(String base64String, String fileName) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS).toString();
        File myDir = new File(root + "/docx/");
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        try {
            file = new File(myDir, fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] bfile = Base64.decode(base64String, Base64.DEFAULT);
            bos.write(bfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveImage(Bitmap bitmap, String fileName, Context context) {
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/images");
        myDir.mkdirs();
        File file = new File(myDir, fileName);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (fileName.split(".")[1].equals("jpeg")) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            }
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }

    private static byte[] readBytes(Uri uri, ContentResolver resolver)
            throws IOException {
        InputStream inputStream = resolver.openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getPath(final Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            //Download Provider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // Media Provider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "unknown";
    }

    @SuppressLint("Range")
    public static String getName(Context context, Uri uri) {
        String fileName = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                // get file name
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }
        return fileName;
    }

    public static String getSize(Context context, Uri uri) {
        String fileSize = null;
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {

                // get file size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getString(sizeIndex);
                }
            }
        } finally {
            cursor.close();
        }
        return fileSize;
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
