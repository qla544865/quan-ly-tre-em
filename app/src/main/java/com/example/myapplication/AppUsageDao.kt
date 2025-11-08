package com.example.myapplication
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageDao {

    @Insert
    suspend fun insert(appUsageDt: AppUsageData)

    @Delete
    suspend fun delete(appUsageDt: AppUsageData)

    @Update
    suspend fun update(appUsageDt: AppUsageData)

    @Query("SELECT * FROM AppUsageData")
    fun getAll(): Flow<List<AppUsageData>>

    @Query("SELECT * FROM AppUsageData WHERE package_name = :inp LIMIT 1")
    suspend fun getFromPackageName(inp: String): AppUsageData?

    @Query("SELECT * FROM AppUsageData WHERE id = :inp LIMIT 1")
    suspend fun getFromId(inp: Int): AppUsageData?
}