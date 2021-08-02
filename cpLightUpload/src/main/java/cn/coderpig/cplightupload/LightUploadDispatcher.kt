package cn.coderpig.cplightupload

import cn.coderpig.cplightupload.task.Task

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 任务调度器
 */
class LightUploadDispatcher(private var kit: LightUpload) : Runnable {

    fun enqueue(task: Task) {
        kit.getExecutorService()!!.execute(this)
    }

    @Synchronized
    override fun run() {
        if(kit.getTaskQueue()?.size!! > 0) {
            kit.invokeTask(kit.getTaskQueue()!!.first)
            kit.getTaskQueue()!!.removeFirst()
        }
    }
}