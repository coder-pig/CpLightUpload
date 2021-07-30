package cn.coderpig.demo

import cn.coderpig.demo.task.Task

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 任务调度器
 */
class LightUploadDispatcher(private var kit: LightUpload) : Runnable {

    fun enqueue(task: Task) {
        kit.getExecutorService()!!.execute(this)
    }


    override fun run() {
        kit.invokeTask(kit.getTaskQueue()!!.first)
        kit.getTaskQueue()!!.removeFirst()
    }
}