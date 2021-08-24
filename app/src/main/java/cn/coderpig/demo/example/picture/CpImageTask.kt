package cn.coderpig.demo.example.picture

import cn.coderpig.cplightupload.ImageTask

/**
 * Author: zpj
 * Date: 2021-08-24
 * Desc:
 */
class CpImageTask : ImageTask() {
    var needRotate: Boolean? = null
    var needCompress: Boolean? = null
    var compressPercent: Int? = 80
}