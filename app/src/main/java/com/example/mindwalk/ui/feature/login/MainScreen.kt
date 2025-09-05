import android.Manifest
import android.annotation.SuppressLint
import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.mindwalk.ui.feature.login.WebAppInterface

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


    // 메인 WebView
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            WebView(it).apply {
                webView = this
                webViewClient = WebViewClient()

                // 새 창을 열 수 있도록 설정 추가
                settings.javaScriptEnabled = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.setSupportMultipleWindows(true)

                // 새 창 열기 요청을 처리할 WebChromeClient 설정
                webChromeClient = object : WebChromeClient() {
                    override fun onCreateWindow(
                        view: WebView?,
                        isDialog: Boolean,
                        isUserGesture: Boolean,
                        resultMsg: Message?
                    ): Boolean {
                        // 새 WebView를 만들어서 팝업으로 띄움
                        val newWebView = WebView(context).apply {
                            settings.javaScriptEnabled = true
                            webViewClient = WebViewClient() // 팝업 내에서 이동을 처리
                        }

                        // 팝업 WebView를 상태에 저장
                        popupWebView = newWebView

                        // 새로 만든 WebView를 시스템에 알려줌 (가장 중요)
                        val transport = resultMsg?.obj as WebView.WebViewTransport
                        transport.webView = newWebView
                        resultMsg.sendToTarget()
                        return true
                    }

                    override fun onCloseWindow(window: WebView?) {
                        super.onCloseWindow(window)
                        // 팝업창 닫기 요청 시 팝업을 숨김
                        popupWebView = null
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

    // ✅ 3. 팝업 WebView를 보여줄 다이얼로그
    if (popupWebView != null) {
        Dialog(onDismissRequest = { popupWebView = null }) {
            AndroidView(factory = { popupWebView!! })
        }
    }
}