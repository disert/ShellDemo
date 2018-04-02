package taoke.com.shelldemo

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.layout_empty_page.*
import kotlinx.android.synthetic.main.layout_top.*
import taoke.com.shelldemo.base.BaseSubscriber
import taoke.com.shelldemo.bean.BaseData
import taoke.com.shelldemo.network.ApiUtils
import taoke.com.shelldemo.bean.UrlBean
import taoke.com.shelldemo.cons.hideProgress
import taoke.com.shelldemo.cons.showProgress


class Player1Activity : BaseActivity() {

    var targatUrl: String? = null
    var interfaceUrl: String? = null
    var linesUrl: String? = null
    var isLandscape = false
    var mCallback: WebChromeClient.CustomViewCallback? = null

    companion object {
        fun newIntent(context: Context, interfaceUrl: String, url: String, linesUrl: String): Intent {
            val intent = Intent(context, Player1Activity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("linesUrl", linesUrl)
            intent.putExtra("interfaceUrl", interfaceUrl)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        targatUrl = intent.getStringExtra("url")
        interfaceUrl = intent.getStringExtra("interfaceUrl")
        linesUrl = intent.getStringExtra("linesUrl")
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE


        initWebView()

        initView()


        loadData()

    }

//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//
//        if(newConfig!!.orientation==Configuration.ORIENTATION_PORTRAIT){
//            isLandscape = false
//        }else if(newConfig!!.orientation==Configuration.ORIENTATION_LANDSCAPE){
//            isLandscape = true
//        }
//    }
    private fun orientationChange(isLandscape: Boolean){
        if(isLandscape){
            top.visibility = View.GONE
//            tv_cover.visibility = View.GONE
        }else{
            top.visibility = View.VISIBLE
//            tv_cover.visibility = View.VISIBLE
        }
    }

    private fun loadData() {
        val pre_url = linesUrl!!
        val videoUrl = targatUrl!!
        val intUrl = interfaceUrl!!
        showProgress("加载中...")
        ApiUtils.request(ApiUtils.getApiInterface()!!.uploadUrl(intUrl, pre_url, videoUrl), object : BaseSubscriber<BaseData<UrlBean>>() {
            override fun onError(e: Throwable?) {
                super.onError(e)
                hideProgress()
            }

            override fun onNext(t: BaseData<UrlBean>?) {
                webView.loadUrl(t?.results?.url)
                hideProgress()
            }
        })
    }

    private fun initView() {
        tv_right_text.visibility = View.GONE
        ll_empty_root.visibility = View.GONE
        tv_close.visibility = View.VISIBLE
        tv_close.setOnClickListener(this)
        top.visibility = View.GONE
//        tv_cover.visibility = View.GONE
//        orientationChange(true)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_close -> finish()
        }
    }

    fun back() {
        finish()
    }


    private fun initWebView() {
        webView.settings.javaScriptEnabled = true

        //设置自适应屏幕，两者合用
        webView.settings.useWideViewPort = true //将图片调整到适合webview的大小
        webView.settings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webView.settings.loadsImagesAutomatically = true
        webView.settings.defaultTextEncodingName = "utf-8"


        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
//                if(newProgress == 100)
//                    view?.loadUrl(getDomOperationStatements(arrayOf("s_tab", "page-tips")))
            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                webView.visibility = View.GONE
                fl_container.visibility = View.VISIBLE
                cover.visibility = View.GONE
                fl_container.addView(view)
                mCallback=callback

                super.onShowCustomView(view, callback)
            }

            override fun onHideCustomView() {
                fullScreen()
                if (mCallback!=null){
                    mCallback!!.onCustomViewHidden()
                }
                webView.visibility = View.VISIBLE
                cover.visibility = View.VISIBLE
                fl_container.removeAllViews()
                fl_container.visibility = View.GONE

                super.onHideCustomView()
            }
            private fun fullScreen() {
//                requestedOrientation = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                } else {
//                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                }
            }



        }
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (checkUrl(url)) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
//                tv_cover.visibility = View.VISIBLE
            }

        }
    }


    private fun checkUrl(url: String?): Boolean {
        if (url != null && url.startsWith("http")) {
            return true
        }
        return false
    }

    fun urlCtrl(url: String?): String? {
        var split = url!!.replace("m.", "")
        var result = split!!.split("html")[0] + "html"
        return result
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}
