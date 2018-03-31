package taoke.com.shelldemo.network


import android.util.Log
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


/**
 * 作者：yl
 * 时间: 2017/8/8 16:18
 * 功能：日志拦截器
 */
class LogInterceptor : Interceptor {

    internal var TAG = javaClass.simpleName

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val body = StringBuilder()
        if (request.body() != null && FormBody::class.java.name == request.body()!!.javaClass.name) {
            val formBody = request.body() as FormBody?
            for (i in 0 until formBody!!.size()) {
                body.append("\n").append(formBody.name(i)).append("\t").append(formBody.value(i))
            }
        }
        val response = chain.proceed(chain.request())
        val mediaType = response.body()!!.contentType()
        var content = response.body()!!.string()
        //ali支付订单接口数据需单独处理

        //解密
//        content = pareseContent(content)
        Log.w(TAG, request.url().toString() + "  " + content)
        //解析后需要单独处理的进行错误抛出
//        throwExceptionAtParesed(content, request.url().toString())

        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build()
    }


}
