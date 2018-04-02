package taoke.com.shelldemo

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_empty_page.*
import kotlinx.android.synthetic.main.layout_top.*

class MainActivity : BaseActivity() {

    var targatUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWebView()

        initView()

    }

    private fun initView() {
        tv_back.visibility = View.GONE
        ll_empty_root.visibility = View.GONE
        tv_title.text = "影视中心"
    }



    private fun initWebView() {
        webView.settings.javaScriptEnabled = true

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
//                if(newProgress == 100)
//                    view?.loadUrl(getDomOperationStatements(arrayOf("s_tab", "page-tips")))
            }


        }
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (checkUrl(url)) {
                    if(url!!.startsWith("http://v.22taoke.com")) view?.loadUrl(url)
                    else{
                        val intent = Intent(this@MainActivity,WebActivity::class.java)
                        intent.putExtra("url",url)
                        startActivity(intent)
//                        startActivity(WebActivity.newIntent(this@MainActivity,url))
                    }
                }

                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                tv_title.text = webView.title


            }
        }
        webView.loadUrl("http://v.22taoke.com")
    }


    private fun checkUrl(url: String?): Boolean{
        if(url != null && url.startsWith("http")){
            return true
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
