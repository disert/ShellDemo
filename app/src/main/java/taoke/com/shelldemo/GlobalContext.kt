package taoke.com.shelldemo

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.umeng.analytics.MobclickAgent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import taoke.com.shelldemo.cons.Constants
import taoke.com.shelldemo.network.ApiInterface
import taoke.com.shelldemo.network.LogInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 应用程序入口 由于加入了shareSDK需要以他的application作为父类 com.mob.MobApplication
 */
class GlobalContext : Application() {

    var apiInterface: ApiInterface? = null
    var httpClient: OkHttpClient? = null
    var retrofit: Retrofit? = null

    /**
     * 获取到错误日志的路径
     *
     * @return
     */
    private val crashLogDir: String
        get() {

            val mCacheRoot: String
            val dirF = getExternalFilesDir("")
            if (dirF == null) {
                mCacheRoot = filesDir.toString()
            } else {
                mCacheRoot = dirF.toString()
            }

            val dir = "$mCacheRoot/crash"
            val f = File(dir)
            if (!f.exists()) {
                f.mkdirs()
            }
            return dir
        }

    // 应用程序的入口
    override fun onCreate() {

        super.onCreate()
        // 上下文
        context = this
        //初始化retrofit
        initInterface()

        //设置全局字体样式
        //        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/rtw_type.otf");

        //异常处理
        val crashHandler = CrashHandler.instance
        crashHandler.init(applicationContext, crashLogDir)

        //友盟
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL)
    }

    private fun initInterface() {
        httpClient = provideOkHttpClient()
        retrofit = provideRetrofit(httpClient!!)
        apiInterface = retrofit!!.create(ApiInterface::class.java)
    }
    internal fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build()
    }

    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(LogInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    override fun attachBaseContext(base: Context) {

        super.attachBaseContext(base)
        //解决方法数过多
        MultiDex.install(this)
    }

    companion object {

        var context: GlobalContext? = null
            private set
    }

}
