package cn.coderpig.cplightupload.task

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 视频类型的上传任务
 */
class VideoTask : Task() {
    var compressVideoPath: String? = null   // 压缩视频路径
    var compressVideoMD5: String? = null   // 压缩视频MD5
}