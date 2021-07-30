package cn.coderpig.demo.example

import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.task.Task
import cn.coderpig.cplightupload.utils.FileUtils
import cn.coderpig.cplightupload.utils.logV

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 图片旋转拦截器
 */
class PictureRotateInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        "判断是否需要图片翻转 ============".logV()
        val degree = FileUtils.readPictureDegree(task.filePath!!)
        if (degree != 0) {
            FileUtils.rotateToDegrees(task.filePath!!, degree.toFloat())
            "图片旋转修正".logV()
        } else {
            "不需要旋转修正".logV()
        }
        "图片旋转处理完毕".logV()
        return chain.proceed(task)
    }
}