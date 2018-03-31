package taoke.com.shelldemo.network

import retrofit2.http.GET
import rx.Observable
import taoke.com.shelldemo.bean.PlayLineBean

/**
 * 作者：yl
 * 时间: 2018/3/31 14:49
 * 功能：
 */
interface ApiInterface {

    @GET("api/site?do=video")
    fun playLine(): Observable<PlayLineBean>
}