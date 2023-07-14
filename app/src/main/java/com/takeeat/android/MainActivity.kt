package com.takeeat.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.FirebaseApp
import com.takeeat.android.adapter.AdapterRefrigeratorExpire
import com.takeeat.android.adapter.AdapterRefrigeratorNormal
import com.takeeat.android.data.api.RetrofitClient
import com.takeeat.android.data.model.RefrigeratorIngredientData
import com.takeeat.android.databinding.ActivityMainBinding
import com.takeeat.android.util.AndroidKeyStoreUtil
import com.takeeat.android.util.MyFirebaseMessagingService
import com.takeeat.android.util.UserSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var normalAdapter: AdapterRefrigeratorNormal
    private lateinit var expireAdapter: AdapterRefrigeratorExpire

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.toolbarMain.toolbar.title = ""
        setSupportActionBar(viewBinding.toolbarMain.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(R.drawable.left)
        }

        viewBinding.btnReceipt.setOnClickListener {
            val intent = Intent(this, ReceiptActivity::class.java)
            startActivity(intent)
        }

        AndroidKeyStoreUtil.init(this)
        UserSharedPreferences.initialize(this)
        RetrofitClient.initialize(this)

        /** FCM설정, Token값 가져오기 */
        MyFirebaseMessagingService().getFirebaseToken(this)


        //loadExpireData(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home ->{
                viewBinding.btnReceipt.background = getDrawable(R.drawable.button_1)
                viewBinding.btnReceipt.text = "N개로 레시피 확인하기"
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(false)
                    setHomeAsUpIndicator(R.drawable.left)
                }
            }

            R.id.insert->{
                val intent = Intent(this, InsertActivity::class.java)
                startActivity(intent)
            }

            R.id.delete->{
                viewBinding.btnReceipt.background = getDrawable(R.drawable.button_2)
                viewBinding.btnReceipt.text = "N개 냉장고에서 삭제하기"
                supportActionBar?.apply {
                    setDisplayHomeAsUpEnabled(true)
                    setHomeAsUpIndicator(R.drawable.left)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadNormalData(context: Context){
        CoroutineScope(Dispatchers.Main).launch {
            val listData = Refrigerator.getNormalData(context)
            setNormalAdapter(context, listData)
        }
    }

    private fun setNormalAdapter(context: Context, dataList: ArrayList<RefrigeratorIngredientData>){
        normalAdapter = AdapterRefrigeratorNormal(dataList)
        Refrigerator.setNormalAdapter(normalAdapter)
        viewBinding.rvRefrigeratorNormal.adapter = normalAdapter
    }

    private fun loadExpireData(context: Context){
        CoroutineScope(Dispatchers.Main).launch {
            val listData = Refrigerator.getExpireData(context)
            setExpireAdapter(context, listData)
        }
    }

    private fun setExpireAdapter(context: Context, dataList: ArrayList<RefrigeratorIngredientData>){
        expireAdapter = AdapterRefrigeratorExpire(dataList)
        Refrigerator.setExpireAdapter(expireAdapter)
        viewBinding.rvRefrigeratorExpire.adapter = expireAdapter
    }
}