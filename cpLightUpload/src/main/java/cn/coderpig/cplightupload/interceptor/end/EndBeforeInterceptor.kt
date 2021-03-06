package cn.coderpig.cplightupload.interceptor.end

import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.utils.logV

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传前最后一个拦截器
 */
class EndBeforeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        "上传前的准备工作完成.".logV()
        return chain.task()
    }
}