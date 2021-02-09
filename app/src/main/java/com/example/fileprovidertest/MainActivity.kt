package com.example.fileprovidertest

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
                            + File.separator
                            + "apks"
                            + File.separator
                            + "SimCheck.apk"
                )
                Log.i("NFL", "=========== apk file path: " + file.absolutePath)
                var uri = FileProvider.getUriForFile(this, "androidx", file)
                Log.i("NFL", "=========== $uri")
                intent.setDataAndType(uri, "application/vnd.android.package-archive")
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
        }
    }
}