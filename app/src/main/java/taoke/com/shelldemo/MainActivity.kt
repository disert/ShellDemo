package taoke.com.shelldemo

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_top.*
import taoke.com.shelldemo.base.BaseSubscriber
import taoke.com.shelldemo.bean.PlayLineBean
import taoke.com.shelldemo.network.ApiUtils

class MainActivity : BaseActivity() {

    var mData: PlayLineBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initWebView()

        initView()

        loadData()
    }

    private fun initView() {
        iv_player.setOnClickListener(this)
        iv_back.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v?.id){
            R.id.iv_player ->{

            }
        }
    }

    private fun loadData() {
        ApiUtils.request(ApiUtils.getApiInterface()!!.playLine(),object : BaseSubscriber<PlayLineBean>(){
            override fun onError(e: Throwable?) {
                super.onError(e)
            }

            override fun onNext(t: PlayLineBean?) {
                mData = t

            }
        })
    }

    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = object : WebChromeClient(){

        }
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if(checkUrl(url))
                    view?.loadUrl(url)
                return true
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
}
