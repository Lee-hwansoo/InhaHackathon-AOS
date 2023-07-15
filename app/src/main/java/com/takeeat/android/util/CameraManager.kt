package com.takeeat.android.util

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.*

class CameraManager {
    fun checkSelfPermission(context: Context, nowActivity: AppCompatActivity) {
        var temp = ""

        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " "
        }

        if (!TextUtils.isEmpty(temp)) {
            Log.d("permission", "checkSelfPermission: 권한을 요청합니다.")
            // 권한 요청
            ActivityCompat.requestPermissions(
                nowActivity,
                temp.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray(),
                1)
        }
    }

    fun checkSelfCameraPermission(context: Context, nowActivity: AppCompatActivity, runFunction: () -> Unit) {
        var temp = ""

        //카메라 권한 확인
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            temp += Manifest.permission.CAMERA + " "
        }
//        //파일 쓰기 권한 확인
//        if (ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " "
//        }
        if (!TextUtils.isEmpty(temp)) {
            Log.d("permission", "checkSelfPermission: 권한을 요청합니다.")
            // 권한 요청
            ActivityCompat.requestPermissions(
                nowActivity,
                temp.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray(),
                0)
        }else{
            runFunction()
        }
    }

    fun returnEmptyUri(context: Context): Uri {
        val cachePath = File(context.externalCacheDir, "addImages")
        if(!cachePath.exists()) cachePath.mkdirs() // don't forget to make the directory
        val imageCachePath  ="$cachePath/${getTodayMillis()}.jpeg"
        val file = File(imageCachePath)
        file.createNewFile()
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    private fun getTodayMillis(): Long{
        val calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
            Calendar.DAY_OF_MONTH), 0, 0, 0);
        Log.d("testTime", "getTodayMillis:${calendar.time} ")
        return System.currentTimeMillis() - calendar.timeInMillis;
    }

    fun getCachePathUseUri(context: Context, uri: Uri, quality: Int, sizeLimit: Double): String{
        val cR: ContentResolver = context.contentResolver
        var type: String? = cR.getType(uri)

        return if(type == "image/png" || type == "image/jpeg"){
            val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
            if(bitmap.byteCount <= sizeLimit){
                createImageCachePath(context, bitmap, quality)
            }else {
                Toast.makeText(context, "사진 사이즈가 너무 큽니다.", Toast.LENGTH_SHORT).show()
                Log.d("TestImagePath", "getCachePathUseUri: 사진 사이즈가 너무 큽니다.")
                ""
            }
        }else{
            Toast.makeText(context, "png 및 jpg(jpeg)형식의 사진만 지원합니다.", Toast.LENGTH_SHORT).show()
            Log.d("TestImagePath", "getCachePathUseUri: png 및 jpg(jpeg)형식의 사진만 지원합니다" + type)
            ""
        }
    }

    fun createImageCachePath(context:Context, bitmap: Bitmap, quality: Int): String{
        val cachePath = File(context.externalCacheDir, "addImages")
        if(!cachePath.exists()) cachePath.mkdirs() // don't forget to make the directory
        val imageCachePath  ="$cachePath/${getTodayMillis()}.png"
        val stream = FileOutputStream(imageCachePath) // overwrites this image every time
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
        stream.close()
        return imageCachePath;
    }
}