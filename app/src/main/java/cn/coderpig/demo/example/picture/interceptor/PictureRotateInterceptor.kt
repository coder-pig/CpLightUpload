package cn.coderpig.demo.example.picture.interceptor

import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.FileUtils
import cn.coderpig.cplightupload.utils.logV
import cn.coderpig.demo.example.picture.CpImageTask
import cn.coderpig.demo.example.picture.ImageUploadConfig

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 图片旋转拦截器
 */
class PictureRotateInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        val config = task.config as? ImageUploadConfig
        if (task is CpImageTask) {
            if(task.needRotate == null) task.needRotate = config?.needRotate
            "============ 判断是否需要图片翻转 ============".logV()
            val degree = FileUtils.readPictureDegree(task.filePath!!)
            if (degree != 0) {
                "图片旋转修正".logV()
                FileUtils.rotateToDegrees(task.filePath!!, degree.toFloat())
                "图片旋转处理完毕".logV()
            } else {
                "不需要旋转修正.".logV()
            }
        }
        return chain.proceed(task)
    }
}