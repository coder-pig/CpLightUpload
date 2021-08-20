package cn.coderpig.cplightupload

import cn.coderpig.cplightupload.entity.ReqData

/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc: 上传配置类
 */
open class LightUploadConfig {
    var enableQuickUpload: Boolean = false  // 是否启用快传
    var reqData: ReqData? = null    // 默认参数
}