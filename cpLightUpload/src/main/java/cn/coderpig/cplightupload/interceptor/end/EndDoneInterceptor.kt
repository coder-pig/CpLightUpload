package cn.coderpig.cplightupload.interceptor.end

import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.task.Task
import cn.coderpig.cplightupload.utils.logV

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传完成最后一个拦截器
 */
class EndDoneInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Task {
        "上传前的准备工作已完成.".logV()
        return chain.task()
    }
}