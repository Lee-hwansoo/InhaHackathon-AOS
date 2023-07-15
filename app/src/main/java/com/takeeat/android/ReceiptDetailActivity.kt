package com.takeeat.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.takeeat.android.databinding.ActivityReceiptBinding
import com.takeeat.android.databinding.ActivityReceiptDetailBinding
import com.takeeat.android.databinding.PopupDoneBinding
import com.takeeat.android.databinding.PopupPhotoBinding

class ReceiptDetailActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityReceiptDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityReceiptDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.toolbarReceiptDetail.toolbar.title = ""
        setSupportActionBar(viewBinding.toolbarReceiptDetail.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.left)
        }

        viewBinding.btnDone.setOnClickListener {
            getBottomMenu()
        }

        viewBinding.btnDone.isSelected = true

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
        val dialogLayout = PopupDoneBinding.inflate(layoutInflater)
        dialogLayout.btnDone.isSelected = true
        dialogLayout.btnDone.setOnClickListener {
            dialog.cancel()
        }
        dialog.setContentView(dialogLayout.root)
        dialog.setCanceledOnTouchOutside(true)  // BottomSheetdialog 외부 화면(회색) 터치 시 종료 여부 boolean(false : ㄴㄴ, true : 종료하자!)
        dialog.create() // create()와 show()를 통해 출력!
        dialog.show()
    }
}