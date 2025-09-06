package com.example.mindwalk

import android.app.Application
import android.webkit.WebView
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "c1d9974c5ab1fd02fed198dc9aa0a86b")
        WebView.setWebContentsDebuggingEnabled(true)
    }
}