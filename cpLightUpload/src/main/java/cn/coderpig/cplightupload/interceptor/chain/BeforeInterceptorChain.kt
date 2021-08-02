package cn.coderpig.cplightupload.interceptor.chain

import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.task.Task

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传前第一个Chain
 */
class BeforeInterceptorChain(private var interceptors: List<Interceptor>,
                             private var index: Int,
                             private var task: Task
) : Interceptor.Chain {

    override fun task() = this.task

    @Synchronized
    override fun proceed(task: Task): Task {
        if (index >= interceptors.size) throw AssertionError()
        val next = BeforeInterceptorChain(interceptors, index + 1, task)
        val interceptor = interceptors[index]
        return interceptor.intercept(next)
    }
}
