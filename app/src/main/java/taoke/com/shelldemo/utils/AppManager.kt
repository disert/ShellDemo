package taoke.com.shelldemo.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import taoke.com.shelldemo.MainActivity
import java.util.*

/**
 * 作者：yl
 * 时间: 2017/8/16 17:27
 * 功能：Activity管理类
 */
class AppManager private constructor() {
    internal val TAG = javaClass.simpleName

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 移除Activity到堆栈
     */
    fun removeActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.remove(activity)
        removeFinish()
        Log.d("退出activity", "还有" + activityStack!!.size + "页面")
        //移除之后如果发现不是首页的退出,并且没有数据之后,主动启动首页
        if (activity !is MainActivity && activityStack!!.isEmpty()) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    fun removeFinish() {

        var i = 0
        while (activityStack!!.size > i) {
            val activity = activityStack!![i]
            if (activity == null || activity.isFinishing) {
                activityStack!!.remove(activity)
                continue
            }
            i++
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i].finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 回到首页的Activity
     */
    fun goHome() {
        var main: Activity? = null
        while (true) {
            if (!activityStack!!.isEmpty()) {
                val pop = activityStack!!.pop()
                if (null != pop) {
                    if (pop is MainActivity) {
                        main = pop
                    } else {
                        pop.finish()
                    }
                }
            } else {
                if (main != null) activityStack!!.add(main)
                return
            }
        }
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.restartPackage(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private var activityStack: Stack<Activity>? = null

        private var instance: AppManager? = null

        /**
         * 单一实例
         */
        val appManager: AppManager
            get() {
                if (instance == null) {
                    instance = AppManager()
                }
                return instance!!
            }
    }
}
