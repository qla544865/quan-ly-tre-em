package com.example.myapplication

import android.app.Service
import android.content.Context

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.delay


import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import kotlinx.coroutines.isActive
import kotlinx.coroutines.delay

class UpdateTimeUsageService: Service() {
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "service_channel")
            .setContentTitle("QuanLyTG")
            .setContentText("")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()


//        startForeground(1, notification)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                1,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(1, notification)
        }

        startTrackingTask()

        return START_STICKY
    }


    private fun startTrackingTask() {
        Log.d("LOGG", "runing")
        scope.launch {
            trackCurrentApp()
        }
    }

    override fun onBind(intent: Intent?) = null




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

    suspend fun trackCurrentApp() {

         val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_usage.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val appUsageDao = db.appUsageDao()

        var app_list = appUsageDao.getAll()

        val intent_exit_app = Intent(Intent.ACTION_MAIN)
        intent_exit_app.addCategory(Intent.CATEGORY_HOME)
        intent_exit_app.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        var curr_app_name = ""
        var text_cnt:String  = ""

        val ctx = applicationContext


        while (true) {
            val app = appUsageDao.getFromPackageName(AppStateViewModel.cur_app_value)

            val name__ = getAppNameFromPackageName(
                ctx,
                AppStateViewModel.cur_app_value
            )

            Log.d("Test", "using app: ${name__}")
            Log.d("TestB", "App: ${app}")
            Thread.sleep(1000)
            app_list = appUsageDao.getAll()


            if (app == null) {
                if (
                    homescreen_packages.contains(AppStateViewModel.cur_app_value) ||
                    AppStateViewModel.cur_app_value == ""
                ) {
                    continue
                }

                val name = getAppNameFromPackageName(
                    ctx
                    ,
                    AppStateViewModel.cur_app_value
                )

                curr_app_name = name

                if (isSystemApp(
                        ctx,
                        AppStateViewModel.cur_app_value
                    ) && !systems_apps.contains(name)) {
                    continue
                }

                val is_found = appUsageDao.getFromPackageName(AppStateViewModel.cur_app_value)
                if (is_found != null) {
                    continue
                }

                Log.d("TestA", "NO DATA")



                val new_app_usage_data = AppUsageData(
                    package_name = AppStateViewModel.cur_app_value,
                    app_name = name
                )

                new_app_usage_data.app_name = getAppNameFromPackageName(ctx, new_app_usage_data.package_name)
                appUsageDao.insert(new_app_usage_data)
            } else {
                app.time_usage = app.time_usage+1
                app.time_left = app.time_left+1
                appUsageDao.update(app)
                Log.d("TestA", "DATA: ${app.package_name} has been used for ${app.time_usage}")
                if (app.time_left >= app.limit_time &&  app.limit_time > 0) {
                    startActivity(intent_exit_app)
                }

            }

        }
    }

}