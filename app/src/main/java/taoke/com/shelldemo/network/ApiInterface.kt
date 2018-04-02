package taoke.com.shelldemo.network

import retrofit2.http.*
import rx.Observable
import taoke.com.shelldemo.bean.BaseData
import taoke.com.shelldemo.bean.LinesBean
import taoke.com.shelldemo.bean.UrlBean

/**
 * 作者：yl
 * 时间: 2018/3/31 14:49
 * 功能：
 */
interface ApiInterface {

    @GET("api/site?do=video")
    fun playLine(): Observable<BaseData<LinesBean>>

    @FormUrlEncoded
    @POST()
    fun uploadUrl(@Url interUrl:String, @Field("pre_url") pre_url: String,
                  @Field("video_url") video_url: String): Observable<BaseData<UrlBean>>
}