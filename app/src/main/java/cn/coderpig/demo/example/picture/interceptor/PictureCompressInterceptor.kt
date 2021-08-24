package cn.coderpig.demo.example.picture.interceptor

import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.FileUtils
import cn.coderpig.cplightupload.utils.logV
import cn.coderpig.demo.example.picture.CpImageTask
import cn.coderpig.demo.example.picture.ImageUploadConfig
import java.io.File

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 图片压缩拦截器
 */
class PictureCompressInterceptor : Interceptor {
    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        val config = task.config as? ImageUploadConfig
        if(task is CpImageTask) {
            if(task.needCompress == null) {
                task.needCompress = config?.needCompress
                task.compressPercent = config?.compressPercent
            }
            "============ 校验是否需要进行图片压缩，及压缩比例 ============".logV()
            task.needCompress?.let {
                if(it) {
                    "执行压缩操作，压缩比例为${task.compressPercent}%".logV()
                    val afterPath = task.filePath!!.replace(".${task.fileType!!}", "") + "_compress_${task.compressPercent}." + task.fileType!!
                    val afterFile = File(afterPath)
                    FileUtils.compressImage(FileUtils.getLocalImage(File(task.filePath!!)), afterFile, task.compressPercent!!)
                    task.md5 = FileUtils.getFileMD5ToString(afterFile)
                    task.filePath = afterPath
                    task.fileName = afterFile.name
                    "图片压缩处理完毕，新的图片路径与MD5 → ${afterFile.name} → ${task.md5}".logV()
                } else {
                    "图片不需要压缩...".logV()
                }
            }
        }
        return chain.proceed(task)
    }
}