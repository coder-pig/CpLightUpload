package cn.coderpig.cplightupload.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.entity.TaskStatus
import cn.coderpig.cplightupload.task.Task
import java.util.*

/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc:
 */
class MainPoster(looper: Looper) : Handler(looper) {
    private val queue: LinkedList<Task> = LinkedList()

    fun enqueue(task: Task) {
        queue.add(task)
        sendEmptyMessage(LightUpload.LIGHT_UPLOAD)
    }

    private fun poll(): Task? {
        if (queue.isNotEmpty()) {
            return queue.removeFirst()
        }
        return null
    }

    override fun handleMessage(msg: Message) {
        if(msg.what == LightUpload.LIGHT_UPLOAD) {
            val task = poll()
            task?.let {
                if(it.callback != null) {
                    if(it.status == TaskStatus.FAILURE) it.callback!!.onFailure(task)
                    else if(it.status == TaskStatus.DONE) it.callback!!.onSuccess(task)
                }
            }
        } else {
            super.handleMessage(msg)
        }
    }

}