package com.example.myapplication

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    private var lastPackage: String = ""
    private var current_package: String = ""


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d("ACCESS_TEST", "AccessibilityService ĐANG CHẠY")

        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
            event?.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ) {
            val pkg = event.packageName?.toString()
            if (pkg != null && lastPackage != pkg) {

                if (pkg != "com.example.myapplication") {
                    lastPackage = pkg

                    val intent = Intent("APP_FOREGROUND_CHANGED")
                    intent.putExtra("packageName", pkg)
                    intent.setPackage(applicationContext.packageName)
                    sendBroadcast(intent)
                    Log.d("GLOBAL_DEBUG", "SENT: $pkg")
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("GLOBAL_DEBUG", "Service ĐÃ CHẠY")
    }

    override fun onInterrupt() {}
}
