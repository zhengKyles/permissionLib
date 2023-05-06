package com.wp.permission

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

}