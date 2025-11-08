package com.example.myapplication

import android.widget.Button
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AppUsageScreen(appUsageDao: AppUsageDao) {

    val scope = rememberCoroutineScope()
    val appUsages by appUsageDao.getAll().collectAsState(initial = emptyList())
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Row {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (text.isNotBlank()) {
                    scope.launch {
//                        appUsageDao.insert(AppUsageData(app_name = text, package_name = text))
                        text = ""
                    }
                }
            }) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(16.dp))

        appUsages.forEach { dt ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(dt.app_name)
                Button(onClick = {
                    scope.launch { appUsageDao.delete(dt) }
                }) {
                    Text("Delete")
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
