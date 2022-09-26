package com.jin.template.util

import android.content.Context
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

@Suppress("UNUSED")
object PermissionDialog {
    fun with(context: Context) = PermissionDialog(context)

    class PermissionDialog(context: Context) {
        private var permissions: Array<out String>? = null
        private var grantedListener: (() -> Unit)? = null
        private var deniedListener: (() -> Unit)? = null

        fun setPermission(vararg permissions: Array<String>) = apply {
            this.permissions = permissions.flatMap { it.toList() }.toTypedArray()
        }

        fun setPermission(vararg permissions: String) = apply {
            this.permissions = permissions
        }

        fun doOnGranted(callback: () -> Unit) = apply { grantedListener = callback }
        fun doOnDenied(callback: () -> Unit) = apply { deniedListener = callback }

        fun check() {
            TedPermission.create()
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        grantedListener?.invoke()
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        Debug.e("[Permission Denied] permission=${deniedPermissions.toString()}")
                        deniedListener?.invoke()
                    }
                })
                .apply { if (permissions != null) setPermissions(*permissions!!) }
                .check()
        }
    }
}