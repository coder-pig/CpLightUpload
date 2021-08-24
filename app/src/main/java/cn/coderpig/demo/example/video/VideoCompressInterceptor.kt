package cn.coderpig.demo.example.video

import android.media.MediaMetadataRetriever
import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.FileUtils
import cn.coderpig.cplightupload.utils.logV
import cn.coderpig.demo.TestApp
import cn.coderpig.demo.ext.getExternalVideoPath
import com.iceteck.silicompressorr.SiliCompressor
import java.io.File
import kotlin.math.max

/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc: 视频压缩拦截器
 */
class VideoCompressInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        // 压缩视频，先获取视频宽高
        val task = chain.task()
        if(task is CpVideoTask) {
            "压缩视频：${task.filePath}".logV()
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(task.filePath)
            var width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
            var height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
            var big = -1
            if(width != null && height != null) {
                big = max(width, height)
                while (big > 1080) {
                    width /= 2
                    height /= 2
                    big /= 2
                }
                if(big != -1) {
                    // 设置压缩参数，开始压缩
                    task.compressVideoPath = SiliCompressor.with(TestApp.context).compressVideo(task.filePath, getExternalVideoPath(), height, width, 450000)
                    val compressVideoFile = File(task.compressVideoPath!!)
                    task.compressVideoMD5 = FileUtils.getFileMD5ToString(compressVideoFile)
                }
                "压缩后的视频路径及新的MD5 → ${task.compressVideoPath} → ${task.compressVideoMD5}".logV()
            }
            retriever.release()
        }
        return chain.proceed(task)
    }
}