package taoke.com.shelldemo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_player1.*
import kotlinx.android.synthetic.main.layout_empty_page.*
import kotlinx.android.synthetic.main.layout_top.*
import taoke.com.shelldemo.base.BaseSubscriber
import taoke.com.shelldemo.bean.BaseData
import taoke.com.shelldemo.bean.UrlBean
import taoke.com.shelldemo.cons.hideProgress
import taoke.com.shelldemo.cons.showProgress
import taoke.com.shelldemo.network.ApiUtils


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
        setContentView(R.layout.activity_player1)

        targatUrl = intent.getStringExtra("url")
        interfaceUrl = intent.getStringExtra("interfaceUrl")
        linesUrl = intent.getStringExtra("linesUrl")
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE



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
                hideProgress()
                videoView.setVideoURI(Uri.parse(t!!.results.url))
                videoView.start()
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
