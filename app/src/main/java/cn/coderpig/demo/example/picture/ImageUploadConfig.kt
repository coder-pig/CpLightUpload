package cn.coderpig.demo.example.picture

import cn.coderpig.cplightupload.LightUploadConfig


/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc: 上传配置类
 */
class ImageUploadConfig : LightUploadConfig() {
    var needRotate: Boolean = true  // 是否需要旋转纠正
    var needCompress: Boolean = true   // 是否需要压缩
    var compressPercent: Int = 80   // 压缩比例，默认80
}