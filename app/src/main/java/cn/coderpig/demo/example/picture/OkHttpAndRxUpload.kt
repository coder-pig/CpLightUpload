package cn.coderpig.demo.example.picture

/**
 * Author: zpj
 * Date: 2021-08-03
 * Desc: OkHttp3 和 RxJava 上传
 */
//class OkHttpAndRxUpload : Upload() {
//    private var mVersion: String = "5.9.1"
//    private var mVersionName: String = "5.9.1"
//    private var width: Int = 1080
//    private var height: Int = 2116
//    private var scale: Float = 2.75f
//    private var mUrl: String? = null
//    private var mTask: Task? = null
//
//    private val mClient: OkHttpClient by lazy { newClient() }
//    private val mCustomerInterceptor: Interceptor by lazy { initCustomInterceptor() }
//    private val apiService by lazy {
//        create("http://uat.zhaoshang800.com/", UploadApiService::class.java)
//    }
//
//    private fun newClient() = OkHttpClient.Builder().apply {
//        readTimeout(100, TimeUnit.SECONDS)   //读取超时
//        connectTimeout(100, TimeUnit.SECONDS)    //连接超时
//        writeTimeout(100, TimeUnit.SECONDS)  //写入超时
//        addInterceptor(mCustomerInterceptor)
//    }.build()
//
//    private fun initCustomInterceptor() = Interceptor { chain ->
//        chain.proceed(chain.request().newBuilder().apply {
//            addHeader("Charset", "utf-8")
//            addHeader("connection", "keep-alive")
//            addHeader("token", "6464637B676E66656764676E6663676366616465606E667B6F626361646467")
//            addHeader("Content-Type", "multipart/form-data;boundary=${UUID.randomUUID()}")
//            addHeader("version", mVersion)
//            addHeader("User-Agent", "partner/${mVersionName}" +
//                    "(Android;${Build.VERSION.RELEASE};${width}*${height};" +
//                    "Scale=${scale};${Build.BRAND}=${Build.MODEL})")
//        }.build())
//    }
//
//    private fun <T> create(baseUrl: String, clazz: Class<T>): T = Retrofit.Builder().apply {
//        baseUrl(baseUrl)
//        client(mClient)
//        addConverterFactory(GsonConverterFactory.create())
//        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//    }.build().create(clazz)
//
//    override fun initRequest(task: Task, callback: CallBack?) {
//        super.initRequest(task, callback)
//        task.let {
//            "上传参数初始化...".logV()
//            super.initRequest(task, callback)
//            mTask = task
//            task.reqData?.let {
//                mUrl = if (it.uploadUrl == null) LightUpload.getConfig()?.get(LightUploadTask.IMAGE)?.uploadServerUrl else it.uploadUrl
//            }
//        }
//    }
//
//    override fun sendRequest() {
//        val file = File(mTask!!.filePath)
//        val fileName = file.name
//        val fileType = if (fileName.indexOf(".") != -1) fileName.substring(fileName.lastIndexOf(".")) else ".jpg"
//        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//        val body = MultipartBody.Part.createFormData("file", "${mTask!!.md5}${fileType}", requestFile)
//        val dispose = apiService.uploadFile(mUrl!!, body)
//            .subscribeOn(Schedulers.io())
//            .subscribe({
//                "文件上传结束...".logV()
//                mTask!!.response = cn.coderpig.cplightupload.upload.Response(
//                    it.code(), it.body().toString()
//                )
//                mTask!!.status = TaskStatus.DONE
//                mCallback?.onSuccess(mTask!!)
//            }, {
//                mTask!!.status = TaskStatus.FAILURE
//                mTask!!.throwable = it
//                mCallback?.onFailure(mTask!!)
//                LightUpload.postTask(mTask!!)
//            })
//    }
//
//    interface UploadApiService {
//        @Multipart
//        @POST
//        fun uploadFile(
//            @Url url: String,
//            @Part file: MultipartBody.Part
//        ): Flowable<Response<Bean<HashMap<String, String>>>>
//    }
//
//    data class Bean<T: Serializable>(
//        var code: Int,
//        var msg: String,
//        var data: T
//    ): Serializable
//
//}