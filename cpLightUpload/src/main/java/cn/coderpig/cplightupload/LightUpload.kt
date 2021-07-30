package cn.coderpig.cplightupload

import cn.coderpig.cplightupload.entity.ReqData
import cn.coderpig.cplightupload.entity.TaskStatus
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.interceptor.chain.BeforeInterceptorChain
import cn.coderpig.cplightupload.interceptor.chain.DoneInterceptorChain
import cn.coderpig.cplightupload.interceptor.chain.ReadyInterceptorChain
import cn.coderpig.cplightupload.interceptor.end.EndBeforeInterceptor
import cn.coderpig.cplightupload.interceptor.end.EndDoneInterceptor
import cn.coderpig.cplightupload.interceptor.end.EndReadyInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartBeforeInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartDoneInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartReadyInterceptor
import cn.coderpig.cplightupload.task.ImageTask
import cn.coderpig.cplightupload.task.Task
import cn.coderpig.cplightupload.utils.logV
import java.util.*
import java.util.concurrent.ExecutorService

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 上传单例
 */
object LightUpload {
    private var executorService: ExecutorService? = null    // 线程池
    private var taskQueue: LinkedList<Task>? = null // 任务队列
    private var dispatcher: LightUploadDispatcher? = null   // 调度器

    // 拦截器(上传前、准备上传、上传后)
    private lateinit var beforeInterceptors: MutableList<Interceptor>
    private lateinit var readyInterceptors: MutableList<Interceptor>
    private lateinit var doneInterceptors: MutableList<Interceptor>

    /** 定义一个初始化方法，可传入一个自定义Builder */
    fun init(builder: LightUploadBuilder?) {
        (builder ?: LightUploadBuilder()).let {
            executorService = it.executorService
            taskQueue = LinkedList()
            beforeInterceptors = it.beforeInterceptors
            readyInterceptors = it.readyInterceptors
            doneInterceptors = it.doneInterceptors
            dispatcher = LightUploadDispatcher(this)
        }
    }

    /** 上传图片 */
    fun uploadImage(filePath: String, md5: String? = null, fileName: String? = null,
                    fileType: String? = null, fileUrl: String? = null,
                    reqData: ReqData? = null, status: TaskStatus? = TaskStatus.BEFORE,
                    needCompress: Boolean? = false, compressPercent: Int? = 30) {
        uploadTask(ImageTask().also {
            it.filePath = filePath
            it.md5 = md5
            it.fileName = fileName
            it.fileType = fileType
            it.fileUrl = fileUrl
            it.reqData = reqData
            it.status = status
            it.needCompress = needCompress
            it.compressPercent = compressPercent
        })
    }

    /** 添加上传任务 */
    private fun uploadTask(task: Task) {
        taskQueue!!.add(task)
        dispatcher!!.enqueue(task)
    }

    /** 执行任务 */
    fun invokeTask(originTask: Task) {
        "执行上传任务：${originTask.filePath}".logV()
        "上传前...".logV()
        var afterTask: Task? = BeforeInterceptorChain(beforeInterceptors.apply {
            add(0, StartBeforeInterceptor())
            add(EndBeforeInterceptor())
        }, 0, originTask).proceed(originTask)
        "准备上传...".logV()
        afterTask = ReadyInterceptorChain(readyInterceptors.apply {
            add(0, StartReadyInterceptor())
            add(EndReadyInterceptor())
        }, 0, originTask).proceed(originTask)
        afterTask = DoneInterceptorChain(doneInterceptors.apply {
            add(0, StartDoneInterceptor())
            add(EndDoneInterceptor())
        }, 0, originTask).proceed(originTask)
    }

    fun getTaskQueue() = taskQueue

    fun getExecutorService() = executorService
}