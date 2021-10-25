package com.yvs.movy.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yvs.movy.R
import com.yvs.movy.sign.signin.SignInActivity
import com.yvs.movy.utils.Preferences

class OnboardingOneActivity : AppCompatActivity() {

    lateinit var preference:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        val btn_daftar = findViewById(R.id.btn_daftar) as Button
        val btn_home = findViewById(R.id.btn_home) as Button

        preference = Preferences(this)

        if (preference.getValues("onboarding").equals("1")){
            var intent = Intent(this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        btn_daftar.setOnClickListener {
            var intent = Intent(this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
            preference.setValues("onboarding", "1")
            finishAffinity()
        }

        btn_home.setOnClickListener {
            var intent = Intent(this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

    }
}