package cn.coderpig.cplightupload.upload

/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc: 后台返回信息简陋封装(响应体内容交给后续响应体解析)
 */
data class Response(var statusCode: Int, var content: String)