package cn.coderpig.cplightupload.interceptor.start

import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.task.Task
import cn.coderpig.cplightupload.utils.logV

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 准备上传第一个拦截器，可在此对参数进行整合
 */
class StartReadyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        "参数整合...".logV()
        return chain.proceed(task)
    }
}