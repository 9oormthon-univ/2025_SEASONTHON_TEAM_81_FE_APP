package com.example.mindwalk.ui.feature.login

import MainScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kakao.sdk.common.util.Utility

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var keyHash = Utility.getKeyHash(this)
        Log.i("kakaoTest", "keyHash: $keyHash")
        setContent {
            MainScreen(url = "https://mindwalk-beryl.vercel.app/")
        }
    }
}