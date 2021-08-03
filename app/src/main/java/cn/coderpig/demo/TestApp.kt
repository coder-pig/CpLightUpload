package cn.coderpig.demo

import android.app.Application
import android.content.Context
import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.LightUploadBuilder
import cn.coderpig.cplightupload.LightUploadTask
import cn.coderpig.demo.example.picture.*
import cn.coderpig.demo.example.video.VideoCompressInterceptor
import cn.coderpig.demo.ext.PartnerKotlinExtKit

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
        PartnerKotlinExtKit.init(context!!)
        LightUpload.init(
            this, LightUploadBuilder()
                .config("image" to ImageUploadConfig()
                    .apply {
                        uploadServerUrl = "http://uat.zhaoshang800.com/broker/image/uploadNotZip"
                    })
                .upload(LightUploadTask.IMAGE to HucUpload())
                .addBeforeInterceptor(PictureRotateInterceptor())
                .addBeforeInterceptor(PictureCompressInterceptor())
                .addBeforeInterceptor(VideoCompressInterceptor())
                .addDoneInterceptors(HucParsingInterceptor())
        )
    }
}