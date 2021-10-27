package com.yvs.movy.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yvs.movy.Plays.CheckoutAdapter
import com.yvs.movy.R
import com.yvs.movy.model.Checkout
import com.yvs.movy.utils.Preferences
import kotlinx.android.synthetic.main.activity_check_out.*

class CheckOutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        preferences = Preferences(this)
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>

        for (a in dataList.indices){
            total += dataList[a].harga!!.toInt()

        }

        dataList.add(Checkout("Total Harus Dibayar", total.toString()))

        rv_checkout.layoutManager = LinearLayoutManager(this)
        rv_checkout.adapter = CheckoutAdapter(dataList){

        }

        btn_tiket.setOnClickListener {
            var intent = Intent(this@CheckOutActivity, CheckOutSuccessActivity::class.java)
            startActivity(intent)
        }

    }
}