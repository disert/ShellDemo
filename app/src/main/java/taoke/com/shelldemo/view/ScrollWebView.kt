package taoke.com.shelldemo.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.WebView

class ScrollWebView : WebView {
    var onScrollChangedCallback: OnScrollChangedCallback? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle) {
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int,
                                 oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        Log.i("onScrollChangedCallback","l=$l , oldl=$oldl,t=$t,oldt=$oldt")
        if (onScrollChangedCallback != null) {
            onScrollChangedCallback!!.onScroll(l, t)
        }
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    interface OnScrollChangedCallback {
        fun onScroll(dx: Int, dy: Int)
    }
}