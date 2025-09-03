package com.example.mindwalk.ui.feature.login

import android.content.Context
import android.webkit.JavascriptInterface
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class WebAppInterface(
    private val context: Context,
    private val onTokenReceived: (String) -> Unit,
    private val onLoginFailed: () -> Unit) {

    @JavascriptInterface
    fun requestKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                handleLoginResult(token, error)
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                handleLoginResult(token, error)
            }
        }
    }

    private fun handleLoginResult(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            // 로그인 실패 처리
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                onLoginFailed()
            }
        } else if (token != null) {
            // 로그인 성공! 웹에 Access Token 전달
            onTokenReceived(token.accessToken)
        }
    }
}