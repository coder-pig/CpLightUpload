package cn.coderpig.cplightupload


import android.os.Looper
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.interceptor.chain.BeforeInterceptorChain
import cn.coderpig.cplightupload.interceptor.chain.DoneInterceptorChain
import cn.coderpig.cplightupload.interceptor.end.EndBeforeInterceptor
import cn.coderpig.cplightupload.interceptor.end.EndDoneInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartBeforeInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartDoneInterceptor
import cn.coderpig.cplightupload.upload.HucUpload
import cn.coderpig.cplightupload.upload.Upload
import cn.coderpig.cplightupload.utils.*
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
    const val LIGHT_UPLOAD = 666  // 消息标记

    // 拦截器(上传前、上传后附加处理)
    private lateinit var beforeInterceptors: List<Interceptor>
    private lateinit var doneInterceptors: List<Interceptor>

    // 上传业务类及上传配置类
    private var mUpload: Map<UploadTaskType, Upload?>? = null
    private var mConfig: Map<UploadTaskType, LightUploadConfig>? = null

    // 子线程回调主线程更新UI
    private var mHandlerPoster: HandlerPoster? = null

    /**
     * 初始化方法
     *
     * 可传入一个自定义Builder对象[builder]，只能调用一次，多次调用会抛异常
     * */
    fun init(builder: LightUploadBuilder?) {
        if (taskQueue == null) {
            (builder ?: LightUploadBuilder()).let {
                executorService = it.executorService
                dispatcher = LightUploadDispatcher(this)
                mHandlerPoster = HandlerPoster(Looper.getMainLooper())
                taskQueue = LinkedList()
                // 添加请求前后拦截器，同时返回一个不可变的列表
                beforeInterceptors = it.beforeInterceptors.apply {
                    add(0, StartBeforeInterceptor())
                    add(EndBeforeInterceptor())
                }.immutableList()
                doneInterceptors = it.doneInterceptors.apply {
                    add(0, StartDoneInterceptor())
                    add(EndDoneInterceptor())
                }.immutableList()
                mUpload = it.uploads
                mConfig = it.configs
            }
        } else {
            throw LightUploadException("只能初始化一次")
        }
    }

    /**
     * 上传任务
     *
     * [filePath] 文件路径
     * [url] 上传地址
     * [callback] 上传结果回调
     * [task] 上传任务
     * */
    fun upload(
        filePath: String? = null,
        url: String? = null,
        callback: Upload.CallBack? = null,
        task: Task? = null
    ) {
        if (task != null) uploadTask(task) else {
            generateTaskByPath(filePath)?.let {
                when (it) {
                    is ImageTask -> uploadTask(ImageTask().apply {
                        this.filePath = filePath
                        this.reqData = ReqData().apply { uploadUrl = url }
                        this.callback = callback
                        this.config = mConfig?.get(UploadTaskType.IMAGE)
                    })
                    is VideoTask -> uploadTask(VideoTask().apply {

                    })
                    is AudioTask -> uploadTask(AudioTask())
                    is FileTask -> uploadTask(FileTask())
                    else -> uploadTask(ElseTask())
                }
            }
        }
    }

    /** 添加上传任务 */
    @Synchronized
    private fun uploadTask(task: Task) {
        dispatcher!!.enqueue(task)
    }

    /** 执行任务 */
    internal fun invokeTask(originTask: Task) {
        "执行上传任务：${originTask.filePath}".logV()
        "============ 准备上传 ============".logV()
        var upload: Upload?
        when (originTask) {
            is ImageTask -> UploadTaskType.IMAGE
            is VideoTask -> UploadTaskType.VIDEO
            is AudioTask -> UploadTaskType.AUDIO
            is FileTask -> UploadTaskType.FILE
            else -> UploadTaskType.ELSE
        }.let {
            upload = mUpload!![it]
            originTask.config = mConfig!![it]
        }
        "执行上传前的准备工作...".logV()
        var finalTask: Task? =
            BeforeInterceptorChain(beforeInterceptors, 0, originTask).proceed(originTask)
        if (finalTask!!.status != UploadTaskStatus.DONE) {
            "初始化上传请求...".logV()
            if (upload == null) upload = HucUpload()
            upload?.let {
                it.initRequest(finalTask!!, object : Upload.CallBack {
                    override fun onSuccess(task: Task) {
                        finalTask!!.response = task.response
                        finalTask = DoneInterceptorChain(doneInterceptors, 0, originTask).proceed(originTask)
                    }

                    override fun onFailure(task: Task) {
                        task.throwable?.message?.logD()
                        // 错误异常处理
                    }
                })
                it.sendRequest()
            }
        } else {
            "任务有传记录，直接结束".logV()
        }
    }

    fun getTaskQueue() = taskQueue

    fun getExecutorService() = executorService

    fun getConfig() = mConfig

    fun postTask(task: Task) {
        mHandlerPoster!!.enqueue(task)
    }

}