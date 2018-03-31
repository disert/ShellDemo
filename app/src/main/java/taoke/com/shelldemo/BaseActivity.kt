package taoke.com.shelldemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.layout_empty_page.*
import kotlinx.android.synthetic.main.layout_top.*
import taoke.com.shelldemo.utils.AppManager

open class BaseActivity : AppCompatActivity(),View.OnClickListener {
    var view: View? = null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.iv_back -> finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.appManager.addActivity(this)
    }

    override fun setContentView(layoutResID: Int) {
        view = View.inflate(this,R.layout.activity_base,null)
        val content_root = view?.findViewById<FrameLayout>(R.id.content_root)
        content_root?.addView(LayoutInflater.from(this).inflate(layoutResID,null))
        super.setContentView(view)

        emptyPage(false)
        iv_back.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.appManager.removeActivity(this)
    }

    fun loading(isLoading: Boolean){

    }
    fun emptyPage(isEmpty: Boolean){
        if(isEmpty)ll_empty_root.visibility = View.GONE
        else ll_empty_root.visibility = View.VISIBLE
    }
}
