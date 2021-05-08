package mgmix.dev.collection_of_android_project.browser

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import mgmix.dev.collection_of_android_project.R

class BrowserActivity : AppCompatActivity() {

    private val webView: WebView by lazy { findViewById(R.id.webView) }
    private val address: EditText by lazy { findViewById(R.id.addressBar) }
    private val home: ImageButton by lazy { findViewById(R.id.homeButton) }
    private val back: ImageButton by lazy { findViewById(R.id.backButton) }
    private val forward: ImageButton by lazy { findViewById(R.id.forwardButton) }
    private val refresh: SwipeRefreshLayout by lazy { findViewById(R.id.refresh) }
    private val loading: ContentLoadingProgressBar by lazy { findViewById(R.id.loading) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        initViews()
        bindViews()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else super.onBackPressed()
    }

    private fun bindViews() {
        address.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) webView.loadUrl(v.text.toString())

            val loadUrl = v.text.toString()
            if (URLUtil.isNetworkUrl(loadUrl)) {
                webView.loadUrl(loadUrl)
            } else {
                webView.loadUrl("http://$loadUrl")
            }

            return@setOnEditorActionListener false
        }

        home.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }

        forward.setOnClickListener {
            webView.goForward()
        }

        back.setOnClickListener {
            webView.goBack()
        }

        refresh.setOnRefreshListener {
            webView.reload()
        }
    }

    inner class WebViewClient : android.webkit.WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            loading.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            refresh.isRefreshing = false
            loading.hide()
            back.isEnabled = webView.canGoBack()
            forward.isEnabled = webView.canGoForward()
            address.setText(url)
        }
    }

    inner class WebChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            loading.progress = newProgress
        }
    }

    companion object {
        private const val DEFAULT_URL = "http://www.google.com"
    }

}