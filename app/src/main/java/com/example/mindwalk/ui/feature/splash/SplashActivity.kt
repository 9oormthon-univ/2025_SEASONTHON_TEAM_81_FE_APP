package com.example.mindwalk.ui.feature.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.mindwalk.ui.feature.login.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashScreen() // 1. 스플래시 UI를 화면에 표시
        }

        // 2. 2초 후에 메인 액티비티로 이동
        lifecycleScope.launch {
            delay(2000) // 2초 대기
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // 스플래시 액티비티 종료
        }
    }
}