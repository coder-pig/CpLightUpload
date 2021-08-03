package cn.coderpig.cplightupload

/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc: 上传配置类
 */
open class LightUploadConfig {
    var enableQuickUpload: Boolean = true  // 是否启用快传
    var baseUrl: String? = null // 默认后台地址
    var uploadServerUrl: String? = null // 默认文件上传地址
}