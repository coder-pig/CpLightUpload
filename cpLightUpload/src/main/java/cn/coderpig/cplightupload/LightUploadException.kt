package cn.coderpig.cplightupload

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 自定义异常
 */
class LightUploadException : RuntimeException {
    constructor(detailMessage: String) : super(detailMessage)
    constructor(throwable: Throwable) : super(throwable)
    constructor(detailMessage: String, throwable: Throwable) : super(detailMessage, throwable)
}