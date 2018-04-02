package taoke.com.shelldemo.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * 作者：yl
 * 时间: 2018/3/31 15:01
 * 功能：
 */

data class BaseData<T>(var success: Int,
                       val tips: String,
                       val results: T)


data class LinesBean(
        val site: Site,
        val source: List<Source>
)

data class Source(
        val name: String,
        val url: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Source> {
        override fun createFromParcel(parcel: Parcel): Source {
            return Source(parcel)
        }

        override fun newArray(size: Int): Array<Source?> {
            return arrayOfNulls(size)
        }
    }

}


data class Site(val name: String,
                val url: String,
                val image: String,
                val star: String,
                val video: String)

data class UrlBean(val url: String)