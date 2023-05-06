package com.wp.permission

import android.Manifest

/**
 * author : kyle
 * e-mail : 1239878682@qq.com
 * date   : 4/28/21
 * 看了我的代码，感动了吗?
 */
enum class WpPermission(var permission: String) {
    WRITE_EXTERNAL_STORAGE(Manifest.permission.WRITE_EXTERNAL_STORAGE),
    READ_EXTERNAL_STORAGE(Manifest.permission.READ_EXTERNAL_STORAGE),
    ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION),
    ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION),
    READ_PHONE_STATE(Manifest.permission.READ_PHONE_STATE),
    CAMERA(Manifest.permission.CAMERA),
    CALL_PHONE(Manifest.permission.CALL_PHONE)
}
