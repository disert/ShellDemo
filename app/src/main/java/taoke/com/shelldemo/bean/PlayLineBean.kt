package taoke.com.shelldemo.bean

/**
 * 作者：yl
 * 时间: 2018/3/31 15:01
 * 功能：
 */

data class PlayLineBean(
		val success: Int,
		val tips: String,
		val results: Results
)

data class Results(
		val list: List<ListBean>,
		val source: List<Source>
)

data class Source(
		val name: String,
		val url: String
)

data class ListBean(
		val sitename: String,
		val classname: String,
		val js: String
)