package com.example.bit240102_foodmanager.services

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

object SyncScheduler {
    private const val BIT240102_UNIQUE_WORK_NAME = "BIT240102_MockApiAutoSync"

    fun schedule(context: Context) {
        if (!MockApiServiceFactory.isConfigured(context)) return

        val request =
            OneTimeWorkRequestBuilder<FoodSyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build(),
                )
                .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            BIT240102_UNIQUE_WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            request,
        )
    }
}

object MockApiServiceFactory {
    fun isConfigured(context: Context): Boolean {
        val endpoint = context.getSharedPreferences("BIT240102_Settings", Context.MODE_PRIVATE)
            .getString("BIT240102_MockApiBaseUrl", null)
            ?.takeIf(String::isNotBlank)
            ?: com.example.bit240102_foodmanager.BuildConfig.BIT240102_MOCK_API_BASE_URL
        return endpoint.endsWith(".mockapi.io/api/v1/") &&
            !endpoint.contains("your-project", ignoreCase = true)
    }

    fun create(context: Context): MockApiService {
        val endpoint = context.getSharedPreferences("BIT240102_Settings", Context.MODE_PRIVATE)
            .getString("BIT240102_MockApiBaseUrl", null)
            ?.takeIf(String::isNotBlank)
            ?: com.example.bit240102_foodmanager.BuildConfig.BIT240102_MOCK_API_BASE_URL
        require(endpoint.startsWith("https://") && endpoint.endsWith("/")) {
            "BIT240102_MOCK_API_BASE_URL must be an HTTPS URL ending in /."
        }

        return retrofit2.Retrofit.Builder()
            .baseUrl(endpoint)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .client(
                okhttp3.OkHttpClient.Builder()
                    .callTimeout(java.time.Duration.ofSeconds(20))
                    .build(),
            )
            .build()
            .create(MockApiService::class.java)
    }
}
