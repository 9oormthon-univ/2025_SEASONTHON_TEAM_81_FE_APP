import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import java.net.URISyntaxException
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MainScreen(url: String) {
    val context = LocalContext.current
    var webView: WebView? = null

    // 팝업용 WebView를 관리할 상태 변수
    var popupWebView by remember { mutableStateOf<WebView?>(null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){
        isGranted: Boolean ->
        val  result =
            if (isGranted)
                Log.d("Permission", "위치 권한이 허용되었습니다.")
            else
                Log.d("Permission", "위치 권한이 거절되었습니다.")
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Scaffold {
        innerPadding ->
        // 메인 WebView
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    webView = this // webView 참조 저장
                    settings.apply {
                        javaScriptEnabled = true
                        // DOM Storage 사용 허용
                        domStorageEnabled = true
                    }

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            if (url != null && url.startsWith("intent://")) {
                                try {
                                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                                    if (intent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(intent)
                                    } else {
                                        val marketIntent = Intent(Intent.ACTION_VIEW)
                                        marketIntent.data = Uri.parse("market://details?id=" + intent.getPackage())
                                        context.startActivity(marketIntent)
                                    }
                                    return true
                                } catch (e: URISyntaxException) {
                                    Log.e("WebView_Intent", "잘못된 Intent URI 형식입니다.", e)
                                }
                            }
                            return false
                        }
                    }
                    webChromeClient = object : WebChromeClient() {
                        override fun onGeolocationPermissionsShowPrompt(
                            origin: String?,
                            callback: GeolocationPermissions.Callback?
                        ) {
                            // 권한 요청에 대해 항상 '허용'으로 응답
                            callback?.invoke(origin, true, false)
                        }
                    }
                    loadUrl(url)
                }
            }
        )
    }

    if (popupWebView != null) {
        Dialog(onDismissRequest = { popupWebView = null }) {
            AndroidView(factory = { popupWebView!! })
        }
    }
}