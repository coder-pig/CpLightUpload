package cn.coderpig.cplightupload.task

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 图片类型的上传任务
 */
class ImageTask : Task() {
    var needCompress: Boolean? = false  // 图片是否需要压缩
    var compressPercent: Int? = 30  // 图片的压缩比例
}