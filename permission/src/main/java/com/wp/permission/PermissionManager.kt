package com.wp.permission

import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable

/**
 * author : kyle
 * e-mail : 1239878682@qq.com
 * date   : 4/28/21
 * 看了我的代码，感动了吗?
 */
object PermissionManager {

    var disposable: Disposable? = null


    fun requestPermission(activity: FragmentActivity, handler: OnCheckPermissionHandler, vararg permission: WpPermission) {

        val filter=permission.filterNot {
            RxPermissions(activity).isGranted(it.permission)
        }
        if (filter.isEmpty()) {
            handler.onAllGranted()
        } else {
            val sb = StringBuilder()
            sb.append("为了您更好的使用本应用,我们即将你申请以下权限:\n\n")

            filter.forEach {
                val str = when (it) {
                            WpPermission.CAMERA -> {
                                "相机权限:巡检工单回单时的上传拍照\n\n"
                            }
                            WpPermission.WRITE_EXTERNAL_STORAGE -> {
                                "写入存储权限:巡检工单回单时的上传拍照后保存到手机\n\n"
                            }
                            WpPermission.READ_EXTERNAL_STORAGE -> {
                                "读取存储权限:巡检工单回单时,选择手机照片上传\n\n"
                            }
                            WpPermission.ACCESS_FINE_LOCATION -> {
                                "获取精确位置:设备搜索提醒附近设备时，方便查询\n\n"
                            }
                            WpPermission.ACCESS_COARSE_LOCATION -> {
                                "获取粗略位置:设备搜索提醒附近设备时，方便查询\n\n"
                            }
                            WpPermission.READ_PHONE_STATE -> {
                                "获取设备信息:用作收集您的设备信息，提供分析服务\n\n"
                            }
                            WpPermission.CALL_PHONE -> {
                                "直接拨打电话:工单出现问题时，联系管理员，调用电话拨打功能\n\n"
                            }
                        }

                sb.append(str)
            }
            AlertDialog.Builder(activity).setTitle("温馨提示").setMessage(sb.toString()).setNegativeButton("确定") { dialog, _ ->
                dialog.dismiss()
                val permissionStr = filter.map { it.permission }.toTypedArray()
                disposable = RxPermissions(activity).requestEachCombined(*permissionStr).subscribe {
                    when {
                        it.granted -> {
                            handler.onAllGranted()
                            handler.onFinish()
                        }
                        it.shouldShowRequestPermissionRationale -> {
                            handler.onAtLeastOneDeniedCanRemind()
                            handler.onFinish()
                        }
                        else -> {
                            //至少有一个不再询问
                            handler.onAtLeastOneDeniedNotRemind()
                            toSetting(activity)
                            handler.onFinish()
                        }
                    }
                }
            }.create().show()
        }
    }


    fun hasPermission(activity: FragmentActivity, permission: WpPermission): Boolean {
        return RxPermissions(activity).isGranted(permission.permission)
    }

    fun onDestroy() {
        disposable?.dispose()
    }

    fun toSetting(activity: FragmentActivity) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.parse("package:" + activity.packageName)
        activity.startActivity(intent)
    }

}