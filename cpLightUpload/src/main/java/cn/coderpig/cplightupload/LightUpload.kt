package cn.coderpig.cplightupload


import cn.coderpig.cplightupload.entity.ReqData
import cn.coderpig.cplightupload.entity.TaskStatus
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.interceptor.chain.BeforeInterceptorChain
import cn.coderpig.cplightupload.interceptor.chain.DoneInterceptorChain
import cn.coderpig.cplightupload.interceptor.end.EndBeforeInterceptor
import cn.coderpig.cplightupload.interceptor.end.EndDoneInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartBeforeInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartDoneInterceptor
import cn.coderpig.cplightupload.task.ImageTask
import cn.coderpig.cplightupload.task.Task
import cn.coderpig.cplightupload.upload.HucUpload
import cn.coderpig.cplightupload.upload.Upload
import cn.coderpig.cplightupload.utils.logD
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

    // 拦截器(上传前、上传后附加处理)
    private lateinit var beforeInterceptors: MutableList<Interceptor>
    private lateinit var doneInterceptors: MutableList<Interceptor>

    // 自定义上传业务类
    private var mUpload: Upload ?= null
    private var mConfig: LightUploadConfig? = null

    /** 定义一个初始化方法，可传入一个自定义Builder */
    fun init(builder: LightUploadBuilder?) {
        (builder ?: LightUploadBuilder()).let {
            executorService = it.executorService
            taskQueue = LinkedList()
            beforeInterceptors = it.beforeInterceptors
            doneInterceptors = it.doneInterceptors
            dispatcher = LightUploadDispatcher(this)
            mUpload = if(it.upload != null) it.upload else HucUpload()
            mConfig = if(it.config != null) it.config else LightUploadConfig()
        }
    }

    /** 上传图片 */
    fun uploadImage(filePath: String, md5: String? = null, fileName: String? = null,
                    fileType: String? = null, fileUrl: String? = null,
                    reqData: ReqData? = ReqData(), status: TaskStatus? = TaskStatus.BEFORE,
                    needCompress: Boolean? = false, compressPercent: Int? = 30,
                    callback: Upload.CallBack? = null) {
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
            it.callback = callback
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
        "执行上传前的准备工作...".logV()
        var finalTask: Task? = BeforeInterceptorChain(beforeInterceptors.apply {
            add(0, StartBeforeInterceptor())
            add(EndBeforeInterceptor())
        }, 0, originTask).proceed(originTask)
        "============ 准备上传 ============".logV()
        mUpload?.initRequest(finalTask!!, object: Upload.CallBack {
            override fun onSuccess(task: Task) {
                finalTask!!.response = task.response
                finalTask = DoneInterceptorChain(doneInterceptors.apply {
                    add(0, StartDoneInterceptor())
                    add(EndDoneInterceptor())
                }, 0, originTask).proceed(originTask)
            }

            override fun onFailure(task: Task, throwable: Throwable) {
                throwable.message?.logD()
                // 错误异常处理
            }

        })
        mUpload?.sendRequest()
    }

    fun getTaskQueue() = taskQueue

    fun getExecutorService() = executorService

    fun getConfig() = mConfig
}