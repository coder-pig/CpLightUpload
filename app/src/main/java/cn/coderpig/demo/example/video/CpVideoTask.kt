package cn.coderpig.demo.example.video

import cn.coderpig.cplightupload.VideoTask

/**
 * Author: zpj
 * Date: 2021-08-24
 * Desc: 自定义视频任务
 */
class CpVideoTask : VideoTask() {
    var limitSize: Int? = -1    // 视频限制大小
    var compressVideoPath: String? = null   // 压缩视频路径
    var compressVideoMD5: String? = null   // 压缩视频MD5
    var firstFramePath: String? = null   // 视频第一帧路径
    var firstFrameMD5: String? = null    // 视频第一帧MD5
}