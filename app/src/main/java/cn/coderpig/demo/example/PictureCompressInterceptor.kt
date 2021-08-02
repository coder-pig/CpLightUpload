package cn.coderpig.demo.example

import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.task.ImageTask
import cn.coderpig.cplightupload.task.Task
import cn.coderpig.cplightupload.utils.FileUtils
import cn.coderpig.cplightupload.utils.logV
import java.io.File

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 图片压缩拦截器
 */
class PictureCompressInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        "============ 校验是否需要进行图片压缩，及压缩比例 ============".logV()
        (task as ImageTask).let {
            if(it.needCompress!!) {
                "执行压缩操作，压缩比例为${it.compressPercent}%".logV()
                val afterPath = task.filePath!!.replace(".${task.fileType!!}", "") + "_compress." + task.fileType!!
                val afterFile = File(afterPath)
                FileUtils.compressImage(FileUtils.getLocalImage(File(task.filePath!!)), afterFile, it.compressPercent!!)
                task.md5 = FileUtils.getFileMD5ToString(afterFile)
                task.filePath = afterPath
                task.fileName = afterFile.name
                "图片压缩处理完毕，新的图片路径与MD5 → ${afterFile.name} → ${task.md5}".logV()
            } else {
                "图片不需要压缩...".logV()
            }
        }
        return chain.proceed(task)
    }
}