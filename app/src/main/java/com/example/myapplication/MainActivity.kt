package com.example.myapplication

import android.R
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
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
import androidx.room.Room
import kotlinx.coroutines.delay


var systems_apps = listOf<String>("YouTube")


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var text_cnt:String  = ""

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_usage.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val appUsageDao = db.appUsageDao()

        val _glo_context =this


        setContent {
            val scrollState = rememberScrollState()
            val currentApp = AppStateViewModel.currentApp.observeAsState("")
            var app_list = appUsageDao.getAll()

            LaunchedEffect(Unit) {
                while (true) {
                    val app = appUsageDao.getFromPackageName(AppStateViewModel.cur_app_value)

                    val name__ = getAppNameFromPackageName(
                        _glo_context,
                        AppStateViewModel.cur_app_value
                    )

                    Log.d("Test", "using app: ${name__}")
                    Log.d("TestB", "App: ${app}")
                    delay(1000)

                    app_list = appUsageDao.getAll()


                    if (app == null) {
                        if (
                            AppStateViewModel.cur_app_value == "com.google.android.apps.nexuslauncher" ||
                            AppStateViewModel.cur_app_value == ""
                            ) {
                            continue
                        }

                        val name = getAppNameFromPackageName(
                            _glo_context,
                            AppStateViewModel.cur_app_value
                        )

                        if (isSystemApp(
                                _glo_context,
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

                        new_app_usage_data.app_name = getAppNameFromPackageName(_glo_context, new_app_usage_data.package_name)
                        appUsageDao.insert(new_app_usage_data)
                    } else {
                        Log.d("TestA", "DATA: ${app.package_name} has been used for ${app.time_usage}")

                        app.time_usage = app.time_usage+1
                        appUsageDao.update(app)

                    }

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
