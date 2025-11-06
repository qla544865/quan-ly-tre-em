package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AppChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pkg = intent.getStringExtra("packageName")
        AppStateViewModel.updateCurrentApp("$pkg")
        Log.d("GLOBAL_DEBUG", "App đang mở: $pkg")
    }
}
