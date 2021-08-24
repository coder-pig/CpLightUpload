package cn.coderpig.demo

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.Task
import cn.coderpig.cplightupload.upload.Upload
import cn.coderpig.demo.example.picture.CpImageTask
import cn.coderpig.demo.example.video.CpVideoTask
import cn.coderpig.demo.ext.KtCameraExt.Companion.dispatchTakePictureIntent
import cn.coderpig.demo.ext.KtCameraExt.Companion.dispatchTakeVideoIntent
import cn.coderpig.demo.ext.UriBean
import cn.coderpig.demo.ext.getOutputMediaFileUri
import cn.coderpig.demo.ext.shortToast
import com.blankj.utilcode.util.UriUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var uriBean: UriBean
    var list = arrayListOf(1, 2, 3, 4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_take_photo_upload.setOnClickListener {
            uriBean = getOutputMediaFileUri()
            dispatchTakePictureIntent(120, MediaStore.EXTRA_OUTPUT to uriBean.uri)
        }
        bt_take_video_upload.setOnClickListener {
            dispatchTakeVideoIntent(110)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            LightUpload.upload(task = CpImageTask().apply {
                filePath = uriBean.path
                callback = object : Upload.CallBack {
                    override fun onSuccess(task: Task) {
                        lly_root.addView(TextView(this@MainActivity).apply {
                            text = " ${task.response!!.content}\n"
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        })
                        nsv_root.fullScroll(View.FOCUS_DOWN)
                    }

                    override fun onFailure(task: Task) {
                        task.throwable?.message?.let { it1 -> shortToast(it1) }
                    }
                }
            })
        } else if (requestCode == 110) {
            LightUpload.upload(task = CpVideoTask().apply {
                filePath = UriUtils.uri2File(data?.data!!).absolutePath
                callback = object : Upload.CallBack {
                    override fun onSuccess(task: Task) {
                        lly_root.addView(TextView(this@MainActivity).apply {
                            text = " ${task.response!!.content}\n"
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        })
                        nsv_root.fullScroll(View.FOCUS_DOWN)
                    }

                    override fun onFailure(task: Task) {
                        task.throwable?.message?.let { it1 -> shortToast(it1) }
                    }

                }
            })
        }
    }
}