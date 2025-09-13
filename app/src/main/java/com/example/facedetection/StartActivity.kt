package com.example.facedetection

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetection.databinding.StartactivityBinding

class StartActivity : AppCompatActivity() {
    private val binding by lazy { StartactivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Set up the Get Started button to navigate to MainActivity
        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close StartActivity after navigating
        }
    }
}
