package cn.coderpig.cplightupload.interceptor.start

import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.logV

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传完第一个拦截器
 */
class StartDoneInterceptor : Interceptor {
    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Task {
        val task = chain.task()
        "分发执行后续处理...".logV()
        return chain.proceed(task)
    }
}