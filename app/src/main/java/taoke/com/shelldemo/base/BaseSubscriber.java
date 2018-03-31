package taoke.com.shelldemo.base;

import com.umeng.analytics.MobclickAgent;

import rx.Subscriber;
import rx.Subscription;
import taoke.com.shelldemo.GlobalContext;

/**
 * 作者：yl
 * 时间: 2017/8/21 10:43
 * 功能：订阅者的基类 方便书写
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private Subscription mSubscription;

    public void setSubscription(Subscription subscription) {

        mSubscription = subscription;
    }

    @Override
    public void onCompleted() {

        if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void onError(Throwable e) {
        if (mSubscription != null) mSubscription.unsubscribe();
        MobclickAgent.reportError(GlobalContext.Companion.getContext(), e);
    }

    @Override
    public void onNext(T t) {

    }
}
