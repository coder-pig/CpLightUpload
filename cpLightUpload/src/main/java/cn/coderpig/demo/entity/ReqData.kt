package cn.coderpig.demo.entity

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 请求数据实体
 */
data class ReqData(
    var uploadUrl: String? = null,   // 上传服务器地址
    var headers: ArrayList<HashMap<String, String>>? = null, // 请求头
    var params: ArrayList<HashMap<String, String>>? = null, // GET请求参数
    var data: ArrayList<HashMap<String, String>>? = null, // POST请求参数
    var timeout: Int = 10 * 1000 // 超时时间
)