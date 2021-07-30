package cn.coderpig.demo.example

import cn.coderpig.demo.interceptor.Interceptor
import cn.coderpig.demo.task.Task
import cn.coderpig.demo.utils.FileUtils
import cn.coderpig.demo.utils.logV
import java.io.File

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 图片压缩拦截器
 */
class PictureCompressInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        "校验是否需要进行图片压缩，及压缩比例 ============".logV()
        "执行压缩操作...".logV()
        val afterPath = task.filePath!!.replace(".${task.fileType!!}", "") + "_compress." + task.fileType!!
        val afterFile = File(afterPath)
        FileUtils.compressImage(FileUtils.getLocalImage(File(task.filePath!!)), afterFile, 80)
        task.md5 = FileUtils.getFileMD5ToString(afterFile)
        task.filePath = afterPath
        task.fileName = afterFile.name
        "图片压缩处理完毕，新的图片路径与MD5 → ${afterFile.name} → ${task.md5}".logV()
        return chain.proceed(task)
    }
}