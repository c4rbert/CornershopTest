package com.cornershop.counterstest.presentation.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cornershop.counterstest.databinding.ActivityWelcomeBinding
import com.cornershop.counterstest.presentation.main.MainActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setGetStartedListener()
        setContentView(binding.root)
    }

    private fun setGetStartedListener(){
        binding.welcomeContent!!.buttonStart.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
