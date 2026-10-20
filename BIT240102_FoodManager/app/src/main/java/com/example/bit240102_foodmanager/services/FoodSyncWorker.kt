package com.example.bit240102_foodmanager.services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bit240102_foodmanager.FoodManagerApplication
import java.io.IOException
import retrofit2.HttpException

class FoodSyncWorker(
    context: Context,
    parameters: WorkerParameters,
) : CoroutineWorker(context, parameters) {
    override suspend fun doWork(): Result {
        val application = applicationContext as FoodManagerApplication
        if (!MockApiServiceFactory.isConfigured(application)) {
            Log.i("BIT240102_Sync", "Configure BIT240102_MOCK_API_BASE_URL to enable synchronization.")
            return Result.success()
        }

        return try {
            application.repository.syncPending()
            Result.success()
        } catch (error: HttpException) {
            Log.e("BIT240102_Sync", "MockAPI returned HTTP ${error.code()}; will retry the pending sync.", error)
            if (error.code() in 400..499 && error.code() != 408 && error.code() != 429) {
                Result.failure()
            } else {
                Result.retry()
            }
        } catch (error: IOException) {
            Log.e("BIT240102_Sync", "MockAPI is unavailable; will retry when connected.", error)
            Result.retry()
        } catch (error: IllegalArgumentException) {
            Log.e("BIT240102_Sync", "MockAPI endpoint configuration is invalid.", error)
            Result.failure()
        }
    }
}
