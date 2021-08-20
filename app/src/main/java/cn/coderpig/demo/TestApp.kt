package cn.coderpig.demo

import android.app.Application
import android.content.Context
import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.LightUploadBuilder
import cn.coderpig.cplightupload.LightUploadTask
import cn.coderpig.cplightupload.entity.ReqData
import cn.coderpig.cplightupload.upload.HucUpload
import cn.coderpig.demo.example.picture.*
import cn.coderpig.demo.example.video.VideoCompressInterceptor
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
        LightUpload.init(
            this, LightUploadBuilder()
                .config(LightUploadTask.IMAGE to ImageUploadConfig().apply {
                    reqData = ReqData(
                        uploadUrl = "http://127.0.0.1:5000/upload_pic",
                        requestMethod = "POST",
                        headers = hashMapOf(
                            "Charset" to "utf-8",
                            "connection" to "keep-alive"
                        )
                    )
                }, LightUploadTask.VIDEO to VideoUploadConfig().apply {
                    reqData = ReqData(
                        uploadUrl = "http://127.0.0.1:5000/upload_video",
                        requestMethod = "POST",
                        headers = hashMapOf(
                            "Charset" to "utf-8",
                            "connection" to "keep-alive"
                        )
                    )
                })
                .upload(LightUploadTask.IMAGE to HucUpload())
                .addBeforeInterceptor(PictureRotateInterceptor())
                .addBeforeInterceptor(PictureCompressInterceptor())
                .addBeforeInterceptor(VideoCompressInterceptor())
                .addDoneInterceptors(HucParsingInterceptor())
        )
    }
}