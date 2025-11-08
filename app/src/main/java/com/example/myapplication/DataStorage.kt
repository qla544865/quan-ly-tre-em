//package com.example.myapplication
//
//import android.content.Context
//import com.google.gson.JsonParser
//import java.io.File
//
//data class AppLog(
//    val packageName: String,
//    val timeUsage: Long
//)
//
//fun writeJson(context: Context, fileName: String, obj: Any) {
//    val gson = com.google.gson.Gson()
//    val json = gson.toJson(obj)
//    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { it.write(json.toByteArray()) }
//}
//
//fun <T> readJson(context: Context, fileName: String, clazz: Class<T>): T? {
//    return try {
//        val json = context.openFileInput(fileName).bufferedReader().use { it.readText() }
//        com.google.gson.Gson().fromJson(json, clazz)
//    } catch (e: Exception) {
//        null
//    }
//}
//
//fun readJsonFile(context: Context, fileName: String): String? {
//    val file = File(context.filesDir, fileName)
//    if (!file.exists()) return null
//    return file.readText()
//}
//fun readJsonList(context: Context, fileName: String): List<AppLog> {
//    return try {
//        val json = context.openFileInput(fileName).bufferedReader().use { it.readText() }
//
//        if (json.isBlank()) return emptyList()
//
//        val type = com.google.gson.reflect.TypeToken.getParameterized(List::class.java, AppLog::class.java).type
//        com.google.gson.Gson().fromJson<List<AppLog>>(json, type) ?: emptyList()
//    } catch (e: Exception) {
//        emptyList()
//    }
//}
//
//fun addToJsonArray(context: Context, fileName: String, newItem: AppLog) {
//    val gson = com.google.gson.Gson()
//
//    val list = readJsonList(context, fileName).toMutableList() // luôn != null
//
//    list.add(newItem)
//
//    val json = gson.toJson(list)
//    context.openFileOutput(fileName, Context.MODE_PRIVATE).use { it.write(json.toByteArray()) }
//}
//
//fun getValueFromJson(context: Context, fileName: String, key: String): String? {
//    val jsonString = readJsonFile(context, fileName) ?: return null
//    val jsonObject = JsonParser.parseString(jsonString).asJsonObject
//
//    return if (jsonObject.has(key)) {
//        jsonObject.get(key).asString
//    } else null
//}
//
//fun updateJsonValue(context: Context, fileName: String, key: String, newValue: Long) {
//    // Read current JSON
//    val jsonString = readJsonFile(context, fileName) ?: return
//    val jsonObject = JsonParser.parseString(jsonString).asJsonObject
//
//    // Update key
//    jsonObject.addProperty(key, newValue)
//
//    // Write back to file
//    writeJsonFile(context, fileName, jsonObject.toString())
//}