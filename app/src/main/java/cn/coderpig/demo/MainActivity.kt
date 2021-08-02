package cn.coderpig.demo

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.coderpig.cplightupload.LightUpload
import cn.coderpig.cplightupload.task.Task
import cn.coderpig.cplightupload.upload.Upload
import cn.coderpig.demo.ext.KtCameraExt.Companion.dispatchTakePictureIntent
import cn.coderpig.demo.ext.KtCameraExt.Companion.dispatchTakeVideoIntent
import cn.coderpig.demo.ext.UriBean
import cn.coderpig.demo.ext.getOutputMediaFileUri
import com.blankj.utilcode.util.UriUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var uriBean : UriBean

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
            repeat(10) {
                LightUpload.uploadImage(uriBean.path, needCompress = true, compressPercent = it * 10, callback = object : Upload.CallBack {
                    override fun onSuccess(task: Task) {
                        lly_root.addView(TextView(this@MainActivity).apply {
                            text = " ${task.response!!.content}\n"
                            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                        })
                        nsv_root.fullScroll(View.FOCUS_DOWN)
                    }

                    override fun onFailure(task: Task) {
                    }
                })
            }
        } else if(requestCode == 110) {
            LightUpload.uploadVideo(UriUtils.uri2File(data?.data!!).absolutePath)
        }
    }

}