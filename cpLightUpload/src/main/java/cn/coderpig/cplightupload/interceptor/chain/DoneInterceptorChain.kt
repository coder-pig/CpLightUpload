package cn.coderpig.cplightupload.interceptor.chain

import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.task.Task

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传完成后的第一个Chain
 */
class DoneInterceptorChain(private var interceptors: List<Interceptor>,
                           private var index: Int,
                           private var task: Task
) : Interceptor.Chain {

    override fun task() = this.task

    @Synchronized
    override fun proceed(task: Task): Task {
        if (index >= interceptors.size) throw AssertionError()
        val next = DoneInterceptorChain(interceptors, index + 1, task)
        val interceptor = interceptors[index]
        return interceptor.intercept(next)
    }
}
