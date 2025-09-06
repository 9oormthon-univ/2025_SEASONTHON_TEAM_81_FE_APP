import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
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
import com.example.mindwalk.ui.feature.login.WebAppInterface
import java.net.URISyntaxException

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
        val  result = if (isGranted) "granted" else "denied"

        webView?.post{
            webView?.evaluateJavascript("javascript:requestLocationPermission($result)", null)
        }
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

                    val jsBridge = WebAppInterface(
                        onRequestPermission = {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                    )
                    addJavascriptInterface(jsBridge, "AndroidApp")
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