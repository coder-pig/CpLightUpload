package cn.coderpig.cplightupload.utils

import android.os.Handler
import android.os.Looper
import android.os.Message
import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.TaskStatus
import java.util.*

/**
 * Author: zpj
 * Date: 2021-08-02
 * Desc: 主线程分发信息用(子线程不能直接更新UI)
 */
class HandlerPoster(looper: Looper) : Handler(looper) {
    private val mLooper = looper
    private val mQueue: LinkedList<Task> = LinkedList()

    fun enqueue(task: Task) {
        // 如果是主线程直接执行不用走Handler
        if(isMainThread()) {
            poll()?.let { call(it) }
        } else {
            mQueue.add(task)
            sendEmptyMessage(LightUpload.LIGHT_UPLOAD)
        }
    }

    private fun poll() = mQueue.pollFirst()

    override fun handleMessage(msg: Message) {
        if(msg.what == LightUpload.LIGHT_UPLOAD) {
            poll()?.let { call(it) }
        } else {
            super.handleMessage(msg)
        }
    }

    private fun call(task: Task?) {
        task?.let {
            if(it.callback != null) {
                if(it.status == TaskStatus.FAILURE) it.callback!!.onFailure(task)
                else if(it.status == TaskStatus.DONE) it.callback!!.onSuccess(task)
            }
        }
    }

    // 判断当前线程是否为主线程
    fun isMainThread() = mLooper == Looper.myLooper()
}