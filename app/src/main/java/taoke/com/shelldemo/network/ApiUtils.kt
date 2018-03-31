package taoke.com.shelldemo.network

import android.accounts.NetworkErrorException
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import taoke.com.shelldemo.GlobalContext
import taoke.com.shelldemo.R
import taoke.com.shelldemo.base.BaseSubscriber
import taoke.com.shelldemo.cons.isNetworkAvailable
import taoke.com.shelldemo.utils.ToastManager

/**
 * 作者：yl
 * 时间: 2018/3/31 15:18
 * 功能：
 */
object ApiUtils {
    fun getApiInterface(): ApiInterface? {
        return GlobalContext.context?.apiInterface
    }

    fun <T> request(observable: Observable<T>, subscriber: Subscriber<T>) {
        if (!isNetworkAvailable(GlobalContext.context!!.applicationContext)) {
            ToastManager.showShort(GlobalContext.context, R.string.check_network_is_available)
            subscriber.onError(NetworkErrorException(""))
            return
        }
        val subscribe = observable.subscribeOn(Schedulers.io())
                .onBackpressureDrop()//防止MissingBackpressureException异常,把来不及处理的数据给丢掉
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)
        if (subscriber is BaseSubscriber) (subscriber as BaseSubscriber).setSubscription(subscribe)

    }
}