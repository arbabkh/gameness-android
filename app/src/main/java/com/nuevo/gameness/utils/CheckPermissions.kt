package com.nuevo.gameness.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class CheckPermissions(activityResultCaller: ActivityResultCaller) {

    private var permissionResultLauncher: ActivityResultLauncher<Array<String>>
    private var intentResultLauncher: ActivityResultLauncher<Intent>
    private var context: Context? = null
    private var requestPermission = ""

    init {
        activityResultCaller.apply {
            context = when(this) {
                is Fragment -> requireContext()
                is ComponentActivity -> applicationContext
                else -> null
            }
            permissionResultLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    permissions.entries.forEach { result ->
                        if (result.value) {
                            permissionResultSuccess(result.key)
                        }else{
                            permissionResultDontAsk(result.key)
                        }
                    }
                }
            intentResultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.let { data ->
                            intentResult(data, requestPermission)
                        }
                    }
                }
        }
    }

    fun permissionResultLaunch(permission: String) {
        context?.let { requireContext ->
            if (ContextCompat.checkSelfPermission(requireContext, permission) != PackageManager.PERMISSION_GRANTED)
                permissionResultLauncher.launch(arrayOf(permission))
            else permissionResultSuccess(permission)
        }
    }

    fun intentResultLaunch(data: Intent, requestPermission: String) {
        this.requestPermission = requestPermission
        intentResultLauncher.launch(data)
    }

    abstract fun permissionResultSuccess(permission: String)
    abstract fun permissionResultDontAsk(permission: String)

    open fun intentResult(data: Intent, requestPermission: String) {
        Log.i("PERMISSIONS", "Success Result Intent: $data")
    }

}