package taoke.com.shelldemo.cons

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.functions.Func1
import rx.schedulers.Schedulers
import taoke.com.shelldemo.GlobalContext
import taoke.com.shelldemo.utils.ToastManager
import taoke.com.shelldemo.view.PublicDialog
import taoke.com.shelldemo.view.CustomProgress



/**
 * 作者：yl
 * 时间: 2018/3/22 17:51
 * 功能：方法扩展
 */

fun <T, R> Activity.runAsyncTask(t: T, action: Func1<T, R>) {
    runAsyncTask(t, action, Action1 { })
}

fun <T, R> Activity.runAsyncTask(t: T, func: Func1<T, R>, action: Action1<R>) {

    Observable.just(t)
            .map(func)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action)
}

fun <T, R> Fragment.runAsyncTask(t: T, action: Func1<T, R>) {
    runAsyncTask(t, action, Action1 { })
}

fun <T, R> Fragment.runAsyncTask(t: T, func: Func1<T, R>, action: Action1<R>) {

    Observable.just(t)
            .map(func)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action)
}

fun Any.showToast(msg: String) {
    ToastManager.showShort(GlobalContext.context, msg)
}

fun Any.showToast(msg: Int) {
    ToastManager.showShort(GlobalContext.context, msg)
}

fun Any.showHintDialog(context: Context, hint: String, listener: View.OnClickListener) {

    showSubmitDialog(context,
            "取消",
            "确定", hint, null, listener)
}

fun Any.showHintDialog(context: Context, left: String, right: String, hint: String, listener: View.OnClickListener) {

    showSubmitDialog(context,
            left,
            right,
            hint,
            null,
            listener)
}

fun Any.showHintDialog(context: Context, hint: Int, listener: View.OnClickListener) {

    showHintDialog(context,
            GlobalContext.context!!.getString(hint), listener)
}

fun Any.showSubmitDialog(context: Context, title: String, left: String, right: String, msg: String?, negative: View.OnClickListener, positive: View.OnClickListener) {

    PublicDialog.Builder(context).setContent(msg)
            .setTitle(title)
            .setCancel(left)
            .setConform(right)
            .setCancelListenr(negative)
            .setConformListenr(positive)
            .show()
}

fun Any.isNetworkAvailable(context: Context): Boolean {

    val cm = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false
    val netInfo = cm.activeNetworkInfo ?: return false
    return netInfo.isAvailable
}

fun Any.showSubmitDialog(context: Context, left: String, right: String, msg: String, negative: View.OnClickListener?, positive: View.OnClickListener): PublicDialog {

    val dialog = PublicDialog.Builder(context).setContent(msg)
            .setCancel(left)
            .setConform(right)
            .setCancelListenr(negative)
            .setConformListenr(positive).build()
    dialog.show()
    return dialog
}
fun Activity.loadImage(url:String,imageView:ImageView){
    Glide.with(this).load(url).into(imageView)
}

fun Fragment.loadImage(url:String,imageView:ImageView){
    Glide.with(this).load(url).into(imageView)
}

private var mProgressDialog: CustomProgress? = null
fun Activity.showProgress(message: String) {
    if(isFinishing || isDestroyed)return
    if (null == mProgressDialog) {
        mProgressDialog = CustomProgress
                .show(this, message, true, null)
    } else {
        if (!mProgressDialog!!.isShowing) {
            mProgressDialog!!.show()
        }
    }
}

fun Any.hideProgress() {
    if (null != mProgressDialog && mProgressDialog!!.isShowing) {
        mProgressDialog!!.dismiss()
    }
}