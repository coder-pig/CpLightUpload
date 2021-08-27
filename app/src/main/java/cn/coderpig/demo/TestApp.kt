package cn.coderpig.demo

import android.app.Application
import android.content.Context
import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.LightUploadBuilder
import cn.coderpig.cplightupload.ReqData
import cn.coderpig.cplightupload.UploadTaskType
import cn.coderpig.cplightupload.upload.HucUpload
import cn.coderpig.demo.example.picture.ImageUploadConfig
import cn.coderpig.demo.example.picture.interceptor.PictureCompressInterceptor
import cn.coderpig.demo.example.picture.interceptor.PictureRotateInterceptor
import cn.coderpig.demo.example.picture.interceptor.SimpleParsingInterceptor
import cn.coderpig.demo.example.video.VideoCompressInterceptor
import cn.coderpig.demo.example.video.VideoFrameInterceptor
import cn.coderpig.demo.example.video.VideoUploadConfig
import cn.coderpig.demo.ext.KotlinExtKit

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc:
 */
class TestApp : Application() {
    companion object {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext
        KotlinExtKit.init(context!!)
        LightUpload.init(LightUploadBuilder()
                .config(UploadTaskType.IMAGE to ImageUploadConfig().apply {
                    reqData = ReqData(
                        uploadUrl = "http://127.0.0.1:5000/upload",
                        requestMethod = "POST",
                        reqHeaders = hashMapOf(
                            "Charset" to "utf-8",
                            "connection" to "keep-alive"
                        )
                    )
                }, UploadTaskType.VIDEO to VideoUploadConfig()
                    .apply {
                    reqData = ReqData(
                        uploadUrl = "http://127.0.0.1:5000/upload",

                        requestMethod = "POST",
                        reqHeaders = hashMapOf(
                            "Charset" to "utf-8",
                            "connection" to "keep-alive"
                        )
                    )
                })
                .upload(UploadTaskType.IMAGE to HucUpload())
                .addBeforeInterceptor(PictureRotateInterceptor())
                .addBeforeInterceptor(PictureCompressInterceptor())
                .addBeforeInterceptor(VideoCompressInterceptor())
                .addBeforeInterceptor(VideoFrameInterceptor())
                .addDoneInterceptors(SimpleParsingInterceptor())
        )
    }
}