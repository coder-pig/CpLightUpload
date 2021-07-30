package cn.coderpig.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import cn.coderpig.demo.entity.ReqData
import cn.coderpig.demo.ext.KtCameraExt.Companion.dispatchTakePictureIntent
import cn.coderpig.demo.ext.UriBean
import cn.coderpig.demo.ext.getOutputMediaFileUri
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) {
            LightUpload.uploadImage(uriBean.path, reqData = ReqData(uploadUrl = "http://uat.zhaoshang800.com//broker/image/uploadNotZip"))
        }
    }

}