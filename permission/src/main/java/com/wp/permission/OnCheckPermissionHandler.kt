package com.wp.permission

/**
 * author : kyle
 * e-mail : 1239878682@qq.com
 * date   : 4/28/21
 * 看了我的代码，感动了吗?
 */
interface OnCheckPermissionHandler {
    fun onAllGranted()

    fun onAtLeastOneDeniedCanRemind()

    fun onAtLeastOneDeniedNotRemind()

    fun onFinish()

}