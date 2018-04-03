package taoke.com.shelldemo

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.webkit.*
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.layout_empty_page.*
import kotlinx.android.synthetic.main.layout_top.*
import taoke.com.shelldemo.base.BaseSubscriber
import taoke.com.shelldemo.bean.BaseData
import taoke.com.shelldemo.bean.LinesBean
import taoke.com.shelldemo.bean.Source
import taoke.com.shelldemo.cons.loadImage
import taoke.com.shelldemo.network.ApiUtils
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import kotlinx.android.synthetic.main.pop_layout.view.*
import taoke.com.shelldemo.cons.hideProgress
import taoke.com.shelldemo.cons.showProgress
import taoke.com.shelldemo.view.ScrollWebView


class WebActivity : BaseActivity() {

    var mData: LinesBean? = null
    var targatUrl: String? = null
    var linePosition = 0

    companion object {
        fun newIntent(context: Context, url: String): Intent {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url", url)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

//        targatUrl = intent.getStringExtra("url")

        initWebView()

        initView()

        loadData()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun initView() {
        iv_player.setOnClickListener(this)
        tv_right_text.setOnClickListener(this)
        tv_close.setOnClickListener(this)

        ll_empty_root.visibility = View.GONE
        tv_back.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_player -> {
                linked()
            }
            R.id.tv_back -> {
                back()
            }
            R.id.tv_right_text -> showPop(v)
            R.id.tv_close -> {
                if (mData?.site?.url != null) {
                    webView.clearHistory()
                    webView.loadUrl(mData!!.site!!.url)
                    isShowBack(false)
                }
            }
        }
    }

    private fun showPop(v: View) {
        if (mPopupWindow == null) return
        if (mPopupWindow!!.isShowing()) {
            mPopupWindow!!.dismiss()
        } else {
            // 设置PopupWindow 显示的形式 底部或者下拉等
            // 在某个位置显示
            mPopupWindow!!.showAtLocation(tv_right_text, Gravity.BOTTOM, 0, 0)
            // 作为下拉视图显示
            // mPopupWindow.showAsDropDown(mPopView, Gravity.CENTER, 200, 300)

        }
    }

    fun back() {
        if (webView.canGoBack()) {
            webView.goBack()
//            iv_player.visibility = View.GONE
        }
    }

    private fun loadData() {
        showProgress("加载中...")
        ApiUtils.request(ApiUtils.getApiInterface()!!.playLine(), object : BaseSubscriber<BaseData<LinesBean>>() {
            override fun onError(e: Throwable?) {
                super.onError(e)
                hideProgress()
            }

            override fun onNext(t: BaseData<LinesBean>?) {
                hideProgress()
                mData = t?.results
                if (mData != null) {
                    webView.loadUrl(mData!!.site.url)
                    loadImage(mData!!.site.image, iv_player)
                    tv_title.text = mData!!.site.name
                    tv_right_text.text = mData!!.source[linePosition].name
                    initLines(mData!!.source)
                }

            }
        })
    }

    private fun initWebView() {
        webView.settings.javaScriptEnabled = true

        //设置自适应屏幕，两者合用
        webView.settings.useWideViewPort = true //将图片调整到适合webview的大小
        webView.settings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webView.settings.loadsImagesAutomatically = true
        webView.settings.defaultTextEncodingName = "utf-8"

        webView.onScrollChangedCallback = object : ScrollWebView.OnScrollChangedCallback{
            override fun onScroll(dx: Int, dy: Int) {
                iv_player.scrollY = dy
            }

        }
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
                    view?.loadUrl(url)
                    loadFinish(url)
                }
                return true
            }

            override fun onLoadResource(view: WebView?, url: String?) {
//                Log.w("onLoadResource",url)
                super.onLoadResource(view, url)
            }

            override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm)
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadFinish(url)
            }
        }
//        webView.loadUrl("http://v.22taoke.com")
    }

    fun isShowBack(isShow: Boolean) {
        tv_back.visibility = if (isShow) View.VISIBLE else View.GONE
        tv_close.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    fun loadFinish(url: String?) {
        isShowBack(webView.canGoBack())
        tv_title.text = webView.title
        iv_player.visibility = View.GONE
        //腾讯的
        if (url!!.startsWith("https://m.v.qq.com/x/cover")
                || url!!.startsWith("https://m.v.qq.com/cover")
                || url!!.startsWith("https://m.v.qq.com/play.html")
                //爱齐艺
                || url!!.startsWith("http://m.iqiyi.com/v_")
                //youku
//                || url!!.startsWith("http://m.youku.com/video")
                || url!!.contains("m.youku.com/video/id_")
                //tudou
//                || url!!.startsWith("http://compaign.tudou.com/v")
                //mangguo
                || url!!.startsWith("https://m.mgtv.com/b")
                //sohu
                || url!!.contains("tv.sohu.com/v")
                || url!!.contains("film.sohu.com/album")) {
            iv_player.visibility = View.VISIBLE
            targatUrl = url
            Log.w("MainActivity", targatUrl)
        }
    }

    fun linked() {
        val curUrl = webView.url
        val linesUrl = mData!!.source[linePosition].url
        Log.w("MainActivity", linesUrl)
        startActivity(PlayerActivity.newIntent(this, mData!!.site.video, curUrl, linesUrl))
    }

    private fun checkUrl(url: String?): Boolean {
        if (url != null && url.startsWith("http")) {
            return true
        }
        if (url != null && !url.startsWith("http")) {
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.data = Uri.parse(url)
//            startActivity(intent)
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

    var mPopView: View? = null
    var mPopupWindow: PopupWindow? = null
    private fun initLines(source: List<Source>) {
        // 将布局文件转换成View对象，popupview 内容视图
        mPopView = layoutInflater.inflate(R.layout.pop_layout, null)
        // 将转换的View放置到 新建一个popuwindow对象中
        mPopupWindow = PopupWindow(mPopView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        mPopupWindow!!.setContentView(mPopView)
        val adapter = MyAdapter()
        mPopView!!.recyclerview.layoutManager = LinearLayoutManager(this)
        mPopView!!.recyclerview.adapter = adapter

        // 点击popuwindow外让其消失
        mPopupWindow!!.setOutsideTouchable(true)
        // mpopupWindow.setBackgroundDrawable(background);
    }

    inner class MyAdapter : RecyclerView.Adapter<MyHolder>() {
        override fun getItemCount(): Int {
            return if (mData?.source?.size == null) 0 else mData!!.source.size
        }

        override fun onBindViewHolder(holder: MyHolder?, position: Int) {
            holder!!.textView!!.text = mData!!.source[position].name
            holder!!.textView!!.setOnClickListener {
                linePosition = position
                tv_right_text.text = mData!!.source[position].name
                mPopupWindow?.dismiss()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
            val view = LayoutInflater.from(this@WebActivity).inflate(R.layout.item_line, parent, false)
            return MyHolder(view)
        }

    }

    class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var textView: TextView? = null

        init {
            textView = itemview as TextView
        }
    }
}
