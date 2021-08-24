package cn.coderpig.demo.example.video

import android.media.MediaMetadataRetriever
import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.FileUtils
import cn.coderpig.cplightupload.utils.logV
import cn.coderpig.demo.example.picture.CpImageTask
import cn.coderpig.demo.ext.getExternalVideoPath
import java.io.File

/**
 * Author: zpj
 * Date: 2021-08-24
 * Desc:
 */
class VideoFrameInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        if (task is CpVideoTask) {
            "生成视频缩略图...".logV()
            val tag = task.compressVideoPath!!.substring(task.compressVideoPath!!.lastIndexOf("/")) // 获取第一帧文件名
            val frameFile = File(getExternalVideoPath() + tag + ".jpg")
            task.firstFramePath = frameFile.absolutePath
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(task.compressVideoPath!!)
            val frameBitmap = mmr.frameAtTime
            FileUtils.compressImage(frameBitmap, frameFile, 80)
            task.firstFrameMD5 =  FileUtils.getFileMD5ToString(frameFile)
            LightUpload.upload(task = CpImageTask().apply {
                filePath = task.firstFramePath
                md5 = task.firstFrameMD5
            })
            frameBitmap?.recycle()
        }
        return chain.proceed(task)
    }
}