package com.takeeat.android

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ImageReader
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.takeeat.android.data.model.ImageItem
import com.takeeat.android.databinding.ActivityInsertBinding
import com.takeeat.android.databinding.ActivityMainBinding
import com.takeeat.android.databinding.PopupPhotoBinding
import java.io.File
import java.util.ArrayList

class InsertActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityInsertBinding
    var cameramanager = CameraManager()
    private var tempUri = Uri.EMPTY
    var imageItemList = ArrayList<ImageItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityInsertBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.toolbarInsert.toolbar.title = ""
        setSupportActionBar(viewBinding.toolbarInsert.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.left)
        }



        viewBinding.btnSearch.setOnClickListener {

        }

        viewBinding.btnCancel.setOnClickListener {

        }

        viewBinding.btnInsert.setOnClickListener {

        }

        viewBinding.btnScan.setOnClickListener {
            getBottomMenu()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home ->{
                Toast.makeText(this, "뒤로가기", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getBottomMenu(){
        // BottomSheetDialog 객체 생성. param : Context
        val dialog = BottomSheetDialog(this)
        val dialogLayout = PopupPhotoBinding.inflate(layoutInflater)
        dialogLayout.cameraSection.setOnClickListener {
            getImageFromCamera()
            dialog.dismiss()
        }
        dialogLayout.imageSection.setOnClickListener {
            getImageFromFile.launch("image/*")
            dialog.dismiss()
        }
        dialogLayout.cancelButton.setOnClickListener {
            dialog.cancel()
        }
        dialog.setContentView(dialogLayout.root)
        dialog.setCanceledOnTouchOutside(true)  // BottomSheetdialog 외부 화면(회색) 터치 시 종료 여부 boolean(false : ㄴㄴ, true : 종료하자!)
        dialog.create() // create()와 show()를 통해 출력!
        dialog.show()
    }

    private val getImageFromFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if(uri != null){
            val copyImagePath = cameramanager.getCachePathUseUri(this, uri, 50, 2e+7)
            if(copyImagePath != "") addImageToList(uri, copyImagePath)
            else Toast.makeText(this, "20MB이하 크기의 PNG, JPEG파일만 지원됩니다. ", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getImageFromCamera(){
        cameramanager.checkSelfCameraPermission(this, this) {openCamera()}
    }

    private fun openCamera() {
        tempUri = cameramanager.returnEmptyUri(this)
        getImageFromCameraCallback.launch(tempUri)
    }

    private val getImageFromCameraCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if(it){
            val copyImagePath = cameramanager.getCachePathUseUri(this, tempUri, 50, 2e+7)
            if(copyImagePath != "") addImageToList(tempUri, copyImagePath)
            else Toast.makeText(this, "사진을 추가하는데 알 수 없는 이유로 실패했습니다.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "취소 되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addImageToList(imageUri: Uri, copyImagePath: String){
        Log.d("test", "newPath: $copyImagePath")
        val nowImageItem = ImageItem(imageUri, copyImagePath)
        imageItemList.add(nowImageItem)
    }


}