package com.wp.permission

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
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
                        handler.onFinish()
                    }
                }
            }
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


    fun hasSystemAlertPermission(context: Context): Boolean {
        //检查是否已经授予权限
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    //请求悬浮窗权限
    @TargetApi(Build.VERSION_CODES.M)
    private fun getOverlayPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)
    }
    fun requestSystemAlertPermission(context: Context) {
        if (hasSystemAlertPermission(context)) {
        } else {
            getOverlayPermission(context)
        }
    }
}