package cn.coderpig.cplightupload

import java.util.*

/**
 * Author: zpj
 * Date: 2021-08-03
 * Desc: 自定义任务队列(进出队列操作加锁)
 */
class LightUploadQueue {
    private val mQueue = LinkedList<Task>()

    @Synchronized
    fun enqueue(task: Task?) {
        if (task == null) throw NullPointerException("空任务不能入列")
        mQueue.add(task)
    }

    @Synchronized
    fun poll() = if (mQueue.size == 0) null else mQueue.pollFirst()
}