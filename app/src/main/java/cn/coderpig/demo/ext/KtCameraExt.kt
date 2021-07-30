package cn.coderpig.demo.ext

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.coderpig.demo.R

/**
 * Author: zpj
 * Date: 2021-07-30
 * Desc: 原生相机相关扩展
 */
open class KtCameraExt {
    companion object {
        const val REQUEST_VIDEO_CAPTURE = 10001
        const val REQUEST_PICTURE_CAPTURE = 20001

        /**
         * 唤醒原生相机录制
         * [extras] 附加数据列表，可选
         * */
        fun Activity.dispatchTakeVideoIntent(requestCode: Int?, vararg extras: Pair<String, Any>) {
            isSdcardExits() falseLet { shortToast(getTextRes(
                R.string.take_video_no_sd_hint)) } elseLet {
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) trueLet {
                    try {
                        val paramsMap = HashMap<String, Pair<String, Any>>()
                        paramsMap[MediaStore.EXTRA_VIDEO_QUALITY] = MediaStore.EXTRA_VIDEO_QUALITY to 1
                        paramsMap[MediaStore.EXTRA_DURATION_LIMIT] = MediaStore.EXTRA_DURATION_LIMIT to 60
                        extras.forEach { extra -> paramsMap[extra.first] = extra }
                        flyBack<AppCompatActivity>(
                            intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE),
                            isResolveActivity = true,
                            requestCode = requestCode ?: REQUEST_VIDEO_CAPTURE,
                            params = * paramsMap.values.toTypedArray())
                    } catch (e: Exception) {
                        shortToast(getTextRes(R.string.enable_camera_failure))
                    }
                } elseLet {
                    shortToast(getTextRes(R.string.enable_camera_failure))
                }
            }
        }

        fun Fragment.dispatchTakeVideoIntent(requestCode: Int?, vararg extras: Pair<String, Any>) {
            isSdcardExits() falseLet { shortToast(getTextRes(
                R.string.take_video_no_sd_hint)) } elseLet {
                activity?.packageManager?.let {
                    it.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) trueLet {
                        try {
                            val paramsMap = HashMap<String, Pair<String, Any>>()
                            paramsMap[MediaStore.EXTRA_VIDEO_QUALITY] = MediaStore.EXTRA_VIDEO_QUALITY to 1
                            paramsMap[MediaStore.EXTRA_DURATION_LIMIT] = MediaStore.EXTRA_DURATION_LIMIT to 60
                            extras.forEach { extra -> paramsMap[extra.first] = extra }
                            flyBack<Fragment>(
                                intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE),
                                isResolveActivity = true,
                                requestCode = requestCode ?: REQUEST_VIDEO_CAPTURE,
                                params = * paramsMap.values.toTypedArray())
                        } catch (e: Exception) {
                            shortToast(getTextRes(R.string.enable_camera_failure))
                        }
                    } elseLet {
                        shortToast(getTextRes(R.string.enable_camera_failure))
                    }
                }
            }
        }

        /**
         * 唤醒原生相机拍照
         * [extras] 附加数据列表，可选
         * */
        fun Activity.dispatchTakePictureIntent(requestCode: Int?, vararg extras: Pair<String, Any>) {
            isSdcardExits() falseLet { shortToast(getTextRes(
                R.string.take_video_no_sd_hint)) } elseLet {
                packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) trueLet {
                    try {
                        val paramsMap = HashMap<String, Pair<String, Any>>()
                        extras.forEach { extra -> paramsMap[extra.first] = extra }
                        flyBack<AppCompatActivity>(
                            intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                            isResolveActivity = true,
                            requestCode = requestCode ?: REQUEST_PICTURE_CAPTURE,
                            params = * paramsMap.values.toTypedArray())
                    } catch (e: Exception) {
                        shortToast(getTextRes(R.string.enable_camera_failure))
                    }
                } elseLet {
                    shortToast(getTextRes(R.string.enable_camera_failure))
                }
            }
        }

        fun Fragment.dispatchTakePictureIntent(requestCode: Int?, vararg extras: Pair<String, Any>) {
            isSdcardExits() falseLet { shortToast(getTextRes(
                R.string.take_video_no_sd_hint)) } elseLet {
                activity?.packageManager?.let {
                    it.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) trueLet {
                        try {
                            val paramsMap = HashMap<String, Pair<String, Any>>()
                            extras.forEach { extra -> paramsMap[extra.first] = extra }
                            flyBack<Fragment>(
                                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                                isResolveActivity = true,
                                requestCode = requestCode ?: REQUEST_PICTURE_CAPTURE,
                                params = * paramsMap.values.toTypedArray())
                        } catch (e: Exception) {
                            shortToast(getTextRes(R.string.enable_camera_failure))
                        }
                    } elseLet {
                        shortToast(getTextRes(R.string.enable_camera_failure))
                    }
                }
            }

        }
    }
}
