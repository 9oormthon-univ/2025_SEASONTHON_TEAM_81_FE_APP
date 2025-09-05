package com.example.mindwalk.ui.feature.login

import android.webkit.JavascriptInterface

class WebAppInterface(  private val onRequestPermission: () -> Unit) {

    @JavascriptInterface
    fun requestLocationPermission() {
        onRequestPermission()
    }

}