package com.example.fileprovidertest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import personal.nfl.permission.annotation.GetPermissions4AndroidX
import personal.nfl.permission.support.util.AbcPermission
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 建议在 application 添加
        AbcPermission.install(this)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val builder = VmPolicy.Builder()
            builder.penaltyDeath()
            builder.detectNonSdkApiUsage()
            StrictMode.setVmPolicy(builder.build())
        }

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_install_app -> {
                val apkFilePath =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator + "apks" + File.separator + "SimCheck.apk"
                installApk(this, apkFilePath)
            }
        }
    }

    /**
     * 安装APK文件
     */
    @GetPermissions4AndroidX(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.REQUEST_INSTALL_PACKAGES,
        Manifest.permission.INSTALL_PACKAGES
    )
    private fun installApk(context: Context, fileName: String?) {
        val apkFile = File(fileName)
        if (!apkFile.exists()) {
            Toast.makeText(context, "未找到安装文件", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        var uri: Uri? = null
        uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i("NFL", "通过 ProviderContent 安装的")
            // intent.action = Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            FileProvider.getUriForFile(context, "androidx", apkFile)
        } else {
            Log.i("NFL", "通过 apkFile 安装的")
            Uri.fromFile(apkFile)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        context.startActivity(intent)
    }
}