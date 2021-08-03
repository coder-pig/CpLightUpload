package cn.coderpig.cplightupload


import android.content.Context
import android.os.Looper
import cn.coderpig.cplightupload.entity.ReqData
import cn.coderpig.cplightupload.interceptor.Interceptor
import cn.coderpig.cplightupload.interceptor.chain.BeforeInterceptorChain
import cn.coderpig.cplightupload.interceptor.chain.DoneInterceptorChain
import cn.coderpig.cplightupload.interceptor.end.EndBeforeInterceptor
import cn.coderpig.cplightupload.interceptor.end.EndDoneInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartBeforeInterceptor
import cn.coderpig.cplightupload.interceptor.start.StartDoneInterceptor
import cn.coderpig.cplightupload.upload.Upload
import cn.coderpig.cplightupload.utils.HandlerPoster
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
    const val LIGHT_UPLOAD = 666  // 消息标记

    // 拦截器(上传前、上传后附加处理)
    private lateinit var beforeInterceptors: MutableList<Interceptor>
    private lateinit var doneInterceptors: MutableList<Interceptor>

    // 上传业务类及上传配置类
    private var mUpload: Map<LightUploadTask, Upload?>? = null
    private var mConfig: Map<LightUploadTask, LightUploadConfig>? = null

    // 子线程回调主线程更新UI用
    private var mHandlerPoster: HandlerPoster? = null
    private var mContext: Context? = null


    /** 定义一个初始化方法，可传入一个自定义Builder */
    fun init(context: Context?, builder: LightUploadBuilder?) {
        mContext = context
        (builder ?: LightUploadBuilder()).let {
            executorService = it.executorService
            taskQueue = LinkedList()
            // 添加请求前的拦截器
            beforeInterceptors = it.beforeInterceptors.apply {
                add(0, StartBeforeInterceptor())
                add(EndBeforeInterceptor())
            }
            // 添加请求后的拦截器
            doneInterceptors = it.doneInterceptors.apply {
                add(0, StartDoneInterceptor())
                add(EndDoneInterceptor())
            }
            dispatcher = LightUploadDispatcher(this)
            mUpload = if (it.uploads != null) it.uploads else hashMapOf(LightUploadTask.ELSE to null)
            mConfig = if (it.configs != null) it.configs else  hashMapOf(LightUploadTask.ELSE to LightUploadConfig())
            mHandlerPoster = HandlerPoster(Looper.getMainLooper())
        }
    }

    /** 上传图片 */
    fun uploadImage(
        filePath: String, md5: String? = null, fileName: String? = null,
        fileType: String? = null, fileUrl: String? = null,
        reqData: ReqData? = ReqData(), status: TaskStatus? = TaskStatus.BEFORE,
        needCompress: Boolean? = false, compressPercent: Int? = 30,
        callback: Upload.CallBack? = null
    ) {
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

    /** 上传视频 */
    fun uploadVideo(filePath: String) {
        uploadTask(VideoTask().also {
            it.filePath = filePath
        })
    }

    /** 添加上传任务 */
    @Synchronized
    private fun uploadTask(task: Task) {
        dispatcher!!.enqueue(task)
    }

    /** 执行任务 */
    @Synchronized
    fun invokeTask(originTask: Task) {
        "执行上传任务：${originTask.filePath}".logV()
        "============ 准备上传 ============".logV()
        var upload: Upload?
        when(originTask) {
            is ImageTask -> LightUploadTask.IMAGE
            is VideoTask -> LightUploadTask.VIDEO
            is AudioTask -> LightUploadTask.AUDIO
            is FileTask -> LightUploadTask.FILE
            else -> LightUploadTask.ELSE
        }.let {
            upload = mUpload!![it]
            originTask.config = mConfig!![it]
        }
        "执行上传前的准备工作...".logV()
        var finalTask: Task? = BeforeInterceptorChain(beforeInterceptors, 0, originTask).proceed(originTask)
        if(finalTask!!.status != TaskStatus.DONE) {
            "初始化上传请求...".logV()
            upload?.initRequest(finalTask, object : Upload.CallBack {
                override fun onSuccess(task: Task) {
                    finalTask!!.response = task.response
                    finalTask = DoneInterceptorChain(doneInterceptors, 0, originTask).proceed(originTask)
                }

                override fun onFailure(task: Task) {
                    task.throwable?.message?.logD()
                    // 错误异常处理
                }
            })
            upload?.sendRequest()
        } else {
            "任务有传记录，直接结束".logV()
        }
    }

    fun getContext() = mContext

    fun getTaskQueue() = taskQueue

    fun getExecutorService() = executorService

    fun getConfig() = mConfig

    fun postTask(task: Task) {
        mHandlerPoster!!.enqueue(task)
    }

}