package cn.coderpig.cplightupload

import cn.coderpig.cplightupload.utils.logV

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 任务调度器
 */
class LightUploadDispatcher(upload: LightUpload) : Runnable {
    private val mUpload: LightUpload = upload
    private val mQueue: LightUploadQueue = LightUploadQueue()

    fun enqueue(task: Task) {
        mQueue.enqueue(task)
        mUpload.getExecutorService()!!.execute(this)
    }

    override fun run() {
        Thread.currentThread().name.logV()
        val task = mQueue.poll() ?: throw IllegalStateException("没有待处理的任务")
        mUpload.invokeTask(task)
    }
}