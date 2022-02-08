package com.cornershop.counterstest.presentation.welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.databinding.ActivityWelcomeBinding
import com.cornershop.counterstest.presentation.main.MainActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setGetStartedListener()
        sharedPreferences = getSharedPreferences("counterTest", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("firstTime", true)) {
            setContentView(binding.root)
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun setGetStartedListener() {
        binding.welcomeContent!!.buttonStart.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("firstTime", false)
            editor.commit()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
