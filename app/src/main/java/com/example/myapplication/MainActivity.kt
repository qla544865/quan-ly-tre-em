package com.example.myapplication

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.toString
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

//import com.example.myapplication.writeJson
//import com.example.myapplication.readJsonList
//import com.example.myapplication.addToJsonArray

import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.delay

var systems_apps = listOf<String>("YouTube")

var homescreen_packages = listOf<String>("com.google.android.apps.nexuslauncher")


class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channel = NotificationChannel(
            "service_channel", "App_quan_ly", NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_usage.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val appUsageDao = db.appUsageDao()

        var app_list = appUsageDao.getAll()

        var text_cnt:String  = ""


        val _glo_context =this


//        val intent = Intent(this, UpdateTimeUsageService::class.java)
//        ContextCompat.startForegroundService(this, intent)

        val intent = Intent(this, UpdateTimeUsageService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }


        val intent_exit_app = Intent(Intent.ACTION_MAIN)
        intent_exit_app.addCategory(Intent.CATEGORY_HOME)
        intent_exit_app.flags = Intent.FLAG_ACTIVITY_NEW_TASK



        setContent {
            val scrollState = rememberScrollState()
            val currentApp = AppStateViewModel.currentApp.observeAsState("")



            LaunchedEffect(Unit) {
//                var curr_app_name = ""
//
                while (true) {
                    app_list = appUsageDao.getAll()
                    delay(100)
                }
            }

            Column {
                Text(
                    text = "App đang chạy: ${currentApp.value}",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(
                            top = 16.dp,
                            bottom = 0.dp,
                            start = 16.dp,
                            end=16.dp
                        ),

                )



                AppUsageScreen(appUsageDao)

            }



        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    fun getAppNameFromPackageName(context: Context, packageName: String): String {
        val packageManager: PackageManager = context.packageManager
        return try {
            val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            "(error)"
        }
    }

    fun isSystemApp(context: Context, packageName: String): Boolean {
        try {
            val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
            if ((packageInfo.applicationInfo?.flags?.and(ApplicationInfo.FLAG_SYSTEM)) != 0) {
                return true
            } else {
                return false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }
}
