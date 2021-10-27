package com.yvs.movy.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yvs.movy.R
import com.yvs.movy.home.HomeActivity
import kotlinx.android.synthetic.main.activity_check_out_success.*

class CheckOutSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out_success)

        btn_home.setOnClickListener {
            var intent = Intent(this@CheckOutSuccessActivity, HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }
}